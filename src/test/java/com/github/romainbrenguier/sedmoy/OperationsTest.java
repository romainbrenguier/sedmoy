package com.github.romainbrenguier.sedmoy;

import static org.junit.jupiter.api.Assertions.*;

import com.github.romainbrenguier.sedmoy.operation.ConstantParameter;
import com.github.romainbrenguier.sedmoy.operation.MethodOperation;
import com.github.romainbrenguier.sedmoy.operation.Operation.State;
import com.github.romainbrenguier.sedmoy.operation.Operations;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.Test;

public class OperationsTest {

  /**
   * Test the scenario:
   * <pre>
   * split ";"
   * get 1
   * </pre>
   */
  @Test
  public void testApply() throws NoSuchMethodException {
    // Arrange
    Operations operations = new Operations();
    Method split = String.class.getDeclaredMethod("split", String.class);
    operations.add(new MethodOperation(
        split, Collections.singletonList(new ConstantParameter(";"))));
    Method get = ArrayList.class.getDeclaredMethod("get", int.class);
    operations.add(new MethodOperation(
        get, Collections.singletonList(new ConstantParameter(1))));
    List<Object> input = Arrays.asList("foo;bar", "baz;wiz");

    // Act
    List<State> result = operations.apply(input);

    // Assert
    assertEquals(2, result.size());
    assertEquals("bar", result.get(0).data);
    assertEquals("wiz", result.get(1).data);
  }
}