package com.github.romainbrenguier.sedmoy;

import com.github.romainbrenguier.sedmoy.GroovyInterpreter.GroovyException;
import com.github.romainbrenguier.sedmoy.csv.Table;
import com.github.romainbrenguier.sedmoy.csv.TableParser;
import java.io.IOException;
import java.nio.charset.MalformedInputException;
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

  @Option(names = {"--separator", "-s"}, required = false)
  String separator = TableParser.DEFAULT_SEPARATOR;

  @Option(names = {"--groovy-script", "-g"})
  Path groovyScript;

  public static void main(String[] args) {
    CommandLine.run(new Main(), args);
  }

  @Override
  public void run() {
    if (groovyScript != null) {
      try {
        final Table table = new TableParser(separator).parseLines(Files.readAllLines(input));
        GroovyInterpreter.run(groovyScript.toFile(), table);
      } catch (MalformedInputException e) {
        System.out.println("Failure reading the file. Before running the script, ensure the\n"
            + "input file is using utf-8 encoding. On linux use `file -i <filname>` to find the\n"
            + "current encoding and:\n"
            + "`iconv -f <current encoding> -t UTF-8 <filename> -o <outpute_file>` to convert it");
        e.printStackTrace();
      } catch (GroovyException | IOException e) {
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
