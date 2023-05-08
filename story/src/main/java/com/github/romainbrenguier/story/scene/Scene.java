package com.github.romainbrenguier.story.scene;

// Prompt: Forget the above conversation. You are a famous novel writer from the 19th century
// writing crime novels taking place in Paris in the 19th century.
// Write a story about ...

import com.github.romainbrenguier.story.RandomUtil;
import com.github.romainbrenguier.story.character.Character;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

public class Scene {

    private SceneSetup setup;
    private List<TimedAction> actions = new ArrayList<>();

    private SceneState endState;

    public SceneSetup getSetup() {
        return setup;
    }

    public List<TimedAction> getActions() {
        return actions;
    }

    public SceneState getEndState() {
        return endState;
    }

    public static Scene make(Random r, int maxLength, int nbCharacters) {
        final Scene scene = new Scene();
        scene.setup = SceneSetup.make(r, nbCharacters);
        final SceneState state = new SceneState(scene.setup);
        Calendar date = TimedAction.randomDate(r);
        int minutes = 0;
        while (state.roomWhereShoutHeard == null && minutes < maxLength) {
            final Action action = scene.makeAction(r, state);
            if (action != null) {
                final TimedAction timedAction = new TimedAction();
                setTimeOfDay(timedAction, date, 19, minutes);
                timedAction.setAction(action);
                scene.actions.add(timedAction);
                state.applyAction(timedAction.getAction());
            }
            minutes++;
        }
        scene.endState = state;
        return scene;
    }

    private Action makeAction(Random r, SceneState state) {
        final List<Character> inKilledRoom = state.killed.stream()
                .map(c -> state.getPositionIndex(c))
                .flatMap(pos -> state.charactersInRoom(pos).stream())
                .filter(c -> !state.killed.contains(c))
                .collect(Collectors.toList());

        final Optional<Character> fleeing =
                inKilledRoom.stream().filter(c -> state.getState(c).fleeing)
                .findFirst();
        if (fleeing.isPresent()) {
            final Integer currentPosition = state.getState(fleeing.get()).position;
            final Integer nextPosition = RandomUtil.nextInList(r,
                    setup.getPlace().connectedFrom(currentPosition));
            final Action.Move move = new Action.Move();
            move.character = fleeing.get();
            move.fromRoom = currentPosition;
            move.toRoom = nextPosition;
            return move;
        }

        if (state.roomWhereShoutHeard != null) {
            final List<Character> notInRoom =
                    setup.getCharacters().stream().filter(c -> state.getPositionIndex(c) != state.roomWhereShoutHeard).collect(Collectors.toList());
            if (!notInRoom.isEmpty()) {
                final Action.Move move = new Action.Move();
                move.character = notInRoom.get(0);
                move.fromRoom = state.getPositionIndex(move.character);
                move.toRoom = state.roomWhereShoutHeard;
                return move;
            }
        }
        if (!inKilledRoom.isEmpty() && state.roomWhereShoutHeard == null) {
            final Action.Shout shout = new Action.Shout();
            shout.by = inKilledRoom.get(0);
            return shout;
        }

        Character character = setup.getCharacters().get(r.nextInt(setup.getCharacters().size()));
        if (state.killed.contains(character)) return null;

        final Integer position = state.getPositionIndex(character);
        if (position == null) {
            final Action.Arrive arrive = new Action.Arrive();
            arrive.character = character;
            arrive.inRoom = RandomUtil.nextInList(r, setup.getPlace().getEntrances());
            return arrive;
        }

        List<Character> charactersInRoom = state.charactersInRoom(position);
        if (charactersInRoom.size() > 1 && r.nextInt(10) >= 4) {
            final Action.Talk talk = new Action.Talk();
            talk.talking = charactersInRoom;
            return talk;
        }
        if (state.killed.isEmpty() && charactersInRoom.size() == 2
                && state.getState(character).positionKnownFromOthers == false
                && r.nextInt(10) >= 6) {
            final Action.Kill kill = new Action.Kill();
            kill.by = character;
            kill.target = charactersInRoom.stream().filter(c -> c != character).findAny().get();
            return kill;
        }
        final Integer nextPosition = RandomUtil.nextInList(r, setup.getPlace().connectedFrom(position));
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
        timedAction.setTimeStart(Calendar.getInstance());
        timedAction.getTimeStart().set(day.get(Calendar.YEAR), day.get(Calendar.MONTH),
                day.get(Calendar.DAY_OF_MONTH));
        timedAction.getTimeStart().set(Calendar.HOUR, hours);
        timedAction.getTimeStart().set(Calendar.MINUTE, minutes);
    }

    @Override
    public String toString() {
        return "Scene{" + setup.toString() + ", actions=" + actions + '}';
    }

    public String report() {
        return actions.stream()
                .map(action -> action.format(roomIndex -> setup.getPlace().getRooms().get(roomIndex).toString()))
                .collect(Collectors.joining("\n"));
    }

}