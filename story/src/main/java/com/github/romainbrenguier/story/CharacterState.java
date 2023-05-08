package com.github.romainbrenguier.story;

import javax.annotation.Nullable;

public class CharacterState {
    @Nullable Integer position;
    boolean fleeing;
    boolean positionKnownFromOthers;
}
