package com.github.romainbrenguier.story;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

public class Personality {
    Map<PersonalityTrait, Integer> traits = new LinkedHashMap<>();

    public static Personality random(Random r) {
        final int index1 = r.nextInt(PersonalityTrait.values().length);
        int index2;
        do {
            index2 = r.nextInt(PersonalityTrait.values().length);
        } while (index2 == index1);
        final Personality personality = new Personality();
        personality.traits.put(PersonalityTrait.values()[index1], 1);
        personality.traits.put(PersonalityTrait.values()[index2], 2);
        return personality;
    }

    @Override
    public String toString() {
        return traits.entrySet().stream().map(
                entry -> entry.getKey().toString() + " " + entry.getValue()
        ).collect(Collectors.joining(", ", "personality: ", ""));
    }
}
