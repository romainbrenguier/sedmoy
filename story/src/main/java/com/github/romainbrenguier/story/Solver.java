package com.github.romainbrenguier.story;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class Solver {
    Calendar timeOfCrime;
    Character victime;

    Map<Character, Calendar> lastTimeSeenBeforeCrime = new HashMap<>();

    boolean deduceTimeOfCrime(List<TimedAction> actions) {
        final Optional<Calendar> time = actions.stream()
                .filter(timedAction -> timedAction.action instanceof Action.Kill)
                .map(timedAction -> timedAction.timeStart)
                .findFirst();
        if (time.isEmpty()) return false;
        timeOfCrime = time.get();
        return true;
    }

    boolean deduceVictime(SceneState endState) {
        if (endState.killed.size() != 1) return false;
        // TODO generalize
        victime = endState.killed.get(0);
        return true;
    }

    void markSeen(Character c, Calendar atTime) {
        final Calendar lastSeen = lastTimeSeenBeforeCrime.get(c);
        if (atTime.before(timeOfCrime) && atTime.after(lastSeen))
            lastTimeSeenBeforeCrime.put(c, atTime);
    }

    void analyzeReport(Report report) {
        report.actions.stream()
                .filter(timedAction -> timedAction.action instanceof Action.Talk)
                .forEach(timedAction -> {
                    final Action.Talk talk = (Action.Talk) timedAction.action;
                    talk.talking.stream()
                            .filter(c -> !c.equals(report.character))
                            .forEach(c -> {
                                markSeen(c, timedAction.timeStart);
                                if (timedAction.timeEnd != null)
                                    markSeen(c, timedAction.timeEnd);
                            });
                });
    }

    @Override
    public String toString() {
        return "Solver{" +
                "timeOfCrime=" + TimedAction.formatTime(timeOfCrime) +
                ", victime=" + victime +
                ", lastTimeSeenBeforeCrime=" + lastTimeSeenBeforeCrime +
                '}';
    }
}
