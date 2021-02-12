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
  final static String DEFAULT_FORMAT = "%9dms %s";
  final static String LN = "\n";

  static class TimedLine {
    final static String DEFAULT_PATTERN = "HH:mm:ss.SSS";
    final LocalTime time;
    final String content;

    TimedLine(@Nullable LocalTime time, String content) {
      this.time = time;
      this.content = content;
    }

    public String toString() {
      return time.format(DateTimeFormatter.ISO_TIME) + " " + content;
    }

    public String diffString(@Nullable LocalTime previous) {
      if (time == null || previous == null) {
        return "NA          " + content;
      }
      Duration diff = Duration.between(previous, time);
      long milliSeconds = diff.toMillis();
      return String.format(DEFAULT_FORMAT, milliSeconds, content);
    }


    public static TimedLine ofString(String line, String separator) {
      DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DEFAULT_PATTERN);
      String[] lineSplit = line.split(separator, 2);
      String timePart = lineSplit[0];
      String messagePart = lineSplit.length > 1 ? lineSplit[1] : "";
      LocalTime newTime = parseLocalTime(formatter, timePart);
      return new TimedLine(newTime, messagePart);
    }

    private static LocalTime parseLocalTime(DateTimeFormatter formatter, String timeString) {
      LocalTime newTime = null;
      try {
        newTime = LocalTime.parse(timeString, formatter);
      } catch (DateTimeParseException ignored){
        System.err.println("Parsing exception " + ignored);
      }
      return newTime;
    }
  }

  List<TimedLine> timedLines = new ArrayList<>();

  private static List<String> readAllLines(Path path) {
    try {
      return Files.readAllLines(path);
    } catch (IOException e) {
      return Collections.emptyList();
    }
  }

  public LogFile(List<String> lines, String separator) {
    lines.forEach(line -> timedLines.add(TimedLine.ofString(line, separator)));
  }

  public static LogFile readFrom(Path path, String separator) {
    return new LogFile(readAllLines(path), separator);
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
