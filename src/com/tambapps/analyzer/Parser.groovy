package com.tambapps.analyzer

import com.tambapps.analyzer.token.Token
import com.tambapps.analyzer.token.TokenNode
import com.tambapps.analyzer.token.TokenNodeType
import com.tambapps.analyzer.token.TokenType
import com.tambapps.analyzer.token.TokenUtils

class Parser { //Analyseur syntaxique

    private final List<Token> tokens
    private int currentIndex

    Parser(Token[] tokens) {
        this(Arrays.asList(tokens))
    }

    Parser(List<Token> tokens) {
        this.tokens = tokens
    }

    TokenNode parse() {
        return atome()
    }

    private TokenNode atome() {
        Token t = getCurrent()
        switch (t.type) {
            case TokenType.CONSTANT:
                moveForward()
                return new TokenNode(t)
                break
            case TokenType.PLUS:
            case TokenType.MINUS:
            case TokenType.NOT:
                moveForward()
                TokenNode node = atome()
                return new TokenNode(TokenUtils.UNARY_OPERATOR_MAP.get(t.type), t, [node])
        }

        throw new RuntimeException("Token type $t is not handled")
    }

    private void moveForward() {
        currentIndex++
    }

    private Token getCurrent() {
        if (currentIndex >= tokens.size()) {
            throw new IndexOutOfBoundsException("Couldn't access token $currentIndex")
        }
        return tokens.get(currentIndex)
    }
}
