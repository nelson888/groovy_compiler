package com.tambapps.analyzer

import com.tambapps.exception.IllegalTransitionStateException
import com.tambapps.exception.LexicalException
import com.tambapps.exception.UnknownSymbolException
import com.tambapps.util.LogicalController
import com.tambapps.util.ReturnTable
import com.tambapps.util.TransitionTable

class LexicalAnalyzer {

    //logical controller stuff
    private static final int INITIAL_STATE = 0
    private static final int WORD_STATE = 1
    private static final int CONSTANT_STATE = 2
    private static final int SYMBOL_STATE = 3

    private final TransitionTable transitionTable = { int currentState, char entry ->
        nextState(currentState, entry)
    } as TransitionTable

    private final ReturnTable<Token> returnTable = { int currentState, int nextState ->
        returnValue(currentState, nextState)
    } as ReturnTable<Token>

    private static final Character SPACE = ' ' as Character
    private static final Character LINE_BREAK = '\n' as Character

    private final LogicalController<Token> logicalController
    private final StringBuilder valueBuilder = new StringBuilder()
    private int col
    private int lig
    private boolean keepLast = false

    LexicalAnalyzer() {
        logicalController = new LogicalController(transitionTable, returnTable)
        valueBuilder.metaClass.clear { //add method clear() to this instance
            valueBuilder.setLength(0)
        }
    }

    Token[] toTokens(File file) {
        return toTokens(file.getText())
    }

    List<Token> toTokens(String content) {
        List<Token> tokens = new ArrayList<>()
        content = content + LINE_BREAK //add line return to simulate end of file

        for (int i = 0; i < content.size(); i++) {
            char c = content.charAt(i)
            if (!c.isWhitespace() && !(c == LINE_BREAK)) {
                valueBuilder.append(c)
            }
            Token token
            try {
                token = logicalController.act(c)
            } catch (RuntimeException e) {
                throw new LexicalException("Lexical error at l:$lig c:$col\n$e.message", e)
            }

            if (token) {
                tokens.add(token)
                if (keepLast) {
                    valueBuilder.delete(0, valueBuilder.size() - 1)
                } else {
                    valueBuilder.clear()
                }
            }
            if (c == LINE_BREAK) {
                lig++
                col = 0
            } else {
                col++
            }
        }
        if (logicalController.getState() != INITIAL_STATE) {
            throw new LexicalException("Unexpected end of file")
        }
        return tokens
    }

    private Token returnValue(int currentState, int nextState) {
        String value = valueBuilder.toString()
        final int col = this.col - value.size()

        switch (currentState) {
            case INITIAL_STATE:
                return null
            case WORD_STATE:
                TokenType t
                if (nextState == SYMBOL_STATE) {
                    t = TokenType.KEYWORDS_MAP.getOrDefault(value.substring(0, value.size() - 1), TokenType.IDENTIFIER)
                    keepLast = true
                } else {
                    t = TokenType.KEYWORDS_MAP.getOrDefault(value, TokenType.IDENTIFIER)
                }

                if (nextState != currentState) {
                    return t == TokenType.IDENTIFIER ? Token.of(t, value, col, lig) : Token.of(t, col, lig)
                }
                return null
            case CONSTANT_STATE:
                return nextState == INITIAL_STATE ? Token.of(TokenType.CONSTANT, Integer.parseInt(value), col, lig) : null
            case SYMBOL_STATE:
                TokenType t
                if (nextState == WORD_STATE) {
                    t = TokenType.SYMBOLS_MAP.getOrDefault(value.substring(0, value.size() - 1), null)
                    keepLast = true
                } else {
                    t = TokenType.SYMBOLS_MAP.getOrDefault(value, null)
                }

                if (t == null) {
                    throw new UnknownSymbolException("Couldn't resolve symbol: $value")
                }
                return Token.of(t, col, lig)
        }
        return null
    }

    private static boolean isInvisibleChar(char c) {
        return c == SPACE || c == LINE_BREAK
    }

    private static int nextState(int currentState, Character entry) {
        switch (currentState) {
            case INITIAL_STATE:
                if (entry.isDigit()) {
                    return CONSTANT_STATE
                } else if (entry.isLetter()) {
                    return WORD_STATE
                } else if (isInvisibleChar(entry)) {
                    return INITIAL_STATE
                } else {
                    return SYMBOL_STATE
                }
                break

            case WORD_STATE:
                if (entry.isDigit() || entry.isLetter()) {
                    return WORD_STATE
                } else if (isInvisibleChar(entry)) {
                    return INITIAL_STATE
                } else {
                    return SYMBOL_STATE
                }
            case CONSTANT_STATE:
                if (entry.isDigit()) {
                    return CONSTANT_STATE
                } else if (entry == SPACE || entry == LINE_BREAK) {
                    return INITIAL_STATE
                } else {
                    throw new IllegalTransitionStateException("Syntax error")
                }
                break
            case SYMBOL_STATE:
                if (isInvisibleChar(entry)) {
                    return INITIAL_STATE
                } else if (entry.isLetter()) {
                    return WORD_STATE
                } else {
                    return SYMBOL_STATE
                }
        }

        throw new IllegalTransitionStateException("Illegal character '$entry' encountered")
    }

}
