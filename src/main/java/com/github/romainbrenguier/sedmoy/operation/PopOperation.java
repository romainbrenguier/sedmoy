package com.github.romainbrenguier.sedmoy.operation;

public class PopOperation implements Operation {
  /** Starts from the top at 0 */
  private final int position;

  public PopOperation(int position) {
    this.position = position;
  }

  @Override
  public State apply(State s) {
    s.data = s.stackAtPosition(position);
    return s;
  }
}
