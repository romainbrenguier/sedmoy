package com.github.romainbrenguier.story.character;

import javax.annotation.Nullable;

public class CharacterState {
    @Nullable public Integer position;
    public boolean fleeing;
    public boolean positionKnownFromOthers;
}
