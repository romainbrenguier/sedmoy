package com.github.romainbrenguier.story.scene;

import com.github.romainbrenguier.story.character.Character;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Reporter {

    private List<TimedAction> mergeActions(List<TimedAction> actions) {
        if (actions.isEmpty()) return actions;
        final ArrayList<TimedAction> result = new ArrayList<>();
        TimedAction newTimedAction = new TimedAction();
        newTimedAction.setAction(actions.get(0).getAction());
        newTimedAction.setTimeStart(actions.get(0).getTimeStart());
        newTimedAction.setEndTime(actions.get(0).getEndTime());
        for (TimedAction timedAction : actions.stream().skip(1).collect(Collectors.toList())) {
            if (timedAction.getAction().equals(newTimedAction.getAction())) {
                newTimedAction.setEndTime(timedAction.getEndTime());
            } else {
                result.add(newTimedAction);
                newTimedAction = new TimedAction();
                newTimedAction.setAction(timedAction.getAction());
                newTimedAction.setTimeStart(timedAction.getTimeStart());
                newTimedAction.setEndTime(timedAction.getEndTime());
            }
        }
        result.add(newTimedAction);
        return result;
    }

    List<TimedAction> hideCrimeAction(Character victim, List<TimedAction> timedActions) {
        Calendar lastSeenBeforeCrime = lastSeenBeforeCrime(timedActions, victim);
        Calendar firstSeenAfterCrime = firstSeenAfterCrime(timedActions);
        if (lastSeenBeforeCrime == null && firstSeenAfterCrime == null) {
            return timedActions;
        }

        final ArrayList<TimedAction> result = new ArrayList<>();
        for (int i = 0; i < timedActions.size(); ++i) {
            final TimedAction timedAction = timedActions.get(i);
            if (!(lastSeenBeforeCrime.before(timedAction.getTimeStart()) &&
                    (firstSeenAfterCrime == null || firstSeenAfterCrime.after(timedAction.getEndTime())))) {
                result.add(timedAction);
            }
        }
        return result;
    }

    private static Calendar lastSeenBeforeCrime(List<TimedAction> timedActions, Character victim) {
        Calendar lastSeenBeforeCrime = null;
        for (TimedAction timedAction : timedActions) {
            if (timedAction.getAction() instanceof Action.Talk &&
                    (!((Action.Talk)timedAction.getAction()).talking.contains(victim)
                            || ((Action.Talk)timedAction.getAction()).talking.size() > 2))
                lastSeenBeforeCrime = timedAction.getEndTime();
            if (timedAction.getAction() instanceof Action.Kill)
                return lastSeenBeforeCrime;
        }
        return null;
    }

    private static Calendar firstSeenAfterCrime(List<TimedAction> timedActions) {
        boolean crimePassed = false;
        for (TimedAction timedAction : timedActions) {
            if (crimePassed && timedAction.getAction() instanceof Action.Talk)
                return timedAction.getTimeStart();
            if (timedAction.getAction() instanceof Action.Kill)
                crimePassed = true;
        }
        return null;
    }

    public Report reportFromPointOfView(Scene scene, Character character) {
        final List<TimedAction> filtered = scene.getActions().stream()
                .filter(timedAction -> timedAction.getAction().actors().contains(character))
                .collect(Collectors.toList());
        // TODO assumes one victim
        return new Report(character, mergeActions(hideCrimeAction(scene.getEndState().killed.get(0),
                filtered)));
    }

    public String reportAsText(Scene scene, Report report) {
        final Function<Integer, String> roomFormatter = scene.getSetup().getPlace().roomFormatter();
        final List<List<TimedAction>> actionGroups = groupSimilarActions(report.actions);
        return actionGroups.stream()
                .map(actionGroup ->
                        reportOnGroupedActions(actionGroup, report.character, roomFormatter))
                .collect(Collectors.joining("\n"));
    }

    List<List<TimedAction>> groupSimilarActions(List<TimedAction> timedActions) {
        final List<List<TimedAction>> result = new ArrayList<>();
        List<TimedAction> currentList = new ArrayList<>();
        currentList.add(timedActions.get(0));
        Class<? extends Action> lastClass = timedActions.get(0).getAction().getClass();
        for (int i = 1; i < timedActions.size(); ++i) {
            if (timedActions.get(i).getAction().getClass().equals(lastClass)) {
                currentList.add(timedActions.get(i));
            } else {
                result.add(currentList);
                currentList = new ArrayList<>();
                currentList.add(timedActions.get(i));
                lastClass = timedActions.get(i).getAction().getClass();
            }
        }
        result.add(currentList);
        return result;
    }

    String reportOnGroupedActions(
            List<TimedAction> timedActions, Character character, Function<Integer, String> roomFormatter) {
        if (timedActions.get(0).getAction() instanceof Action.Move) {
            final Integer endRoom = ((Action.Move) timedActions.get(timedActions.size() - 1).getAction()).toRoom;
            final Set<Integer> roomSet = timedActions.stream()
                    .map(timedAction -> ((Action.Move) timedAction.getAction()).toRoom)
                    .filter(i -> !Objects.equals(i, endRoom))
                    .collect(Collectors.toSet());
            return String.format("I went through %s to %s.",
                    roomSet.stream().map(roomFormatter).collect(Collectors.joining(", ")),
                    roomFormatter.apply(endRoom));
        }
        return timedActions.stream().map(t -> reportFromPointOfView(t, character, roomFormatter))
                .collect(Collectors.joining(" "));
    }

    String reportFromPointOfView(TimedAction timedAction, Character character, Function<Integer,
         String> roomFormatter) {
        final Action action = timedAction.getAction();
        if (action instanceof Action.Arrive) {
            return String.format("I arrived at %s in %s.",
                    TimedAction.formatTime(timedAction.getTimeStart()),
                    roomFormatter.apply(((Action.Arrive) action).inRoom));
        }
        if (action instanceof Action.Move) {
            final Action.Move move = (Action.Move) action;
            return String.format("I went to %s.", roomFormatter.apply(move.toRoom));
        }
        if (action instanceof Action.Talk) {
            final Action.Talk talk = (Action.Talk) action;
            final String others = talk.talking.stream()
                    .filter(c -> !c.equals(character))
                    .map(Character::toString)
                    .collect(Collectors.joining(", "));
            return String.format("I talked with %s.", others);
        }
        if (action instanceof Action.Shout) {
            return "I saw the body and shout.";
        }
        return timedAction.format(roomFormatter);
    }
}
