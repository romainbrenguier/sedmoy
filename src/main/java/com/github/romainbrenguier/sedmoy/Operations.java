package com.github.romainbrenguier.sedmoy;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Wrapper for a list of operations.
 */
public class Operations {
  private List<Operation> operationList = new ArrayList<>();

  public void removeLast() {
      operationList.remove(operationList.size() - 1);
  }

  public void add(Operation operation) {
      operationList.add(operation);
  }


  private static Object transformArrayToList(Object input) {
    if (input.getClass().isArray()) {
      return Arrays.stream((Object[])input).collect(Collectors.toList());
    }
    return input;
  }

  public List<Object> apply(List<Object> inputData) {
    for (Operation value : operationList) {
      System.out.println("operation " + value.method + " of class"
          + value.method.getDeclaringClass());
      List<Object> newResult = new ArrayList<>();
      for (Object input : inputData) {
        try {
          newResult.add(transformArrayToList(value.apply(input)));
        } catch (Exception e) {
          newResult.add(e.getMessage());
        }
      }
      inputData = newResult;
    }
    return inputData;
  }
}
