package com.github.romainbrenguier.story;

import com.github.romainbrenguier.story.places.Room;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;
import java.util.function.Function;

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
        return String.format("At %d:%02d, %s", time.get(Calendar.HOUR),
                time.get(Calendar.MINUTE), action);
    }

    public String format(Function<Integer, String> roomFormatter) {
        return String.format("At %d:%02d, %s", time.get(Calendar.HOUR),
                time.get(Calendar.MINUTE), action.format(roomFormatter));
    }
}
