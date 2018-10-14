package com.tambapps.compiler.analyzer

import com.tambapps.compiler.analyzer.token.TokenType
import com.tambapps.compiler.analyzer.token.TokenUtils
import com.tambapps.compiler.exception.ParsingException
import com.tambapps.compiler.analyzer.token.Token
import com.tambapps.compiler.analyzer.token.TokenNode

import static com.tambapps.compiler.analyzer.token.TokenUtils.ASSOCIATIVITY_MAP
import static com.tambapps.compiler.analyzer.token.TokenUtils.BINARY_OPERATOR_MAP
import static com.tambapps.compiler.analyzer.token.TokenUtils.PRIORITY_MAP

class Parser { //Syntax analyzer

    private def tokens //can be a list or an array of tokens
    private int currentIndex = 0

    /**
     *
     * @param tokens list or array of tokens
     * @return the Token tree
     */
    TokenNode parse(tokens) {
        this.tokens = tokens
        TokenNode node =  expression()
        if (currentIndex < tokens.size() - 1) {
            Token t = getCurrent()
            if (t.type != TokenType.END_OF_FILE) {
                throw new ParsingException("Syntax error at the end of the file", t.l, t.c)
            }
        }
        return node
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
                    throw new ParsingException("Parenthesis should be close", node.l, node.c)
                }
                moveForward()
                return node
        }

        throw new ParsingException("Unexpected token $t.type encountered", t.l, t.c)
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
            Token last = tokens[-1]
            throw new ParsingException("Unexpected end of file", last.l, last.c)
        }
        return tokens[currentIndex]
    }

    void reset() {
        tokens = null
        currentIndex = 0
    }

}
