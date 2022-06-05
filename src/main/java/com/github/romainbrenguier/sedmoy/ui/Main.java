package com.github.romainbrenguier.sedmoy.ui;

import com.github.romainbrenguier.sedmoy.app.GroovyException;
import com.github.romainbrenguier.sedmoy.app.GroovyInterpreter;
import com.github.romainbrenguier.sedmoy.app.HtmlToMobi;
import com.github.romainbrenguier.sedmoy.app.TableEvaluator;
import com.github.romainbrenguier.sedmoy.app.TableToHtml;
import com.github.romainbrenguier.sedmoy.model.DataTable;
import com.github.romainbrenguier.sedmoy.model.CsvParser;
import com.github.romainbrenguier.sedmoy.model.Dimension;
import com.github.romainbrenguier.sedmoy.model.Document;
import com.github.romainbrenguier.sedmoy.model.FormulaTable;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.MalformedInputException;
import java.nio.file.Files;
import java.nio.file.Path;

import java.util.Collections;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

@Command
public class Main implements Runnable {

  @Option(names = {"--input", "-i"}, required = true)
  Path input;

  @Option(names = {"--output", "-o"})
  Path output;

  @Option(names = {"--table-name", "-n"}, defaultValue = "table")
  String tableName;

  @Option(names = {"--add-formula"})
  String addFormula;

  @Option(names = {"--number-of-columns", "-c"}, defaultValue = "1")
  Integer numberOfColumns;

  @Option(names = {"--number-of-lines", "-l"}, defaultValue = "1")
  Integer numberOfLines;

  @Option(names = {"--separator", "-s"})
  String separator = CsvParser.DEFAULT_SEPARATOR;

  @Option(names = {"--groovy-script", "-g"})
  Path groovyScript;

  public static void main(String[] args) {
    CommandLine.run(new Main(), args);
  }

  @Override
  public void run() {
    if (groovyScript != null) {
      try {
        final DataTable table = new CsvParser(separator).parseLines(Files.readAllLines(input));
        final GroovyInterpreter groovyInterpreter = new GroovyInterpreter();
        groovyInterpreter.set("input", table);
        groovyInterpreter.set("tableToHtml", new TableToHtml());
        groovyInterpreter.set("htmlToMobi", new HtmlToMobi());
        groovyInterpreter.run(groovyScript.toFile());
      } catch (MalformedInputException e) {
        System.out.println("Failure reading the file. Before running the script, ensure the\n"
            + "input file is using utf-8 encoding. On linux use `file -i <filname>` to find the\n"
            + "current encoding and:\n"
            + "`iconv -f <current encoding> -t UTF-8 <filename> -o <outpute_file>` to convert it");
        e.printStackTrace();
      } catch (GroovyException | IOException e) {
        e.printStackTrace();
      }
    } else if (input.toString().endsWith("csv")) {
      System.out.println("Convert csv to sedmoy document");
      try {
        final DataTable table = new CsvParser(separator).parseLines(Files.readAllLines(input));
        final Document document = new Document();
        document.add(tableName, table);
        OutputStream out;
        if (output != null) {
          out = new FileOutputStream(output.toFile());
        } else {
          out = System.out;
        }
        document.toJson(out);
        System.out.println();
      } catch (IOException exception) {
        System.out.println("Failed to read input: " + input);
        exception.printStackTrace();
      }
    } else if (input.toString().endsWith("sdm")) {
      System.out.println("Read sedmoy document");
      try {
        final Document document = Document.ofJson(new FileInputStream(input.toFile()));
        if (addFormula != null) {
          final FormulaTable formulaTable =
              new FormulaTable(new Dimension(numberOfLines, numberOfColumns), addFormula);
          document.add(tableName, formulaTable);
        }
        if (output == null) {
          System.out.println(document.toString());
        } else if (output.toString().endsWith("csv")) {
          final Document result = new TableEvaluator(new GroovyInterpreter()).evaluate(document);
          System.out.println("Write file " + output);
          Files.write(output, Collections.singleton(result.toString()));
        } else if (output.toString().endsWith("sdm")) {
          System.out.println("Write file " + output);
          document.toJson(new FileOutputStream(output.toFile()));
        } else {
          System.out.println("No action known for output type:" + output);
        }
      } catch (IOException exception) {
        System.out.println("Failed to read input: " + input);
        exception.printStackTrace();
      }
    } else {
      System.out.println("No action found to perform on input " + input.getFileName());
    }
  }
}
