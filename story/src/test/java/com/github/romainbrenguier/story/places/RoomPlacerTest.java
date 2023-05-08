package com.github.romainbrenguier.story.places;

import org.junit.jupiter.api.Test;

import java.awt.*;
import java.util.Map;
import java.util.Random;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.github.romainbrenguier.story.places.RoomPlacer.formatTableMap;
import static com.github.romainbrenguier.story.places.RoomPlacer.placeInTable;
import static com.github.romainbrenguier.story.places.RoomPlacer.placeRooms;

public class RoomPlacerTest {
    @Test
    void testPlaceRooms() {
        final Place place = new Mansion().make(new Random(1));
        final Map<Integer, Point> coordinates = placeRooms(place);
        assert coordinates.containsKey(place.getEntrances().get(0));
        final Integer[][] table = placeInTable(coordinates);
        assert table.length > 0;
        System.out.println(formatTableMap(table, place::connectedFrom, place.roomFormatter(),
                place.getEntrances()));
    }

    @Test
    void testSolveConstraint() {
        final Place place = new Mansion().make(new Random(1));
        final Function<Integer, String> roomFormatter = place.roomFormatter();
//        for (int limit = 1; limit <= place.rooms.size(); ++limit)
        {
//            int limit = 6;
            int limit = place.getRooms().size();
            System.out.println("Placing " + limit + " rooms");
            final Map<Integer, Point> coordinates = RoomPlacer.solveConstraints(place, limit);
            if (coordinates == null) {
                System.out.println("Could not place room " + roomFormatter.apply(limit - 1));
                System.out.println("Connected to " +
                        place.connectedFrom(limit - 1).stream().map(roomFormatter).collect(Collectors.joining(", ")));
                return;
            } else {
                final Integer[][] table = placeInTable(coordinates);
                System.out.println(formatTableMap(table, place::connectedFrom, roomFormatter,
                        place.getEntrances()));
            }
        }
    }
}
