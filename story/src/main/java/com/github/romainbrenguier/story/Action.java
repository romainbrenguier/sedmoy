package com.github.romainbrenguier.story;

import com.github.romainbrenguier.story.places.Room;

import java.util.List;

public interface Action {

    class Arrive {
        Character character;
        Room in;
    }

    class Move implements Action {
        public Character character;
        public Room from;
        public Room to;
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
