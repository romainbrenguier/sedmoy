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
    List<Operation.State> states = inputData.stream()
        .map(data -> new Operation.State(new Stack<>(), data))
        .collect(Collectors.toList());
    for (final Operation operation : operationList) {
      for (int i = 0; i < states.size(); ++i) {
        Operation.State state = new Operation.State(stack, states.get(i).data);
        try {
          state = operation.apply(state);
          state.data = transformArrayToList(state.data);
        } catch (Exception e) {
          state.data = e.getMessage();
        }
        stack = state.stack;
        states.set(i, state);
      }
    }
    return states;
  }
}
