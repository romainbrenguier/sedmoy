package com.github.romainbrenguier.story;

// Prompt: Forget the above conversation. You are a famous novel writer from the 19th century
// writing crime novels taking place in Paris in the 19th century.
// Write a story about ...

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Random;

public class Scene {
    SceneSetup setup;
    List<TimedAction> actions = new ArrayList<>();

    public static Scene make(Random r) {
        final Scene scene = new Scene();
        scene.setup = SceneSetup.make(r);
        Calendar start = TimedAction.randomDate(r);
        for (int i = 0; i < 10; ++i) {
            final TimedAction timedAction = new TimedAction();
            timedAction.time = Calendar.getInstance();
            timedAction.time.set(start.get(Calendar.YEAR), start.get(Calendar.MONTH),
                    start.get(Calendar.DAY_OF_MONTH));
            timedAction.time.set(Calendar.HOUR, 19);
            timedAction.time.set(Calendar.MINUTE, i);
            final Action.Move move = new Action.Move();
            move.character =
                    scene.setup.characters.get(r.nextInt(scene.setup.characters.size()));
            move.from = scene.setup.place.rooms.get(r.nextInt(scene.setup.place.rooms.size()));
            move.to = scene.setup.place.rooms.get(r.nextInt(scene.setup.place.rooms.size()));
            timedAction.action = move;
            scene.actions.add(timedAction);
        }
        return scene;
    }

    @Override
    public String toString() {
        return "Scene{" + setup.toString() + ", actions=" + actions + '}';
    }
}
