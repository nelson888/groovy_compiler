package com.tambapps.compiler.analyzer.token

import java.util.stream.Stream

enum TokenType {
    IDENTIFIER, CONSTANT,
    NOT('!'),// unary operator
    PLUS('+'), MINUS('-'), // unary or binary operator
    DIVIDE('/'), MULTIPLY('*'), POWER('^'), MODULO('%'), // binary operator
    ASSIGNMENT('='),
    EQUAL('=='), NOT_EQUAL('!='), STRICT_SUP('>'), STRICT_INF('<'), SUP('>='), INF('<='), AND('and'), OR('or'), //binary operator
    PARENT_OPEN('('), PARENT_CLOSE(')'), COMMA(','), SEMICOLON(';'), ACCOLADE_OPEN('{'), ACCOLADE_CLOSE('}'),
    IF('if'), FOR('for'), WHILE('while'),
    END_OF_FILE;

    final String value

    TokenType() {
        this(null)
    }

    TokenType(String value) {
        this.value = value
    }
    boolean isBinaryOperator() {
        return Stream.of(PLUS, MINUS, DIVIDE, MULTIPLY, POWER, MODULO, EQUAL, NOT_EQUAL, STRICT_SUP, STRICT_INF, SUP, INF, AND, OR)
                .anyMatch({t -> this == t})
    }

    boolean isBooleanOperator() {
        return Stream.of(EQUAL, NOT_EQUAL, STRICT_SUP, STRICT_INF, SUP, INF, AND, OR, NOT)
            .anyMatch({t -> this == t})
    }

    boolean isOperator() {
        return Stream.of(PLUS, MINUS, DIVIDE, MULTIPLY, POWER, MODULO, NOT, ASSIGNMENT, EQUAL, NOT_EQUAL, STRICT_SUP, STRICT_INF, SUP, INF)
                .anyMatch({t -> this == t})
    }

    boolean isSymbol() {
        return value?.chars()?.allMatch({c -> ! Character.isLetter(c)})
    }


    boolean isKeyWord() {
        return value?.chars()?.allMatch({c -> Character.isLetter(c)})
    }

    /**
     * returns wether this type can only be a single char symbol
     * e.g: '^' is only a single char symbol
     * but '<' is not because there is the symbol '<=' that contains more than 1 character
     * @return
     */
    boolean isOnlySingleCharSymbol() {
        return isSymbol() && value.length() == 1 &&  !Stream.of(ASSIGNMENT, STRICT_SUP, STRICT_INF, NOT).anyMatch({t -> t == this})
    }

}