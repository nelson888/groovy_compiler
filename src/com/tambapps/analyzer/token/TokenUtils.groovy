package com.tambapps.analyzer.token

class TokenUtils {

    static final int LEFT = 0
    static final int RIGHT = 1
    //TokenType
    static final Map<String, TokenType> KEYWORDS_MAP
    static final Map<String, TokenType> SYMBOLS_MAP

    //TokenNodeType
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
        UNARY_OPERATOR_MAP = Collections.unmodifiableMap(unaryMap)

        Map<TokenType, TokenNodeType> binaryMap = new HashMap<>()
        binaryMap.put(TokenType.PLUS, TokenNodeType.PLUS_B)
        binaryMap.put(TokenType.MINUS, TokenNodeType.MINUS_B)
        binaryMap.put(TokenType.MODULO, TokenNodeType.MODULO)
        binaryMap.put(TokenType.MULTIPLY, TokenNodeType.MULTIPLY)
        binaryMap.put(TokenType.DIVIDE, TokenNodeType.DIVIDE)
        binaryMap.put(TokenType.POWER, TokenNodeType.POWER)
        BINARY_OPERATOR_MAP = Collections.unmodifiableMap(binaryMap)

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

        Map<TokenType, Integer> priorityMap = new HashMap<>()
        priorityMap.put(TokenType.POWER, 1)
        priorityMap.put(TokenType.MULTIPLY, 2)
        priorityMap.put(TokenType.DIVIDE, 2)
        priorityMap.put(TokenType.MODULO, 2)
        priorityMap.put(TokenType.PLUS, 3)
        priorityMap.put(TokenType.MINUS, 3)
        PRIORITY_MAP = Collections.unmodifiableMap(priorityMap)

        Map<TokenType, Integer> associativityMap = new HashMap<>()
        associativityMap.put(TokenType.POWER, RIGHT)
        associativityMap.put(TokenType.MULTIPLY, LEFT)
        associativityMap.put(TokenType.DIVIDE, LEFT)
        associativityMap.put(TokenType.MODULO, LEFT)
        associativityMap.put(TokenType.PLUS, LEFT)
        associativityMap.put(TokenType.MINUS, LEFT)
        ASSOCIATIVITY_MAP = Collections.unmodifiableMap(associativityMap)
    }
}
