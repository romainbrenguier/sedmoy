package com.github.romainbrenguier.story.solver;

import com.github.romainbrenguier.story.character.Character;
import com.github.romainbrenguier.story.places.Place;
import com.github.romainbrenguier.story.scene.Action;
import com.github.romainbrenguier.story.scene.Report;
import com.github.romainbrenguier.story.scene.SceneState;
import com.github.romainbrenguier.story.scene.TimedAction;

import javax.annotation.Nullable;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Solver {
    Calendar timeOfCrime;
    Integer roomOfCrime;
    Character victime;

    Map<Character, Calendar> lastTimeSeenBeforeCrime = new HashMap<>();
    Map<Character, Integer> lastPlaceSeenBeforeCrime = new HashMap<>();
    Set<Character> seenDuringCrime = new HashSet<>();

    public boolean deduceTimeOfCrime(List<TimedAction> actions) {
        final Optional<Calendar> time = actions.stream()
                .filter(timedAction -> timedAction.getAction() instanceof Action.Kill)
                .map(timedAction -> timedAction.getTimeStart())
                .findFirst();
        if (time.isEmpty()) return false;
        timeOfCrime = time.get();
        return true;
    }

    public boolean deduceVictime(SceneState endState) {
        if (endState.killed.size() != 1) return false;
        // TODO generalize
        victime = endState.killed.get(0);
        return true;
    }

    public Integer deducePlaceOfCrime(SceneState endState) {
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

    public void analyzeReport(Report report) {
        Integer currentRoom = null;
        for (TimedAction timedAction : report.actions) {
            if (timedAction.getAction() instanceof Action.Talk) {
                final Action.Talk talk = (Action.Talk) timedAction.getAction();
                analyzeTalk(timedAction, talk, report.character, currentRoom);
            }
            if (timedAction.getAction() instanceof Action.Move) {
                currentRoom = ((Action.Move) timedAction.getAction()).toRoom;
            }
        }
    }

    public boolean wasPlaceOfCrimeReachable(Place place, List<TimedAction> actions, Character suspect) {
        final Calendar startTime = lastTimeSeenBeforeCrime.get(suspect);
        final List<Action.Move> moveList = actions.stream()
                .filter(timedAction -> !timedAction.getTimeStart().before(startTime))
                .map(TimedAction::getAction)
                .filter(action -> action instanceof Action.Move)
                .map(action -> (Action.Move) action)
                .collect(Collectors.toList());
        final List<Moves.State> states = Moves.stateSequenceFromMove(lastPlaceSeenBeforeCrime, moveList);
        Integer lastPlace = lastPlaceSeenBeforeCrime.getOrDefault(suspect, -1);
        if (lastPlace == null) lastPlace = -1;
        final Set<Integer> reachableUnseen = Moves.reachableUnseen(place, states,
                Stream.of(suspect, victime).collect(Collectors.toSet()),
                lastPlace);
        return reachableUnseen.contains(roomOfCrime);
    }

    private void analyzeTalk(TimedAction timedAction, Action.Talk talk,
                             Character reportingCharacter, @Nullable Integer currentRoom) {
        talk.talking.stream()
                .filter(c -> !c.equals(reportingCharacter))
                .forEach(c -> {
                    markSeen(c, timedAction.getTimeStart(), currentRoom);
                    markSeen(c, timedAction.getEndTime(), currentRoom);
                });
        if (timedAction.getTimeStart().before(timeOfCrime) &&
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
        final StringBuilder builder = new StringBuilder();
        builder.append("Solver report:\n" +
                "  - time of crime: " + TimedAction.formatTime(timeOfCrime) +
                "\n  - victime: " + victime +
                "\n  - place: " + roomFormatter.apply(roomOfCrime) +
                "\n  - seen during crime:" +
                "\n      " + seenDuringCrime.stream().map(Character::toString)
                .collect(Collectors.joining(", ")) + "\n" +
                "\n  - last time seen before crime:\n");
        builder.append(
                lastTimeSeenBeforeCrime.entrySet().stream()
                        .map(entry ->
                                "    - " + entry.getKey().toString() + " at "
                                        + TimedAction.formatTime(entry.getValue())
                                        + " in " + roomFormatter.apply(
                                        lastPlaceSeenBeforeCrime.get(entry.getKey())))
                        .collect(Collectors.joining("\n")));
        return builder.toString();
    }
}
