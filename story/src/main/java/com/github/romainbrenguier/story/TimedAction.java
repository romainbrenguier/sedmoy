package com.github.romainbrenguier.story;

import com.github.romainbrenguier.story.places.Room;

import javax.annotation.Nullable;
import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;
import java.util.function.Function;

public class TimedAction {
    Calendar timeStart;
    // null means 1 minute later or unknown
    @Nullable
    Calendar timeEnd;
    Action action;

    public static Calendar randomDate(Random r) {
        final Calendar calendar = Calendar.getInstance();
        calendar.set(1850, r.nextInt(12), r.nextInt(28));
        return calendar;
    }

    @Override
    public String toString() {
        final int startHour = timeStart.get(Calendar.HOUR);
        final int startMinute = timeStart.get(Calendar.MINUTE);
        int endHour = timeEnd != null ? timeEnd.get(Calendar.HOUR) : startHour;
        int endMinute = timeEnd != null ? timeEnd.get(Calendar.MINUTE) : startMinute;
        return String.format("From %d:%02d to %d:%02d, %s", startHour,
                startMinute, endHour, endMinute, action);
    }

    public String format(Function<Integer, String> roomFormatter) {
        if (timeEnd == null) {
            return String.format("At %d:%02d, %s", timeStart.get(Calendar.HOUR),
                    timeStart.get(Calendar.MINUTE), action.format(roomFormatter));
        }
        return String.format("From %d:%02d to %d:%02d, %s", timeStart.get(Calendar.HOUR),
                timeStart.get(Calendar.MINUTE), timeEnd.get(Calendar.HOUR),
                timeEnd.get(Calendar.MINUTE), action.format(roomFormatter));

    }
}
