package com.github.romainbrenguier.story;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Random;

public class Main {
    public static void main(String[] args) {
        final Random random = new Random();
        final Scene scene = Scene.make(random, 240, 4);
//        System.out.println("Setup: " + scene.setup);

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
        solver.deduceTimeOfCrime(scene.actions);
        scene.setup.characters.stream()
                .filter(c -> !scene.endState.killed.contains(c))
                .map(c -> reporter.reportFromPointOfView(scene, c))
                .forEach(solver::analyzeReport);
        System.out.println("Analysis:" + solver);

        System.out.println("Solution:");
        scene.actions.stream()
                .filter(timedAction -> timedAction.action instanceof Action.Kill)
                .map(timedAction -> timedAction.format(scene.setup.place.roomFormatter()))
                .forEach(System.out::println);
    }
}
