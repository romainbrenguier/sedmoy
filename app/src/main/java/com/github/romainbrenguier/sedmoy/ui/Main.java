package com.github.romainbrenguier.sedmoy.ui;

import com.github.romainbrenguier.sedmoy.app.CachedFileReader;
import com.github.romainbrenguier.sedmoy.app.GroovyInterpreter;
import com.github.romainbrenguier.sedmoy.app.TableEvaluator;
import com.github.romainbrenguier.sedmoy.model.CsvParser;
import com.github.romainbrenguier.sedmoy.model.DataTable;
import com.github.romainbrenguier.sedmoy.model.Dimension;
import com.github.romainbrenguier.sedmoy.model.Document;
import com.github.romainbrenguier.sedmoy.model.FormulaTable;
import com.github.romainbrenguier.sedmoy.app.GroovyException;
import com.github.romainbrenguier.sedmoy.app.HtmlToMobi;
import com.github.romainbrenguier.sedmoy.app.TableToHtml;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.MalformedInputException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.stream.Collectors;

import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

@Command
public class Main implements Runnable {

    private static final String DEFAULT_CONTENT =
            "Welcome to Sedmoy. This is a dummy table with 1 cell.";

    @Option(names = {"--input", "-i"})
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

    @Option(names = {"--gui"})
    boolean useGraphicalInterface;

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
                groovyInterpreter.setCurrentDirectory(Paths.get(System.getProperty("user.dir")));
                groovyInterpreter.setCachedFileReader(new CachedFileReader());
                System.out.println("Reading from file: " + groovyScript);
                final String script = String.join("\n", Files.readAllLines(groovyScript));
                groovyInterpreter.setScript(script);
                groovyInterpreter.run();
            } catch (MalformedInputException e) {
                System.out.println("Failure reading the file. Before running the script, ensure the\n"
                        + "input file is using utf-8 encoding. On linux use `file -i <filname>` to find the\n"
                        + "current encoding and:\n"
                        + "`iconv -f <current encoding> -t UTF-8 <filename> -o <outpute_file>` to convert it");
                e.printStackTrace();
            } catch (GroovyException | IOException e) {
                e.printStackTrace();
            }
        }
        Document document;
        try {
            if (input == null) {
                document = new Document();
                final DataTable table = new DataTable(new Dimension(1, 1));
                table.set(0, 0, DEFAULT_CONTENT);
                document.add(tableName, table);
            } else if (input.toString().endsWith("csv")) {
                System.out.println("Convert csv to sedmoy document");
                final DataTable table = new CsvParser(separator).parseLines(Files.readAllLines(input));
                document = new Document();
                document.add(tableName, table);
            } else if (input.toString().endsWith("sdm")) {
                System.out.println("Read sedmoy document");
                document = Document.ofJson(new FileInputStream(input.toFile()));
            } else {
                document = new Document();
            }
        } catch (IOException exception) {
            System.out.println("Failed to read input: " + input);
            exception.printStackTrace();
            document = new Document();
        }

        if (useGraphicalInterface) {
            new GraphicalInterface(document).run();
            return;
        }
        OutputStream out = System.out;
        if (output != null) {
            try {
                out = new FileOutputStream(output.toFile());
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        try {
            document.toJson(out);
        } catch (IOException exception) {
            exception.printStackTrace();
        }
        System.out.println();
        if (addFormula != null) {
            final FormulaTable formulaTable =
                    new FormulaTable(new Dimension(numberOfLines, numberOfColumns), addFormula);
            document.add(tableName, formulaTable);
        }
        if (output == null) {
            System.out.println(document.toString());
            System.out.println("Evaluate");
            final TableEvaluator evaluator = new TableEvaluator(new GroovyInterpreter());
            System.out.println(evaluator.evaluate(document).toString());
        } else if (output.toString().endsWith("csv")) {
            final Document result = new TableEvaluator(new GroovyInterpreter()).evaluate(document);
            System.out.println("Write file " + output);
            try {
                Files.write(output, Collections.singleton(result.toString()));
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        } else if (output.toString().endsWith("sdm")) {
            System.out.println("Write file " + output);
            try {
                document.toJson(new FileOutputStream(output.toFile()));
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        } else {
            System.out.println("No action known for output type:" + output);
        }
    }
}
