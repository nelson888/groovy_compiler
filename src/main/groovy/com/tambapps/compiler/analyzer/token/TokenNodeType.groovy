package com.tambapps.compiler.analyzer.token

import java.util.stream.Stream

enum TokenNodeType {
    PLUS_U, MINUS_U, NOT,
    PLUS_B, MINUS_B, MODULO, MULTIPLY, DIVIDE, POWER,
    EQUAL, NOT_EQUAL, STRICT_INF, STRICT_SUP, SUP, INF, AND, OR,
    CONSTANT,
    DROP,ASSIGNMENT,VAR_REF, VAR_DECL,
    PROG, BLOC, SEQ;

    boolean isUnaryOperator() {
        return Stream.of(PLUS_U, MINUS_U, NOT)
                .anyMatch({t -> this == t})
    }

    boolean isBinaryOperator() {
        return Stream.of(PLUS_B, MINUS_B, MODULO, MULTIPLY, DIVIDE, POWER, EQUAL, NOT_EQUAL, STRICT_INF, STRICT_SUP, SUP, INF, AND, OR)
        .anyMatch({t -> this == t})
    }
}
