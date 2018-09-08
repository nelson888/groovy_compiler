package com.tambapps.analyzer

import static com.tambapps.analyzer.token.TokenUtils.ASSOCIATIVITY_MAP
import static com.tambapps.analyzer.token.TokenUtils.BINARY_OPERATOR_MAP
import static com.tambapps.analyzer.token.TokenUtils.PRIORITY_MAP

import com.tambapps.analyzer.token.Token
import com.tambapps.analyzer.token.TokenNode
import com.tambapps.analyzer.token.TokenType
import com.tambapps.analyzer.token.TokenUtils
import com.tambapps.exception.ParentheseException

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
        return expression()
    }

    private TokenNode atome() {
        Token t = getCurrent()
        switch (t.type) {
            case TokenType.CONSTANT:
                moveForward()
                return new TokenNode(t)
            case TokenType.PLUS:
            case TokenType.MINUS:
            case TokenType.NOT:
                moveForward()
                TokenNode node = atome()
                return new TokenNode(TokenUtils.UNARY_OPERATOR_MAP.get(t.type), t, [node])
            case TokenType.PARENT_OPEN:
                moveForward()
                TokenNode node = expression()
                if (getCurrent().type != TokenType.PARENT_CLOSE) {
                    throw new ParentheseException("Parenthese should be close") //TODO display l and c
                }
                moveForward()
                return node
        }

        throw new RuntimeException("Token type $t is not handled or ERROR")
    }

    private TokenNode expression() {
        return expression(Integer.MAX_VALUE)
    }

    private TokenNode expression(int maxP) {
        TokenNode A = atome()
        Token T = getCurrent()
        while (T.type.isBinaryOperator() &&  PRIORITY_MAP.get(T.type) < maxP) {
            moveForward()
            TokenNode N = new TokenNode(T, BINARY_OPERATOR_MAP.get(T.type))
            N.addChild(A)
            N.addChild(expression(PRIORITY_MAP.get(T.type) + ASSOCIATIVITY_MAP.get(T.type)))
            A = N
            T = getCurrent()
        }
        return A
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