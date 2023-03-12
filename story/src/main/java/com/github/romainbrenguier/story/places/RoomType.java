package com.github.romainbrenguier.story.places;

public enum RoomType {
    Foyer("The entrance to the mansion, often featuring a grand staircase and intricate tilework."),

    LivingRoom("A spacious and elegant room used for entertaining guests,  with a fireplace and " +
            "large windows."),
    DiningRoom("A formal room used for hosting dinner parties and featuring a long table and " +
            "ornate chandelier."),
    Study("A private room used for reading, writing, or conducting business."),
    Library("A larger room filled with books and comfortable seating,             often featuring" +
            " a fireplace and large windows."),
    DrawingRoom("A more intimate room for small gatherings or conversations, often featuring " +
            "comfortable seating and a piano."),
    Conservatory("A glass-enclosed room filled with plants and often used for relaxation or " +
            "meditation."),
    Ballroom("A grand room used for hosting dances and large gatherings, featuring high ceilings " +
            "and ornate decor."),
    Bedroom("Located on the upper floors, these rooms are used for sleeping and often feature " +
            "luxurious furnishings and private bathrooms."),
    Kitchen("Located on the ground floor, this room is used for preparing meals and often " +
            "features modern appliances and plenty of counter space."),
    ButlersPantry("A small room used for storing and preparing serving dishes and silverware."),
    WineCellar("A cool, dark room used for storing and aging wine."),

    ServantsQuarters("Located on the upper floors or in a separate wing, these rooms are used by the" +
            " mansion's staff and often feature basic furnishings and shared bathrooms."),

    SecretPassages("These hidden corridors and tunnels connect various rooms and provide a sense " +
            "of mystery and intrigue to the mansion's layout."),
    Hallway("A room leading to other rooms"),
    UpperFloor("Connected by a staircase from the foyer"),
    Garden("An outdoor garden");

    String description;

    RoomType(String description) {
        this.description = description;
    }
}
