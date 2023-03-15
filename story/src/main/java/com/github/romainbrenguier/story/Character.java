package com.github.romainbrenguier.story;

import java.util.Arrays;
import java.util.Random;

public class Character {
    Personality personality;
    Status status;
    int age;
    boolean isWoman;

    Name name;

    public static Character random(Random r) {
        final Character character = new Character();
        character.personality = Personality.random(r);
        character.status = Status.random(r);
        character.age = (int) (Math.round((r.nextGaussian() + 1.0) * 40.0));
        if (character.age < 0) character.age = -character.age;
        character.isWoman = r.nextBoolean();
        character.name = Name.make(
                r,
                character.isWoman, Arrays.asList(Status.Aristocrat, Status.Merchant,
                        Status.Doctor, Status.Politician, Status.Priest).contains(
                        character.status));
        return character;
    }

    @Override
    public String toString() {
        return name.toString();
    }

    public String details() {
        final StringBuilder builder = new StringBuilder();
        if (age < 18) {
            if (isWoman) builder.append("girl");
            else builder.append("boy");
        } else {
            if (isWoman) builder.append("woman");
            else builder.append("man");
        }
        builder.append(" aged ").append(age);
        builder.append(", ").append(status);
        builder.append(", ").append(personality);
        return builder.toString();
    }
}
