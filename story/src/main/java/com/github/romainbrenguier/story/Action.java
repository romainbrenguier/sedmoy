package com.github.romainbrenguier.story;

import com.github.romainbrenguier.story.places.Room;

import java.util.List;

public interface Action {

    class Arrive implements Action {
        Character character;
        Room in;

        @Override
        public String toString() {
            return character + " arrives in " + in + ".";
        }
    }

    class Move implements Action {
        public Character character;
        public Room from;
        public Room to;

        @Override
        public String toString() {
            return character + " goes " +
                    " from " + from +
                    " to " + to + ".";
        }
    }

    class Talk {
        List<Character> talking;
    }

    class Kill {
        Character by;
        Character target;
    }

    class Shout {
        Character by;
    }
}
