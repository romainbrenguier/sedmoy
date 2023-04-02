package com.github.romainbrenguier.story.places;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class Place {
    PlaceType type;
    public List<Room> rooms = new ArrayList<>();

    /** Correspond to indexes in {@link #rooms} */
    public List<Integer> entrances = Collections.singletonList(0);
    ListMultimap<Integer, Integer> connections = ArrayListMultimap.create();
    Map<Integer, Integer> stairsUp = new HashMap<>();
    Map<Integer, Integer> stairsDown = new HashMap<>();


    Place(PlaceType type) {
        this.type = type;
    }

    int addRoom(Room r) {
        rooms.add(r);
        return rooms.size() - 1;
    }

    void connect(int roomIndex1, int roomIndex2) {
        connections.put(roomIndex1, roomIndex2);
        connections.put(roomIndex2, roomIndex1);
    }

    void connectByStairs(int roomBelow, int roomAbove) {
        stairsUp.put(roomBelow, roomAbove);
        stairsUp.put(roomAbove, roomBelow);
    }

    public boolean areConnected(int roomIndex1, int roomIndex2) {
        return connections.get(roomIndex1).contains(roomIndex2);
    }

    public List<Integer> connectedFrom(int roomIndex) {
        return connections.get(roomIndex);
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append("[->]").append(entrances);
        for (int i = 0; i < rooms.size(); ++i) {
            builder.append(" (" + i + ") ").append(rooms.get(i));
        }
        return builder.toString();
    }

    public Function<Integer, String> roomFormatter() {
        return roomIndex -> {
            if (roomIndex == null) return "nowhere";
            final Room room = rooms.get(roomIndex);
            return room == null ? "nowhere" : room.toString();
        };
    }
}
