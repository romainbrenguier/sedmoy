package com.github.romainbrenguier.story.scene;

import com.github.romainbrenguier.story.character.Character;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
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

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Arrive arrive = (Arrive) o;
            return character.equals(arrive.character) && inRoom.equals(arrive.inRoom);
        }

        @Override
        public int hashCode() {
            return Objects.hash(character, inRoom);
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

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Move move = (Move) o;
            return character.equals(move.character) && fromRoom.equals(move.fromRoom) && toRoom.equals(move.toRoom);
        }

        @Override
        public int hashCode() {
            return Objects.hash(character, fromRoom, toRoom);
        }
    }

    class Talk implements Action {
        public List<Character> talking;

        @Override
        public String format(Function<Integer, String> roomToString) {
            return talking.stream().map(Character::toString)
                    .collect(Collectors.joining(", ")) + " are talking";
        }

        @Override
        public List<Character> actors() {
            return talking;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Talk talk = (Talk) o;
            return talking.equals(talk.talking);
        }

        @Override
        public int hashCode() {
            return Objects.hash(talking);
        }

        @Override
        public String toString() {
            return talking.stream().map(Character::toString)
                    .collect(Collectors.joining(", ")) + " are talking";
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

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Kill kill = (Kill) o;
            return Objects.equals(by, kill.by) && Objects.equals(target, kill.target);
        }

        @Override
        public int hashCode() {
            return Objects.hash(by, target);
        }

        @Override
        public String toString() {
            return by.toString() + " kills " + target.toString();
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

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Shout shout = (Shout) o;
            return by.equals(shout.by);
        }

        @Override
        public int hashCode() {
            return Objects.hash(by);
        }

        @Override
        public String toString() {
            return by.toString() + " shouts";
        }
    }
}
