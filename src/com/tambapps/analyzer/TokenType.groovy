package com.tambapps.analyzer

enum TokenType {
    IDENTIFIER, CONSTANT,
    PLUS, MINUS, DIVIDE, MULTIPLY, POWER, MODULO,
    PARENT_OPEN, PARENT_CLOSE, //TODO
    ACCOLADE_OPEN, ACCOLADE_CLOSE, //TODO
    AND, OR, IF, FOR, WHILE; //TODO

    static final Map<String, TokenType> KEYWORDS_MAP
    static {
        Map<String, TokenType> map = new HashMap<>()
        for (TokenType t : [AND, OR, IF, FOR, WHILE]) {
            map.put(t.name().toLowerCase(), t)
        }

        KEYWORDS_MAP = Collections.unmodifiableMap(map)

    }
}