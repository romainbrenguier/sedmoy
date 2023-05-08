package com.github.romainbrenguier.story.character;

import java.util.Random;

public enum Status {
    Aristocrat, //Noble
    Merchant, //Businessman
    Artisan, // /Craftsman
    Peasant, //Farmer/Laborer
    Priest, // Clergy
    Scholar, // Intellectual
    Artist, // Writer/Musician
    Criminal, // Outlaw
    Prostitute, //Courtesan
    Servant, //Domestic Worker
    Soldier, //Warrior
    Politician, //Government Official
    Doctor, //Healer
    Entertainer, //Performer
    Homeless; //Beggar.

    public static Status random(Random random) {
        final int index = random.nextInt(Status.values().length);
        return Status.values()[index];
    }
}
