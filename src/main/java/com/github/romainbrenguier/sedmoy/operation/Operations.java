package com.github.romainbrenguier.sedmoy.operation;

import com.github.romainbrenguier.sedmoy.operation.Operation.State;
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

  public void add(Operation operation) {
      operationList.add(operation);
  }

  private static Object transformArrayToList(Object input) {
    if (input.getClass().isArray()) {
      return Arrays.stream((Object[])input).collect(Collectors.toList());
    }
    return input;
  }

  public List<Operation.State> apply(List<Object> inputData) {
    Stack<Object> stack = new Stack<>();
    List<Operation.State> newResult = new ArrayList<>();
    for (Object input : inputData) {
      Operation.State state = new Operation.State(stack, input);
      try {
        for (final Operation operation : operationList) {
          state = operation.apply(state);
          state.data = transformArrayToList(state.data);
        }
      } catch (Exception e) {
        state.data = e.getMessage();
      }
      newResult.add(new State((Stack<Object>) state.stack.clone(), state.data));
    }
    return newResult;
  }
}
