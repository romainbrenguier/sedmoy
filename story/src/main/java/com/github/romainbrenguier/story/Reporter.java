package com.github.romainbrenguier.story;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Reporter {

    private List<TimedAction> mergeActions(List<TimedAction> actions) {
        if (actions.isEmpty()) return actions;
        final ArrayList<TimedAction> result = new ArrayList<>();
        TimedAction newTimedAction = new TimedAction();
        newTimedAction.action = actions.get(0).action;
        newTimedAction.timeStart = actions.get(0).timeStart;
        newTimedAction.timeEnd = actions.get(0).timeEnd;
        for (TimedAction timedAction : actions.stream().skip(1).collect(Collectors.toList())) {
            if (timedAction.action.equals(newTimedAction.action)) {
                newTimedAction.timeEnd = timedAction.timeEnd;
            } else {
                result.add(newTimedAction);
                newTimedAction = new TimedAction();
                newTimedAction.action = timedAction.action;
                newTimedAction.timeStart = timedAction.timeStart;
                newTimedAction.timeEnd = timedAction.timeEnd;
            }
        }
        result.add(newTimedAction);
        return result;
    }

    List<TimedAction> hideCrimeAction(List<TimedAction> timedActions) {
        final ArrayList<TimedAction> result = new ArrayList<>();
        for (int i = 0; i < timedActions.size(); ++i) {
            if (!(timedActions.get(i).action instanceof Action.Kill
                    || (i > 0 && timedActions.get(i - 1).action instanceof Action.Kill)
                    || (i < timedActions.size() - 1 && timedActions.get(i + 1).action instanceof Action.Kill))) {
                result.add(timedActions.get(i));
            }
        }
        return result;
    }

    public Report reportFromPointOfView(Scene scene, Character character) {
        final List<TimedAction> filtered = scene.actions.stream()
                .filter(timedAction -> timedAction.action.actors().contains(character))
                .collect(Collectors.toList());
        return new Report(character, mergeActions(hideCrimeAction(filtered)));
    }

    public String reportAsText(Scene scene, Report report) {
        final Function<Integer, String> roomFormatter = scene.setup.place.roomFormatter();
        return report.actions.stream()
                .map(action -> reportFromPointOfView(action, report.character, roomFormatter))
                .collect(Collectors.joining("\n"));
    }

    String reportFromPointOfView(TimedAction timedAction, Character character, Function<Integer,
         String> roomFormatter) {
        final Action action = timedAction.action;
        if (action instanceof Action.Arrive) {
            return String.format("I arrived at %s in %s.",
                    TimedAction.formatTime(timedAction.timeStart),
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
