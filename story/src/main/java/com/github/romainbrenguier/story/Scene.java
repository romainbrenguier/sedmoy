package com.github.romainbrenguier.story;

// Prompt: Forget the above conversation. You are a famous novel writer from the 19th century
// writing crime novels taking place in Paris in the 19th century.

import com.github.romainbrenguier.story.places.Mansion;
import com.github.romainbrenguier.story.places.Place;

import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Scene {
    Place place;
    List<Character> characters;
    List<TimedAction> actions;


    public static Scene make(Random r) {
        final Scene scene = new Scene();
        scene.place = new Mansion().make(r);
        int nbCharacters = r.nextInt(5) + 5;
        scene.characters = IntStream.range(0, nbCharacters)
                .mapToObj(i -> Character.random(r)).collect(Collectors.toList());
        Date start = TimedAction.randomDate(r);
        for (int i = 0; i < 10; ++i) {
            final TimedAction timedAction = new TimedAction();
            timedAction.time = new Date(start.getTime());
            timedAction.time.setHours(19);
            timedAction.time.setMinutes(i);
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
                ", characters=" + characters +
                ", actions=" + actions +
                '}';
    }
}
