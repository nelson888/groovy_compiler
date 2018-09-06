package com.tambapps.analyzer.token

enum TokenNodeType {
    PLUS_U, MINUS_U, NOT, CONSTANT, IDENTIFIER;
    static final Map<TokenType, TokenNodeType> UNARY_OPERATOR_MAP
    static final Map<TokenType, TokenNodeType> TYPE_MAP

    static {
        Map<TokenType, TokenNodeType> unaryMap = new HashMap<>()
        unaryMap.put(TokenType.PLUS, PLUS_U)
        unaryMap.put(TokenType.MINUS, MINUS_U)
        unaryMap.put(TokenType.NOT, NOT)
        UNARY_OPERATOR_MAP = Collections.unmodifiableMap(unaryMap)

        Map<TokenType, TokenNodeType> typeMap = new HashMap<>()
        typeMap.put(TokenType.CONSTANT, CONSTANT)
        typeMap.put(TokenType.IDENTIFIER, IDENTIFIER)
        TYPE_MAP = Collections.unmodifiableMap(typeMap)
    }
}
