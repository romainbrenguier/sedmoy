package com.github.romainbrenguier.story.places;

public class Room {
    RoomType type;

    Room(RoomType type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return type.toString();
    }
}
