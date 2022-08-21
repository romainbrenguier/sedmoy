package com.github.romainbrenguier.sedmoy.app;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.junit.jupiter.api.Test;

class GroovyInterpreterTest {

  @Test
  void run() throws IOException, GroovyException {
    // Arrange
    final Path path = Paths.get("src/test/resources/Cube.groovy");
    final String script = Files.readString(path);
    final GroovyInterpreter interpreter = new GroovyInterpreter();
    interpreter.setScript(script);

    // Act
    final Object result = interpreter.run();

    // Assert
    assert(result instanceof Integer);
    assert(((Integer)result).intValue() == 10);
  }
}