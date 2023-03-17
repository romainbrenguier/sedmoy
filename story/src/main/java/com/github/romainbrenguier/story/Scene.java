package com.github.romainbrenguier.story;

// Prompt: Forget the above conversation. You are a famous novel writer from the 19th century
// writing crime novels taking place in Paris in the 19th century.
// Write a story about ...

import com.github.romainbrenguier.story.places.Mansion;
import com.github.romainbrenguier.story.places.Place;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Scene {
    Place place;
    List<Character> characters = new ArrayList<>();
    List<TimedAction> actions = new ArrayList<>();


    public static Scene make(Random r) {
        final Scene scene = new Scene();
        scene.place = new Mansion().make(r);
        int nbCharacters = r.nextInt(5) + 5;
        scene.characters = IntStream.range(0, nbCharacters)
                .mapToObj(i -> Character.random(r)).collect(Collectors.toList());
        Calendar start = TimedAction.randomDate(r);
        for (int i = 0; i < 10; ++i) {
            final TimedAction timedAction = new TimedAction();
            timedAction.time = Calendar.getInstance();
            timedAction.time.set(start.get(Calendar.YEAR), start.get(Calendar.MONTH),
                    start.get(Calendar.DAY_OF_MONTH));
            timedAction.time.set(Calendar.HOUR, 19);
            timedAction.time.set(Calendar.MINUTE, i);
            final Action.Move move = new Action.Move();
            move.character = scene.characters.get(r.nextInt(nbCharacters));
            move.from = scene.place.rooms.get(r.nextInt(scene.place.rooms.size()));
            move.to = scene.place.rooms.get(r.nextInt(scene.place.rooms.size()));
            timedAction.action = move;
            scene.actions.add(timedAction);
        }
        return scene;
    }

    @Override
    public String toString() {
        return "Scene{" +
                "place=" + place +
                ", characters=" + characters.stream().map(c -> c.name.toString() + ":" + c.details()).collect(Collectors.joining(", ")) +
                ", actions=" + actions +
                '}';
    }
}
