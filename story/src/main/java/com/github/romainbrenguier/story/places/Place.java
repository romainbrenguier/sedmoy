package com.github.romainbrenguier.story.places;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Place {
    PlaceType type;
    public List<Room> rooms = new ArrayList<>();

    /** Correspond to indexes in {@link #rooms} */
    public List<Integer> entrances = Collections.singletonList(0);
    ListMultimap<Integer, Integer> connections = ArrayListMultimap.create();

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

    public boolean areConnected(int roomIndex1, int roomIndex2) {
        return connections.get(roomIndex1).contains(roomIndex2);
    }

    public List<Integer> connectedFrom(int roomIndex) {
        return connections.get(roomIndex);
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        final Room firstRoom = rooms.get(0);
        builder.append(firstRoom);
        List<Integer> toVisit = connections.get(0);
        while (!toVisit.isEmpty()) {
            builder.append('\n');
            List<Integer> newToVisit = new ArrayList<>();
            toVisit.forEach(i -> {
                builder.append(" " + rooms.get(i));
                newToVisit.addAll(connections.get(i));
            } );
            toVisit = newToVisit;
        }
        return builder.toString();
    }
}
