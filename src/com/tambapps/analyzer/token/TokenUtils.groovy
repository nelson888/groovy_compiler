package com.tambapps.analyzer.token

class TokenUtils {

    //TokenType
    static final Map<String, TokenType> KEYWORDS_MAP
    static final Map<String, TokenType> SYMBOLS_MAP

    //TokenNodeType
    static final Map<TokenType, TokenNodeType> UNARY_OPERATOR_MAP
    static final Map<TokenType, TokenNodeType> TYPE_MAP

    static {
        Map<TokenType, TokenNodeType> unaryMap = new HashMap<>()
        unaryMap.put(TokenType.PLUS, TokenNodeType.PLUS_U)
        unaryMap.put(TokenType.MINUS, TokenNodeType.MINUS_U)
        unaryMap.put(TokenType.NOT, TokenNodeType.NOT)
        UNARY_OPERATOR_MAP = Collections.unmodifiableMap(unaryMap)

        Map<TokenType, TokenNodeType> typeMap = new HashMap<>()
        typeMap.put(TokenType.CONSTANT, TokenNodeType.CONSTANT)
        typeMap.put(TokenType.IDENTIFIER, TokenNodeType.IDENTIFIER)
        TYPE_MAP = Collections.unmodifiableMap(typeMap)


        Map<String, TokenType> keywordsMap = new HashMap<>()
        for (TokenType t : [TokenType.AND, TokenType.OR, TokenType.IF, TokenType.FOR, TokenType.WHILE]) {
            keywordsMap.put(t.name().toLowerCase(), t)
        }
        KEYWORDS_MAP = Collections.unmodifiableMap(keywordsMap)

        Map<String, TokenType> symbolsMap = new HashMap<>()
        symbolsMap.put('+', TokenType.PLUS)
        symbolsMap.put('-', TokenType.MINUS)
        symbolsMap.put('*', TokenType.MULTIPLY)
        symbolsMap.put('/', TokenType.DIVIDE)
        symbolsMap.put('^', TokenType.POWER)
        symbolsMap.put('%', TokenType.MODULO)
        symbolsMap.put('=', TokenType.ASSIGNMENT)
        symbolsMap.put('(', TokenType.PARENT_OPEN)
        symbolsMap.put(')', TokenType.PARENT_CLOSE)
        symbolsMap.put('{', TokenType.ACCOLADE_OPEN)
        symbolsMap.put('}', TokenType.ACCOLADE_CLOSE)
        symbolsMap.put(',', TokenType.COMMA)
        symbolsMap.put(';', TokenType.SEMICOLON)
        symbolsMap.put('==', TokenType.EQUAL)
        symbolsMap.put('!=', TokenType.NOT_EQUAL)
        symbolsMap.put('>', TokenType.STRICT_SUP)
        symbolsMap.put('<', TokenType.STRICT_INF)
        symbolsMap.put('>=', TokenType.SUP)
        symbolsMap.put('<=', TokenType.INF)

        SYMBOLS_MAP = Collections.unmodifiableMap(symbolsMap)
    }
}
