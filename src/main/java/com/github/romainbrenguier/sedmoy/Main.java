package com.github.romainbrenguier.sedmoy;

import com.github.romainbrenguier.sedmoy.GroovyInterpreter.GroovyException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

@Command
public class Main implements Runnable {
  @Option(names = {"--input", "-i"}, required = true)
  Path input;

  @Option(names = {"--groovy-script", "-g"})
  Path groovyScript;

  public static void main(String[] args) {
    CommandLine.run(new Main(), args);
  }

  @Override
  public void run() {
    if (groovyScript != null) {
      try {
        GroovyInterpreter.run(groovyScript.toFile(), input);
      } catch (GroovyException e) {
        e.printStackTrace();
      }
      return;
    }
    try {
      final Preferences preferences = Preferences.load();
      final List<String> data = Files.readAllLines(input);
      new InteractiveMode(data, preferences).run();
      preferences.save();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
