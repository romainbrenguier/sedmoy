package com.github.romainbrenguier.story.places;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class Mansion {

    Map<RoomType, Integer> roomIndex = new HashMap<>();

    void addConnectionIfPresent(Place in, RoomType from, RoomType to) {
        Integer index1 = roomIndex.get(from);
        Integer index2 = roomIndex.get(to);
        if (index1 != null && index2 != null) {
            in.connect(index1, index2);
        }
    }

    public Place make(Random r) {
        Place mansion = new Place(PlaceType.Mansion);
        for (int i = 0; i < RoomType.values().length; ++i) {
            RoomType roomType = RoomType.values()[i];
//            if (r.nextBoolean()) {
                roomIndex.put(roomType,
                        mansion.addRoom(new Room(roomType)));
//            }
        }
        addConnectionIfPresent(mansion, RoomType.Foyer, RoomType.LivingRoom);
        addConnectionIfPresent(mansion, RoomType.Foyer, RoomType.DiningRoom);
        addConnectionIfPresent(mansion, RoomType.Foyer, RoomType.Study);
        mansion.connectByStairs(roomIndex.get(RoomType.Foyer), roomIndex.get(RoomType.UpperFloor));
        addConnectionIfPresent(mansion, RoomType.Foyer, RoomType.Ballroom);
        addConnectionIfPresent(mansion, RoomType.LivingRoom, RoomType.DrawingRoom);
        addConnectionIfPresent(mansion, RoomType.LivingRoom, RoomType.Hallway);
        addConnectionIfPresent(mansion, RoomType.DiningRoom, RoomType.Kitchen);
        addConnectionIfPresent(mansion, RoomType.DiningRoom, RoomType.ButlersPantry);
        addConnectionIfPresent(mansion, RoomType.Study, RoomType.Library);
        addConnectionIfPresent(mansion, RoomType.Library, RoomType.DrawingRoom);
        addConnectionIfPresent(mansion, RoomType.DrawingRoom, RoomType.Conservatory);
        addConnectionIfPresent(mansion, RoomType.UpperFloor, RoomType.Bedroom);
        addConnectionIfPresent(mansion, RoomType.Kitchen, RoomType.ButlersPantry);
        addConnectionIfPresent(mansion, RoomType.Kitchen, RoomType.Hallway);
        addConnectionIfPresent(mansion, RoomType.Kitchen, RoomType.WineCellar);
        addConnectionIfPresent(mansion, RoomType.Kitchen, RoomType.ServantsQuarters);
        return mansion;
    }
}
