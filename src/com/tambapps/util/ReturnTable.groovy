package com.tambapps.util
/**
 * function that returns a value (or null if none wanted)
 * for a given transition (current state and next state)
 * @param <T> the return type of the automate
 */
interface ReturnTable<T> {
    T getReturnValue(int currentState, int nextState)
}
