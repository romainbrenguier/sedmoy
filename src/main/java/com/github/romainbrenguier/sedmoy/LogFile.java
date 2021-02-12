package com.github.romainbrenguier.sedmoy;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class LogFile {
  final static String DEFAULT_FORMAT = "%3d.%02ds %s";
  final static String LN = "\n";

  static class TimedLine {

    final LocalTime time;
    final String content;

    TimedLine(@Nullable LocalTime time, String content) {
      this.time = time;
      this.content = content;
    }

    public String toString() {
      return time.format(DateTimeFormatter.ISO_DATE_TIME) + content;
    }

    public String diffString(@Nullable LocalTime previous) {
      if (time == null) {
        return "NA      " + content;
      }
      Duration diff = Duration.between(previous, time);
      long hundredthOfSeconds = diff.toMillis() / 10;
      return String.format(DEFAULT_FORMAT, hundredthOfSeconds, content);
    }
  }

  List<TimedLine> timedLines = new ArrayList<>();

  private List<String> readAllLines(Path path) {
    try {
      return Files.readAllLines(path);
    } catch (IOException e) {
      return Collections.emptyList();
    }
  }

  public LogFile(Path path, String separator) {
    List<String> lines = readAllLines(path);
    String[] split = lines.get(0).split(separator, 2);
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss.SSS");
    for (String line : lines) {
      String[] lineSplit = line.split(separator, 2);
      String timePart = lineSplit[0];
      String messagePart = lineSplit.length > 1 ? lineSplit[1] : "";
      LocalTime newTime = null;
      try {
        LocalTime.parse(timePart, formatter);
      } catch (DateTimeParseException ignored){
      }
      timedLines.add(new TimedLine(newTime, messagePart));
    }
  }

  public String toString(String format) {
    StringBuilder builder = new StringBuilder();
    LocalTime previousTime = null;
    for (TimedLine line : timedLines) {
      builder.append(line.diffString(previousTime)).append(LN);
      previousTime = line.time;
    }
    return builder.toString();
  }

  public String toString() {
    return toString(DEFAULT_FORMAT);
  }
}
