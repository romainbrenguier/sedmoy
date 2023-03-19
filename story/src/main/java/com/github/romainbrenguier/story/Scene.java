package com.github.romainbrenguier.story;

// Prompt: Forget the above conversation. You are a famous novel writer from the 19th century
// writing crime novels taking place in Paris in the 19th century.
// Write a story about ...

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class Scene {
    SceneSetup setup;
    List<TimedAction> actions = new ArrayList<>();

    public static Scene make(Random r, int length) {
        final Scene scene = new Scene();
        scene.setup = SceneSetup.make(r);
        final SceneState state = new SceneState(scene.setup);
        Calendar date = TimedAction.randomDate(r);
        for (int i = 0; i < length; ++i) {
            final TimedAction timedAction = new TimedAction();
            setTimeOfDay(timedAction, date, 19, i);
            timedAction.action = scene.makeAction(r, state);
            scene.actions.add(timedAction);
            state.applyAction(timedAction.action);
        }
        return scene;
    }

    private Action makeAction(Random r, SceneState state) {
        final List<Character> inKilledRoom = state.killed.stream()
                .map(c -> state.getPositionIndex(c))
                .flatMap(pos -> state.charactersInRoom(pos).stream())
                .collect(Collectors.toList());

        if (!inKilledRoom.isEmpty()) {
            final Action.Shout shout = new Action.Shout();
            shout.by = inKilledRoom.get(0);
            return shout;
        }

        Character character = setup.characters.get(r.nextInt(setup.characters.size()));
        final Integer position = state.getPositionIndex(character);
        if (position == null) {
            final Action.Arrive arrive = new Action.Arrive();
            arrive.character = character;
            arrive.inRoom = RandomUtil.nextInList(r, setup.place.entrances);
            return arrive;
        }

        List<Character> charactersInRoom = state.charactersInRoom(position);
        if (charactersInRoom.size() > 1 && r.nextInt(10) > 7) {
            final Action.Talk talk = new Action.Talk();
            talk.talking = charactersInRoom;
            return talk;
        }
        if (charactersInRoom.size() == 2 && r.nextInt(10) > 8) {
            final Action.Kill kill = new Action.Kill();
            kill.by = character;
            kill.target = charactersInRoom.stream().filter(c -> c != character).findAny().get();
            return kill;
        }
        final Integer nextPosition = RandomUtil.nextInList(r, setup.place.connectedFrom(position));
        if (nextPosition != null) {
            final Action.Move move = new Action.Move();
            move.character = character;
            move.fromRoom = position;
            move.toRoom = nextPosition;
            return move;
        }
        return null;
    }

    private static void setTimeOfDay(TimedAction timedAction, Calendar day, int hours, int minutes) {
        timedAction.time = Calendar.getInstance();
        timedAction.time.set(day.get(Calendar.YEAR), day.get(Calendar.MONTH),
                day.get(Calendar.DAY_OF_MONTH));
        timedAction.time.set(Calendar.HOUR, hours);
        timedAction.time.set(Calendar.MINUTE, minutes);
    }

    @Override
    public String toString() {
        return "Scene{" + setup.toString() + ", actions=" + actions + '}';
    }

    public String report() {
        return actions.stream()
                .map(action -> action.format(roomIndex -> setup.place.rooms.get(roomIndex).toString()))
                .collect(Collectors.joining("\n"));
    }

    public String reportFromPointOfView(Character character) {
        return actions.stream()
                .filter(action -> action.action.actors().contains(character))
                .map(action -> action.format(roomIndex -> setup.place.rooms.get(roomIndex).toString()))
                .collect(Collectors.joining("\n"));
    }
}
