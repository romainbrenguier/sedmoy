package com.github.romainbrenguier.story;

import com.github.romainbrenguier.story.places.Room;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public interface Action {

    String format(Function<Integer, String> roomToString);

    List<Character> actors();

    class Arrive implements Action {
        Character character;
        Integer inRoom;

        @Override
        public String toString() {
            return character + " arrives in " + inRoom + ".";
        }

        @Override
        public String format(Function<Integer, String> roomToString) {
            return character + " arrives in " + roomToString.apply(inRoom) + ".";
        }

        @Override
        public List<Character> actors() {
            return Collections.singletonList(character);
        }
    }

    class Move implements Action {
        public Character character;
        public Integer fromRoom;
        public Integer toRoom;

        @Override
        public String toString() {
            return character + " goes from " + fromRoom + " to " + toRoom + ".";
        }

        @Override
        public String format(Function<Integer, String> roomToString) {
            return character + " goes from " + roomToString.apply(fromRoom) + " to " + roomToString.apply(toRoom) +
                    ".";
        }

        @Override
        public List<Character> actors() {
            return Collections.singletonList(character);
        }
    }

    class Talk implements Action {
        List<Character> talking;

        @Override
        public String format(Function<Integer, String> roomToString) {
            return talking.stream().map(Character::toString)
                    .collect(Collectors.joining(", ")) + " are talking";
        }

        @Override
        public List<Character> actors() {
            return talking;
        }


    }

    class Kill implements Action {
        Character by;
        Character target;

        @Override
        public String format(Function<Integer, String> roomToString) {
            return by.toString() + " kills " + target.toString();
        }

        @Override
        public List<Character> actors() {
            return Arrays.asList(by, target);
        }
    }

    class Shout implements Action {
        Character by;

        @Override
        public String format(Function<Integer, String> roomToString) {
            return by.toString() + " shouts";
        }

        @Override
        public List<Character> actors() {
            return Collections.singletonList(by);
        }
    }
}
