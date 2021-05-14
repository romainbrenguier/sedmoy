package com.github.romainbrenguier.sedmoy.operation;

import com.github.romainbrenguier.sedmoy.operation.Operation.State;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Stack;
import java.util.stream.Collectors;

/** Operation which applies to a stream of objects rather than a single one */
public interface BatchOperation {

  List<State> apply(List<Operation.State> states);

  static Object transformArrayToList(Object input) {
    if (input.getClass().isArray()) {
      return Arrays.stream((Object[])input).collect(Collectors.toList());
    }
    return input;
  }

  class Objectwise implements BatchOperation {
    private final Operation underlying;

    public Objectwise(Operation underlying) {
      this.underlying = underlying;
    }

    @Override
    public List<State> apply(List<State> states) {
      Stack<Object> stack = states.get(states.size() - 1).stack;
      for (int i = 0; i < states.size(); ++i) {
        Operation.State state = new Operation.State(stack, states.get(i).data);
        try {
          state = underlying.apply(state);
          state.data = transformArrayToList(state.data);
        } catch (Exception e) {
          state.data = e.getMessage();
        }
        stack = state.stack;
        states.set(i, state);
      }
      return states;
    }
  }

  class SortOperation implements BatchOperation {
    private final Comparator<String> comparator;

    public SortOperation(Comparator<String> comparator) {
      this.comparator = comparator;
    }

    @Override
    public List<State> apply(List<State> states) {
      states.sort((state1, state2) -> comparator.compare(
          state1.data.toString(), state2.data.toString()));
      return states;
    }
  }
}
