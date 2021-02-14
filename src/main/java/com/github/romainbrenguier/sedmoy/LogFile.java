package com.github.romainbrenguier.sedmoy;

import java.time.Duration;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class LogFile implements RowTransform {
  final static String DEFAULT_FORMAT = "%9dms";
  final static String LN = "\n";
  final static String DEFAULT_PATTERN = "HH:mm:ss.SSS";

  private LocalTime lastSeenTime = null;

  public Row transform(Row row) {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DEFAULT_PATTERN);
    String timePart = row.column(0);
    LocalTime newTime = As.localTime(formatter, timePart);
    row.setColumn(0, diffString(lastSeenTime, newTime));
    lastSeenTime = newTime;
    return row;
  }

  String diffString(@Nullable LocalTime previous, @Nullable LocalTime time) {
    if (time == null || previous == null) {
      return "NA";
    }
    Duration diff = Duration.between(previous, time);
    long milliSeconds = diff.toMillis();
    return String.format(DEFAULT_FORMAT, milliSeconds);
  }
}
