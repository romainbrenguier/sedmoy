package com.github.romainbrenguier.sedmoy;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class Main {
  public static void main(String[] args) {
    Path path = Paths.get(args[0]);
    MainConfig config = new MainConfig().withPath(path);
    run(config);
  }

  public static void run(MainConfig config) {
    assert config.isInteractive() : "only interactive mode is supported";
    try {
      final List<String> data = Files.readAllLines(config.path);
      new InteractiveMode(data).run();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
