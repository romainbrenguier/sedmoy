package com.github.romainbrenguier.sedmoy;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Stack;
import java.util.stream.Collectors;

/**
 * Wrapper for a list of operations.
 */
public class Operations {
  private final List<Operation> operationList = new ArrayList<>();

  public void removeLast() {
      operationList.remove(operationList.size() - 1);
  }

  public void add(MethodOperation operation) {
      operationList.add(operation);
  }

  private static Object transformArrayToList(Object input) {
    if (input.getClass().isArray()) {
      return Arrays.stream((Object[])input).collect(Collectors.toList());
    }
    return input;
  }

  public List<Object> apply(List<Object> inputData) {
    final Stack<Object> stack = new Stack<>();
    for (final Operation operation : operationList) {
      List<Object> newResult = new ArrayList<>();
      for (Object input : inputData) {
        try {
          newResult.add(transformArrayToList(
              operation.apply(stack, input)));
        } catch (Exception e) {
          newResult.add(e.getMessage());
        }
      }
      inputData = newResult;
    }
    return inputData;
  }
}
