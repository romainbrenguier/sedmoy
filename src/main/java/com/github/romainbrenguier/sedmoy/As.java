package com.github.romainbrenguier.sedmoy;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/** Type conversion utilities */
public class As {

  public static LocalTime localTime(DateTimeFormatter formatter, String timeString) {
    try {
      return LocalTime.parse(timeString, formatter);
    } catch (DateTimeParseException ignored){
      System.err.println("Parsing exception " + ignored);
      return null;
    }
  }
}
