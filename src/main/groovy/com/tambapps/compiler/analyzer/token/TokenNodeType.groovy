package com.tambapps.compiler.analyzer.token

enum TokenNodeType {
  PLUS_U, MINUS_U, NOT,
  PLUS_B, MINUS_B, MODULO, MULTIPLY, DIVIDE, POWER,
  EQUAL, NOT_EQUAL, STRICT_INF, STRICT_SUP, SUP, INF, AND, OR,
  CONSTANT,
  DROP, ASSIGNMENT, VAR_REF, VAR_DECL,
  COND, BREAK, LOOP,
  PROG, BLOC, SEQ, FUNCTION, FUNCTION_CALL, RETURN,D_REF,
  PRINT,
  TAB_DECL, TAB_REF;

  boolean isUnaryOperator() {
    return this in [PLUS_U, MINUS_U, NOT]
  }

  boolean isBinaryOperator() {
    return this in [PLUS_B, MINUS_B, MODULO, MULTIPLY, DIVIDE, POWER,
                    EQUAL, NOT_EQUAL, STRICT_INF, STRICT_SUP, SUP, INF, AND, OR]
  }
}
