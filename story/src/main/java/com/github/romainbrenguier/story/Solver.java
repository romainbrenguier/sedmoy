package com.github.romainbrenguier.story;

import javax.annotation.Nullable;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Solver {
    Calendar timeOfCrime;
    Integer roomOfCrime;
    Character victime;

    Map<Character, Calendar> lastTimeSeenBeforeCrime = new HashMap<>();
    Map<Character, Integer> lastPlaceSeenBeforeCrime = new HashMap<>();
    Set<Character> seenDuringCrime = new HashSet<>();

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

    Integer deducePlaceOfCrime(SceneState endState) {
        assert victime != null : "must first deduce victime";
        roomOfCrime = endState.getPositionIndex(victime);
        return roomOfCrime;
    }

    void markSeen(Character c, Calendar atTime, @Nullable Integer inRoom) {
        final Calendar lastSeen = lastTimeSeenBeforeCrime.get(c);
        if (atTime.before(timeOfCrime) && (lastSeen == null || atTime.after(lastSeen))) {
            lastTimeSeenBeforeCrime.put(c, atTime);
            lastPlaceSeenBeforeCrime.put(c, inRoom);
        }
    }

    void analyzeReport(Report report) {
        Integer currentRoom = null;
        for (TimedAction timedAction : report.actions) {
            if (timedAction.action instanceof Action.Talk) {
                final Action.Talk talk = (Action.Talk) timedAction.action;
                analyzeTalk(timedAction, talk, report.character, currentRoom);
            }
            if (timedAction.action instanceof Action.Move) {
                currentRoom = ((Action.Move) timedAction.action).toRoom;
            }
        }
    }

    private void analyzeTalk(TimedAction timedAction, Action.Talk talk,
                             Character reportingCharacter, @Nullable Integer currentRoom) {
        talk.talking.stream()
                .filter(c -> !c.equals(reportingCharacter))
                .forEach(c -> {
                    markSeen(c, timedAction.timeStart, currentRoom);
                    markSeen(c, timedAction.getEndTime(), currentRoom);
                });
        if (timedAction.timeStart.before(timeOfCrime) &&
                timedAction.getEndTime().after(timeOfCrime)) {
            talk.talking.stream()
                    .filter(c -> !c.equals(reportingCharacter))
                    .forEach(c -> seenDuringCrime.add(c));
        }
    }

    @Override
    public String toString() {
        return report(Object::toString);
    }

    public String report(Function<Integer, String> roomFormatter) {
        return "Solver report:\n" +
                "  - time of crime: " + TimedAction.formatTime(timeOfCrime) +
                "\n  - victime: " + victime +
                "\n  - place: " + roomFormatter.apply(roomOfCrime) +
                "\n  - seen during crime:" +
                "\n      " + seenDuringCrime.stream().map(Character::toString)
                .collect(Collectors.joining(", ")) + "\n" +
                "\n  - last time seen before crime:\n" +
                lastTimeSeenBeforeCrime.entrySet().stream()
                        .map(entry ->
                                "    - " + entry.getKey().toString() + " at "
                                        + TimedAction.formatTime(entry.getValue())
                                        + " in " + roomFormatter.apply(
                                        lastPlaceSeenBeforeCrime.get(entry.getKey())))
                        .collect(Collectors.joining("\n"));
    }
}