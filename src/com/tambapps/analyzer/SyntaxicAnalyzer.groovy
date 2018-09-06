package com.tambapps.analyzer

import com.tambapps.analyzer.token.Token
import com.tambapps.analyzer.token.TokenNode
import com.tambapps.analyzer.token.TokenNodeType
import com.tambapps.analyzer.token.TokenType

class SyntaxicAnalyzer {

    private final List<Token> tokens
    private int currentIndex

    SyntaxicAnalyzer(Token[] tokens) {
        this(Arrays.asList(tokens))
    }

    SyntaxicAnalyzer(List<Token> tokens) {
        this.tokens = tokens
    }

    TokenNode atome() {
        Token t = tokens.get(currentIndex)
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
                return new TokenNode(TokenNodeType.UNARY_OPERATOR_MAP.get(t.type), t, [node])
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
