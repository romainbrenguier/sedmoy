package com.github.romainbrenguier.sedmoy.operation;

import com.github.romainbrenguier.sedmoy.operation.BatchOperation.Objectwise;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.stream.Collectors;

/**
 * Wrapper for a list of operations.
 */
public class Operations {
  private final List<BatchOperation> operationList = new ArrayList<>();

  public void removeLast() {
      operationList.remove(operationList.size() - 1);
  }

  public void add(Operation operation) {
      operationList.add(new Objectwise(operation));
  }

  public List<Operation.State> apply(List<Object> inputData) {
    List<Operation.State> states = inputData.stream()
        .map(data -> new Operation.State(new Stack<>(), data))
        .collect(Collectors.toList());
    for (final BatchOperation operation : operationList) {
      states = operation.apply(states);
    }
    return states;
  }
}
