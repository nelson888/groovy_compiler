package com.tambapps.util

class Automate<T> {

    TransitionTable transitionTable
    ReturnTable<T> returnTable
    int currentState

    Automate(TransitionTable transitionTable, ReturnTable<T> returnTable) {
        this.transitionTable = transitionTable
        this.returnTable = returnTable
        this.currentState = 0 // initial state has to be 0
    }

    T act(char c) {
        int nextState = transitionTable.getNextState(currentState, c)
        T value = returnTable.getReturnValue(currentState, nextState)

        currentState = nextState
        if (value != null) {
            return value
        }
        return null
    }

}
