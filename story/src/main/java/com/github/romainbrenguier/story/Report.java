package com.github.romainbrenguier.story;

import java.util.List;

public class Report {
    final Character character;
    List<TimedAction> actions;

    public Report(Character character, List<TimedAction> actions) {
        this.character = character;
        this.actions = actions;
    }
}
