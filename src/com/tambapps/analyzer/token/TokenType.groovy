package com.tambapps.analyzer.token

enum TokenType {
    IDENTIFIER, CONSTANT,
    NOT,// unary operator //TODO
    PLUS, MINUS, // unary or binary operator
    DIVIDE, MULTIPLY, POWER, MODULO, // binary operator
    ASSIGNMENT,
    EQUAL, NOT_EQUAL, STRICT_SUP, STRICT_INF, SUP, INF, //binary operator
    PARENT_OPEN, PARENT_CLOSE, COMMA, SEMICOLON, ACCOLADE_OPEN, ACCOLADE_CLOSE,
    AND, OR, IF, FOR, WHILE
}