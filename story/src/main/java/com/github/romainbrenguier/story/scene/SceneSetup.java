package com.github.romainbrenguier.story.scene;

import com.github.romainbrenguier.story.character.Character;
import com.github.romainbrenguier.story.places.Mansion;
import com.github.romainbrenguier.story.places.Place;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class SceneSetup {
    private Place place;
    private List<Character> characters = new ArrayList<>();

    public Place getPlace() {
        return place;
    }

    public List<Character> getCharacters() {
        return characters;
    }

    public static SceneSetup make(Random r, int nbCharacters) {
        final SceneSetup setup = new SceneSetup();
        setup.place = new Mansion().make(r);
        setup.characters = IntStream.range(0, nbCharacters)
                .mapToObj(i -> Character.random(r)).collect(Collectors.toList());
        return setup;
    }

    @Override
    public String toString() {
        return "place:\n  " + place +
                "\ncharacters:\n  " + characters.stream()
                .map(c -> c.getName().toString() + ":" + c.details()).collect(Collectors.joining("\n  "));
    }
}
