package com.github.romainbrenguier.story;

import java.util.Random;

public class Main {
    public static void main(String[] args) {
        final Random random = new Random();
        final Scene scene = Scene.make(random, 240);
        System.out.println("Setup: " + scene.setup);

        Reporter reporter = new Reporter();
        for (Character c : scene.setup.characters) {
            System.out.println("Report from: " + c);
            System.out.println(reporter.reportFromPointOfView(scene, c));
            System.out.println("\n");
        }
    }
}
