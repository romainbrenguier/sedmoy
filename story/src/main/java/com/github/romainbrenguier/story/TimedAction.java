package com.github.romainbrenguier.story;

import javax.annotation.Nullable;
import java.util.Calendar;
import java.util.Random;
import java.util.function.Function;

public class TimedAction {
    Calendar timeStart;
    // null means 1 minute later or unknown
    @Nullable
    private Calendar timeEnd;
    Action action;

    public static Calendar randomDate(Random r) {
        final Calendar calendar = Calendar.getInstance();
        calendar.set(1850, r.nextInt(12), r.nextInt(28));
        return calendar;
    }

    public Calendar getEndTime() {
        return timeEnd != null ? timeEnd : timeStart;
    }

    public void setEndTime(Calendar time) {
        timeEnd = time;
    }

    public static String formatTime(Calendar calendar) {
        final int startHour = calendar.get(Calendar.HOUR);
        final int startMinute = calendar.get(Calendar.MINUTE);
        return String.format("%d:%02d", startHour, startMinute);
    }

    @Override
    public String toString() {
        Calendar endTime = timeEnd != null ? timeEnd : timeStart;
        return String.format("From %s to %s, %s", formatTime(timeStart),
                formatTime(endTime), action);
    }

    public String format(Function<Integer, String> roomFormatter) {
        if (timeEnd == null) {
            return String.format("At %s, %s", formatTime(timeStart), action.format(roomFormatter));
        }
        return String.format("From %s to %s, %s",
                formatTime(timeStart), formatTime(timeEnd), action.format(roomFormatter));

    }
}
