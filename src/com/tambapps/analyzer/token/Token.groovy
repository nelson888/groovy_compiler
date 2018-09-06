package com.tambapps.analyzer.token

class Token extends AbstractToken {

    final TokenType type

    Token(int l, int c, value, TokenType type) {
        super(l, c, value)
        this.type = type
    }

    Token(int l, int c, TokenType type) {
        super(l, c, null)
        this.type = type
    }

    static Token of(TokenType type, int c, int l) {
        return new Token(l, c, type)
    }

    static Token of(TokenType type, def value, int c, int l) {
        return new Token(l, c, value, type)
    }
}