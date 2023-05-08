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
    private final List<Room> rooms = new ArrayList<>();

    private final List<Integer> entrances = Collections.singletonList(0);
    private final ListMultimap<Integer, Integer> connections = ArrayListMultimap.create();
    Map<Integer, Integer> stairsUp = new HashMap<>();
    Map<Integer, Integer> stairsDown = new HashMap<>();


    Place(PlaceType type) {
        this.type = type;
    }

    int addRoom(Room r) {
        getRooms().add(r);
        return getRooms().size() - 1;
    }

    void connect(int roomIndex1, int roomIndex2) {
        connections.put(roomIndex1, roomIndex2);
        connections.put(roomIndex2, roomIndex1);
    }

    void connectByStairs(int roomBelow, int roomAbove) {
        stairsUp.put(roomBelow, roomAbove);
        stairsUp.put(roomAbove, roomBelow);
    }

    public List<Integer> connectedFrom(int roomIndex) {
        return connections.get(roomIndex);
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append("[->]").append(getEntrances());
        for (int i = 0; i < getRooms().size(); ++i) {
            builder.append(" (" + i + ") ").append(getRooms().get(i));
        }
        return builder.toString();
    }

    public Function<Integer, String> roomFormatter() {
        return roomIndex -> {
            if (roomIndex == null) return "nowhere";
            final Room room = getRooms().get(roomIndex);
            return room == null ? "nowhere" : room.toString();
        };
    }

    public List<Room> getRooms() {
        return rooms;
    }

    /** Correspond to indexes in {@link #rooms} */
    public List<Integer> getEntrances() {
        return entrances;
    }
}
