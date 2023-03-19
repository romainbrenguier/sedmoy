package com.github.romainbrenguier.story;

import com.github.romainbrenguier.story.places.Room;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public interface Action {

    String format(Function<Integer, String> roomToString);

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
    }

    class Talk implements Action {
        List<Character> talking;

        @Override
        public String format(Function<Integer, String> roomToString) {
            return talking.stream().map(Character::toString)
                    .collect(Collectors.joining(", ")) + "are talking";
        }
    }

    class Kill {
        Character by;
        Character target;
    }

    class Shout {
        Character by;
    }
}
