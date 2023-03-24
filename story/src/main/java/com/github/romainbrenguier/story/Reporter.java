package com.github.romainbrenguier.story;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
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

    public String reportFromPointOfView(Scene scene, Character character) {
        final List<TimedAction> filtered = scene.actions.stream()
                .filter(timedAction -> timedAction.action.actors().contains(character))
                .collect(Collectors.toList());
        final List<TimedAction> mergedActions = mergeActions(filtered);
        return mergedActions.stream()
                .map(action -> action.format(roomIndex -> scene.setup.place.rooms.get(roomIndex).toString()))
                .collect(Collectors.joining("\n"));
    }
}
