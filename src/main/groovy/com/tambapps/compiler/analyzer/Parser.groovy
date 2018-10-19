package com.tambapps.compiler.analyzer

import com.tambapps.compiler.analyzer.token.TokenNodeType
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
        TokenNode P = new TokenNode(TokenNodeType.PROG, new Token(0, 0, null, null), [])
        while (currentIndex < tokens.size() - 1) {
            TokenNode n = statement()
            P.addChild(n)
        }
        return P
    }

    private TokenNode atome() {
        Token t = getCurrent()
        moveForward()
        switch (t.type) {
            case TokenType.CONSTANT:
                return new TokenNode(t)
            case TokenType.IDENTIFIER:
                return new TokenNode(t, TokenNodeType.VAR_REF, new VarInfo(t.value))
            case TokenType.PLUS:
            case TokenType.MINUS:
            case TokenType.NOT:
                TokenNode node = atome()
                return new TokenNode(TokenUtils.UNARY_OPERATOR_MAP.get(t.type), t, [node])
            case TokenType.PARENT_OPEN:
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

    private TokenNode statement() {
        Token t = getCurrent()
        switch (t.type) {
            case TokenType.VAR:
                accept(TokenType.VAR)
                Token tokIdent = accept(TokenType.IDENTIFIER)
                if (getCurrent().type == TokenType.SEMICOLON) {
                    accept(TokenType.SEMICOLON)
                    return new TokenNode(tokIdent, TokenNodeType.VAR_DECL, new VarInfo(tokIdent.value))
                } else if (getCurrent().type == TokenType.ASSIGNMENT) { //var ident = expr;
                    Token assignToken = accept(TokenType.ASSIGNMENT)
                    TokenNode seq  = new TokenNode(assignToken, TokenNodeType.SEQ)
                    TokenNode declTok = new TokenNode(tokIdent, TokenNodeType.VAR_DECL, new VarInfo(tokIdent.value))
                    TokenNode value = expression()
                    TokenNode assignTok = new TokenNode(assignToken, TokenNodeType.ASSIGNMENT, null)
                    assignTok.addChildren(new TokenNode(tokIdent, TokenNodeType.VAR_REF, new VarInfo(tokIdent.value)), value)
                    seq.addChildren(declTok, assignTok)
                    accept(TokenType.SEMICOLON)
                    return seq
                }
            throw new ParsingException("Expected token $TokenType.SEMICOLON or $TokenType.ASSIGNMENT", tokIdent.l, tokIdent.c)

            case TokenType.IF: // if (test) S
                TokenNode N = new TokenNode(accept(TokenType.IF))
                accept(TokenType.PARENT_OPEN)
                TokenNode test = expression()
                accept(TokenType.PARENT_CLOSE)
                TokenNode S = statement()
                N.addChild(test)
                N.addChild(S)
                if (getCurrent().type == TokenType.ELSE) {
                    accept(TokenType.ELSE)
                    N.addChild(statement())
                }
                return N
            case TokenType.ACCOLADE_OPEN:
                TokenNode N = new TokenNode(accept(TokenType.ACCOLADE_OPEN))
                while (getCurrent().type != TokenType.ACCOLADE_CLOSE) {
                    N.addChild(statement())
                }
                accept(TokenType.ACCOLADE_CLOSE)
                return N
            case TokenType.WHILE: //while (E) S
                TokenNode N = new TokenNode(accept(TokenType.WHILE)) //noeud loop
                TokenNode cond = new TokenNode(TokenNodeType.COND, N.l, N.c)
                N.addChild(cond)
                accept(TokenType.PARENT_OPEN)
                TokenNode test = expression()
                accept(TokenType.PARENT_CLOSE)
                TokenNode S = statement()
                cond.addChildren(test, S)
                return N
            case TokenType.FOR: //for (init;test;step) S
                TokenNode N = new TokenNode(accept(TokenType.FOR)) //noeud seq
                TokenNode loop = new TokenNode(TokenNodeType.LOOP, N.l, N.c)
                accept(TokenType.PARENT_OPEN)
                TokenNode init = expression()
                accept(TokenType.SEMICOLON)
                TokenNode test = expression()
                accept(TokenType.SEMICOLON)
                TokenNode step = expression()
                accept(TokenType.PARENT_CLOSE)
                TokenNode body = statement()

                TokenNode cond = new TokenNode(TokenNodeType.COND, loop.l, loop.c)
                TokenNode seq = new TokenNode(TokenNodeType.SEQ, step.l, step.c)
                TokenNode breakNode = new TokenNode(TokenNodeType.BREAK, step.l, step.c)

                N.addChildren(init, loop)
                loop.addChild(cond)
                cond.addChildren(test, seq, breakNode)
                seq.addChildren(body, step)
                return N
            case TokenType.PRINT:
                TokenNode print = new TokenNode(accept(TokenType.PRINT))
                TokenNode e = expression()
                print.addChild(e)
                return new TokenNode(TokenNodeType.DROP, accept(TokenType.SEMICOLON), [print])
            default: // expression;
                TokenNode e = expression()
                return new TokenNode(TokenNodeType.DROP, accept(TokenType.SEMICOLON), [e])
        }
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

    Token accept(TokenType t) {
        Token token = getCurrent()
        if (token.type != t) {
            throw new ParsingException("Expected token of type $t", token.l, token.c)
        }
        moveForward()
        return token
    }

    static class VarInfo {
        String name
        Integer index

        VarInfo(def value) {
            this.name = value
            index = null
        }

        @Override
        String toString() {
            return "{" +
                "name=" + name +
                ", index=" + index +
                '}'
        }
    }
}
