package com.github.romainbrenguier.story;

import com.github.romainbrenguier.story.places.Mansion;
import com.github.romainbrenguier.story.places.Place;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class SceneSetup {
    Place place;
    List<Character> characters = new ArrayList<>();

    public static SceneSetup make(Random r) {
        final SceneSetup setup = new SceneSetup();
        setup.place = new Mansion().make(r);
        int nbCharacters = r.nextInt(5) + 5;
        setup.characters = IntStream.range(0, nbCharacters)
                .mapToObj(i -> Character.random(r)).collect(Collectors.toList());
        return setup;
    }

    @Override
    public String toString() {
        return "place:\n  " + place +
                "\ncharacters:\n  " + characters.stream()
                .map(c -> c.name.toString() + ":" + c.details()).collect(Collectors.joining("\n  "));
    }
}
