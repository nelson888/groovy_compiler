package com.tambapps.analyzer

enum TokenType {
    IDENTIFIER, CONSTANT, PLUS, MINUS;
    static Map<String, TokenType> TOKEN_MAP = new HashMap<>();
    static {
        TOKEN_MAP.put('+', PLUS);
        TOKEN_MAP.put('-', MINUS);
    }
}