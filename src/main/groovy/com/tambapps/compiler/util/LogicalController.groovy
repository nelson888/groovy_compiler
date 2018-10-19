package com.tambapps.compiler.util

class LogicalController<T> {

  private TransitionTable transitionTable
  private ReturnTable<T> returnTable
  private int currentState

  LogicalController(TransitionTable transitionTable, ReturnTable<T> returnTable) {
    this.transitionTable = transitionTable
    this.returnTable = returnTable
    this.currentState = 0 // initial state has to be 0
  }

  T act(char c) {
    int nextState = transitionTable.getNextState(currentState, c)
    T value = returnTable.getReturnValue(currentState, nextState)

    currentState = nextState

    return value
  }

  int getState() {
    return currentState
  }

  void setState(int state) {
    this.currentState = state
  }

}
