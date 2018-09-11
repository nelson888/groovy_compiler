package com.tambapps.analyzer.token

import java.util.stream.Stream

enum TokenNodeType {
    PLUS_U, MINUS_U, NOT,
    PLUS_B, MINUS_B, MODULO, MULTIPLY, DIVIDE, POWER,
    CONSTANT, IDENTIFIER;

    boolean isUnaryOperator() {
        return Stream.of(PLUS_U, MINUS_U, NOT)
                .anyMatch({t -> this == t})
    }

    boolean isBinaryOperator() {
        return Stream.of(PLUS_B, MINUS_B, MODULO, MULTIPLY, DIVIDE, POWER)
        .anyMatch({t -> this == t})
    }
}
