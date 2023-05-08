package com.github.romainbrenguier.story;

import com.github.romainbrenguier.story.character.Character;
import com.github.romainbrenguier.story.places.RoomPlacer;
import com.github.romainbrenguier.story.scene.Action;
import com.github.romainbrenguier.story.scene.Reporter;
import com.github.romainbrenguier.story.scene.Scene;
import com.github.romainbrenguier.story.solver.Solver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Random;

public class Main {
    public static void main(String[] args) {
        final long seed = args.length > 0
                ? Long.parseLong(args[0])
                : System.nanoTime() ^ 783497276652981L;
        System.out.println("Seed: " + seed);
        final Random random = new Random(seed);

        final Scene scene = Scene.make(random, 240, 5);
        System.out.println("Crime scene:\n" + RoomPlacer.makeMap(scene.getSetup().getPlace()));

        System.out.println(String.format("%s was killed\n", scene.getEndState().killed));

        Reporter reporter = new Reporter();
        for (Character c : scene.getSetup().getCharacters()) {
            if (!scene.getEndState().killed.contains(c)) {
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
        solver.deduceVictime(scene.getEndState());
        solver.deducePlaceOfCrime(scene.getEndState());
        solver.deduceTimeOfCrime(scene.getActions());
        scene.getSetup().getCharacters().stream()
                .filter(c -> !scene.getEndState().killed.contains(c))
                .map(c -> reporter.reportFromPointOfView(scene, c))
                .forEach(solver::analyzeReport);
        System.out.println("GraphAnalysis:" + solver.report(
                scene.getSetup().getPlace().roomFormatter()));

        System.out.println("Solution:");
        scene.getActions().stream()
                .filter(timedAction -> timedAction.getAction() instanceof Action.Kill)
                .map(timedAction -> timedAction.format(scene.getSetup().getPlace().roomFormatter()))
                .forEach(System.out::println);
    }
}
