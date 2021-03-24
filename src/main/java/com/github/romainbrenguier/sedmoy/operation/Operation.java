package com.github.romainbrenguier.sedmoy.operation;

import java.util.Stack;

public interface Operation {

  class State {
    public Stack<Object> stack;
    public Object data;
    public State(Stack<Object> stack, Object data) {
      this.stack = stack;
      this.data = data;
    }

    /** Position starts from the top at 0 */
    public Object stackAtPosition(int position) {
      return stack.elementAt(stack.size() - 1 - position);
    }
  }

  State apply(State s);

}
