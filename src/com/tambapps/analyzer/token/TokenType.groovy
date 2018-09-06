package com.tambapps.analyzer.token

enum TokenType {
    IDENTIFIER, CONSTANT,
    NOT,// unary operator //TODO
    PLUS, MINUS, // unary or binary operator
    DIVIDE, MULTIPLY, POWER, MODULO, // binary operator
    ASSIGNMENT,
    EQUAL, NOT_EQUAL, STRICT_SUP, STRICT_INF, SUP, INF, //binary operator
    PARENT_OPEN, PARENT_CLOSE, COMMA, SEMICOLON, ACCOLADE_OPEN, ACCOLADE_CLOSE,
    AND, OR, IF, FOR, WHILE;

    static final Map<String, TokenType> KEYWORDS_MAP
    static final Map<String, TokenType> SYMBOLS_MAP
    static {
        Map<String, TokenType> keywordsMap = new HashMap<>()
        for (TokenType t : [AND, OR, IF, FOR, WHILE]) {
            keywordsMap.put(t.name().toLowerCase(), t)
        }
        KEYWORDS_MAP = Collections.unmodifiableMap(keywordsMap)

        Map<String, TokenType> symbolsMap = new HashMap<>()
        symbolsMap.put('+', PLUS)
        symbolsMap.put('-', MINUS)
        symbolsMap.put('*', MULTIPLY)
        symbolsMap.put('/', DIVIDE)
        symbolsMap.put('^', POWER)
        symbolsMap.put('%', MODULO)
        symbolsMap.put('=', ASSIGNMENT)
        symbolsMap.put('(', PARENT_OPEN)
        symbolsMap.put(')', PARENT_CLOSE)
        symbolsMap.put('{', ACCOLADE_OPEN)
        symbolsMap.put('}', ACCOLADE_CLOSE)
        symbolsMap.put(',', COMMA)
        symbolsMap.put(';', SEMICOLON)
        symbolsMap.put('==', EQUAL)
        symbolsMap.put('!=', NOT_EQUAL)
        symbolsMap.put('>', STRICT_SUP)
        symbolsMap.put('<', STRICT_INF)
        symbolsMap.put('>=', SUP)
        symbolsMap.put('<=', INF)

        SYMBOLS_MAP = Collections.unmodifiableMap(symbolsMap)
    }
}