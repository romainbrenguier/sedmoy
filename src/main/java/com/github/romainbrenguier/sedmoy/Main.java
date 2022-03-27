package com.github.romainbrenguier.sedmoy;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

@Command
public class Main implements Runnable {
  @Option(names = "--path")
  Path path;

  public static void main(String[] args) {
    CommandLine.run(new Main(), args);
  }

  @Override
  public void run() {
    try {
      final Preferences preferences = Preferences.load();
      final List<String> data = Files.readAllLines(path);
      new InteractiveMode(data, preferences).run();
      preferences.save();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
