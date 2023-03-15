package com.github.romainbrenguier.story;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

public class TimedAction {
    Calendar time;
    Action action;

    public static Calendar randomDate(Random r) {
        final Calendar calendar = Calendar.getInstance();
        calendar.set(1850, r.nextInt(12), r.nextInt(28));
        return calendar;
    }

    @Override
    public String toString() {
        return "At " + time.get(Calendar.HOUR) + " " + time.get(Calendar.MINUTE) + ", " + action;
    }
}
