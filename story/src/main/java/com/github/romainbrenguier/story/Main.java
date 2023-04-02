package com.github.romainbrenguier.story;

import com.github.romainbrenguier.story.places.RoomPlacer;

import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Map;
import java.util.Random;

public class Main {
    public static void main(String[] args) {
        final long seed = args.length > 0
                ? Long.parseLong(args[0])
                : System.nanoTime() ^ 783497276652981L;
        System.out.println("Seed: " + seed);
        final Random random = new Random(seed);

        final Scene scene = Scene.make(random, 240, 5);
        System.out.println("Crime scene:\n" + RoomPlacer.makeMap(scene.setup.place));

        System.out.println(String.format("%s was killed\n", scene.endState.killed));

        Reporter reporter = new Reporter();
        for (Character c : scene.setup.characters) {
            if (!scene.endState.killed.contains(c)) {
                System.out.println("Report from: " + c);
                System.out.println(
                        reporter.reportAsText(scene, reporter.reportFromPointOfView(scene, c)));
                System.out.println("\n");
            }
        }

        System.out.println("Who is guilty? Press enter to reveal.");

        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        try {
            String ignored = reader.readLine();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        final Solver solver = new Solver();
        solver.deduceVictime(scene.endState);
        solver.deducePlaceOfCrime(scene.endState);
        solver.deduceTimeOfCrime(scene.actions);
        scene.setup.characters.stream()
                .filter(c -> !scene.endState.killed.contains(c))
                .map(c -> reporter.reportFromPointOfView(scene, c))
                .forEach(solver::analyzeReport);
        System.out.println("Analysis:" + solver.report(
                scene.setup.place.roomFormatter()));

        System.out.println("Solution:");
        scene.actions.stream()
                .filter(timedAction -> timedAction.action instanceof Action.Kill)
                .map(timedAction -> timedAction.format(scene.setup.place.roomFormatter()))
                .forEach(System.out::println);
    }
}
