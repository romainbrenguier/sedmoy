package com.github.romainbrenguier.story.places;

public class Theatre {

    enum RoomType {
        Lobby, // The entrance to the theater, featuring a box office, concessions stand, and
        // seating area for patrons waiting to enter.
        Auditorium, // The main performance space, featuring a stage, seating for the audience,
        // and lighting and sound equipment.
        Stage, //  The main platform where performers act, dance, or sing, featuring props, sets,
        // and backdrops.
        DressingRooms, // Private areas where performers prepare for their performances, featuring makeup mirrors, costume racks, and chairs.
        RehearsalRoom, // A separate space used for practicing and rehearsing performances,
        // featuring mirrors, barres, and a piano.
        Workshop, // A separate area where sets, props, and costumes are constructed, featuring
        // workbenches, tools, and sewing machines.
        StorageRoom, // A space where extra sets, props, and costumes are stored when not in use,
        // featuring shelves and racks for organization.
        Restrooms, // Separate facilities for male and female patrons, performers, and staff.
        Staircases; // Staircases leading to the upper levels of the theater, including the
        // balcony and lighting catwalk.
    }
}
