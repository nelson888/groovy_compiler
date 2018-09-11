package com.tambapps.util
/**
 * Map the current state and given entry to the next state
 */
interface TransitionTable {
    int getNextState(int currentState, char entry)
}