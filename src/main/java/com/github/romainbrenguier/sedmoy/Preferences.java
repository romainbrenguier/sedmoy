package com.github.romainbrenguier.sedmoy;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

public class Preferences {

  private final Properties properties;

  private static final Path DEFAULT_FILE =
      Paths.get(System.getProperty("user.home"), ".sedmoy.prf");

  private Preferences() {
    this.properties = new Properties();
  }

  public static Preferences load() {
    Preferences preferences = new Preferences();
    try (FileReader reader = new FileReader(DEFAULT_FILE.toFile())) {
      preferences.properties.load(reader);
    } catch (IOException e) {
      // file didn't exist yet, we will create an empty one on exit
    }
    return preferences;
  }

  public void save() {
    try (FileWriter writer = new FileWriter(DEFAULT_FILE.toFile())) {
      properties.store(writer, "Sedmoy preferences");
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}