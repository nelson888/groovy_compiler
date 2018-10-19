package com.tambapps.compiler.analyzer.token

class TokenUtils {

  static final int LEFT = 0
  static final int RIGHT = 1

  static final Map<String, TokenType> KEYWORDS_MAP
  static final Map<String, TokenType> SYMBOLS_MAP

  static final Map<TokenType, TokenNodeType> UNARY_OPERATOR_MAP
  static final Map<TokenType, TokenNodeType> BINARY_OPERATOR_MAP
  static final Map<TokenType, TokenNodeType> TYPE_MAP
  static final Map<TokenType, Integer> PRIORITY_MAP
  static final Map<TokenType, Integer> ASSOCIATIVITY_MAP

  static {
    Map<TokenType, TokenNodeType> unaryMap = new HashMap<>()
    unaryMap.put(TokenType.PLUS, TokenNodeType.PLUS_U)
    unaryMap.put(TokenType.MINUS, TokenNodeType.MINUS_U)
    unaryMap.put(TokenType.NOT, TokenNodeType.NOT)

    Map<TokenType, TokenNodeType> binaryMap = new HashMap<>()
    binaryMap.put(TokenType.PLUS, TokenNodeType.PLUS_B)
    binaryMap.put(TokenType.MINUS, TokenNodeType.MINUS_B)
    binaryMap.put(TokenType.MODULO, TokenNodeType.MODULO)
    binaryMap.put(TokenType.MULTIPLY, TokenNodeType.MULTIPLY)
    binaryMap.put(TokenType.DIVIDE, TokenNodeType.DIVIDE)
    binaryMap.put(TokenType.POWER, TokenNodeType.POWER)
    binaryMap.put(TokenType.EQUAL, TokenNodeType.EQUAL)
    binaryMap.put(TokenType.NOT_EQUAL, TokenNodeType.NOT_EQUAL)
    binaryMap.put(TokenType.STRICT_SUP, TokenNodeType.STRICT_SUP)
    binaryMap.put(TokenType.STRICT_INF, TokenNodeType.STRICT_INF)
    binaryMap.put(TokenType.SUP, TokenNodeType.SUP)
    binaryMap.put(TokenType.INF, TokenNodeType.INF)
    binaryMap.put(TokenType.AND, TokenNodeType.AND)
    binaryMap.put(TokenType.OR, TokenNodeType.OR)
    binaryMap.put(TokenType.ASSIGNMENT, TokenNodeType.ASSIGNMENT)

    Map<TokenType, TokenNodeType> typeMap = new HashMap<>()
    typeMap.put(TokenType.CONSTANT, TokenNodeType.CONSTANT)
    typeMap.put(TokenType.IDENTIFIER, TokenNodeType.VAR_REF)
    typeMap.put(TokenType.IF, TokenNodeType.COND)
    typeMap.put(TokenType.ACCOLADE_OPEN, TokenNodeType.BLOC)
    typeMap.put(TokenType.WHILE, TokenNodeType.LOOP)
    typeMap.put(TokenType.FOR, TokenNodeType.SEQ)
    typeMap.put(TokenType.PRINT, TokenNodeType.PRINT)

    Map<TokenType, Integer> priorityMap = new HashMap<>()
    priorityMap.put(TokenType.POWER, 1)
    priorityMap.put(TokenType.NOT, 1)
    priorityMap.put(TokenType.MULTIPLY, 2)
    priorityMap.put(TokenType.DIVIDE, 2)
    priorityMap.put(TokenType.MODULO, 2)
    priorityMap.put(TokenType.PLUS, 3)
    priorityMap.put(TokenType.MINUS, 3)
    priorityMap.put(TokenType.AND, 4)
    priorityMap.put(TokenType.EQUAL, 5)
    priorityMap.put(TokenType.NOT_EQUAL, 5)
    priorityMap.put(TokenType.STRICT_SUP, 5)
    priorityMap.put(TokenType.STRICT_INF, 5)
    priorityMap.put(TokenType.SUP, 5)
    priorityMap.put(TokenType.INF, 5)
    priorityMap.put(TokenType.OR, 5)
    priorityMap.put(TokenType.ASSIGNMENT, 6)

    //filled in the loop
    Map<TokenType, Integer> associativityMap = new HashMap<>()
    Map<String, TokenType> keywordsMap = new HashMap<>()
    Map<String, TokenType> symbolsMap = new HashMap<>()

    for (TokenType t : TokenType.values()) {
      if (t.isSymbol()) {
        symbolsMap.put(t.value, t)
      } else if (t.isKeyWord()) {
        keywordsMap.put(t.value, t)
      }
      if (t.isBinaryOperator()) {
        associativityMap.put(t, t == TokenType.POWER || t == TokenType.ASSIGNMENT ? RIGHT : LEFT)
      }
    }

    UNARY_OPERATOR_MAP = Collections.unmodifiableMap(unaryMap)
    BINARY_OPERATOR_MAP = Collections.unmodifiableMap(binaryMap)
    TYPE_MAP = Collections.unmodifiableMap(typeMap)
    KEYWORDS_MAP = Collections.unmodifiableMap(keywordsMap)
    SYMBOLS_MAP = Collections.unmodifiableMap(symbolsMap)
    PRIORITY_MAP = Collections.unmodifiableMap(priorityMap)
    ASSOCIATIVITY_MAP = Collections.unmodifiableMap(associativityMap)
  }
}
