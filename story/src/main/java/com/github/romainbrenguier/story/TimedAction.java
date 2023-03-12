package com.github.romainbrenguier.story;

import java.util.Date;
import java.util.Random;

public class TimedAction {
    Date time;
    Action action;

    static Date randomDate(Random r) {
        return new Date(1850, r.nextInt(12), r.nextInt(28));
    }
}
