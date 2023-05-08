package com.github.romainbrenguier.story.scene;

import com.github.romainbrenguier.story.character.Character;
import com.github.romainbrenguier.story.scene.TimedAction;

import java.util.List;

public class Report {
    public final Character character;
    public final List<TimedAction> actions;

    public Report(Character character, List<TimedAction> actions) {
        this.character = character;
        this.actions = actions;
    }
}
