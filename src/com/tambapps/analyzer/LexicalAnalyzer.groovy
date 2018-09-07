package com.tambapps.analyzer

import com.tambapps.analyzer.token.Token
import com.tambapps.analyzer.token.TokenType
import com.tambapps.analyzer.token.TokenUtils
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
    private static final Character EQUAL = '=' as Character
    private static final Character[] SINGLE_CHAR_SYMBOLS =  ['(', ')', '+', '-', '*', '/', ',', '%', '^', ';', '{', '}', '!'] as Character[]

    private final LogicalController<Token> logicalController
    private final StringBuilder valueBuilder = new StringBuilder()
    private int col
    private int lig
    private boolean keepLast = false
    private Character nextChar

    LexicalAnalyzer() {
        logicalController = new LogicalController(transitionTable, returnTable)
        valueBuilder.metaClass.clear { //add method clear() to this instance
            valueBuilder.setLength(0)
        }
    }

    List<Token> toTokens(File file) {
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

    private static String truncateLast(String s) {
        return s.substring(0, s.length() - 1)
    }

    private Token returnValue(int currentState, int nextState) {
        String value = valueBuilder.toString()
        int col = this.col - value.size()
        keepLast = false

        switch (currentState) {
            case INITIAL_STATE:
                if (nextState == INITIAL_STATE && !isInvisibleChar(nextChar)) {
                    TokenType t = TokenUtils.SYMBOLS_MAP.get(value)
                    if (t == null) {
                        throw new UnknownSymbolException("Couldn't resolve symbol: $value")
                    }
                    return Token.of(t, col, lig)
                } else {
                    return null
                }

            case WORD_STATE:
                String name

                if (nextState == SYMBOL_STATE) {
                    name = truncateLast(value)
                    keepLast = true
                    col++
                } else if (nextState == INITIAL_STATE) {
                    name = value
                } else  {
                    return null
                }

                TokenType t = TokenUtils.KEYWORDS_MAP.getOrDefault(name, TokenType.IDENTIFIER)
                return t == TokenType.IDENTIFIER ? Token.of(t, name, col, lig) : Token.of(t, col, lig)

            case CONSTANT_STATE:
                Integer number
                if (nextState == SYMBOL_STATE) {
                    number = Integer.parseInt(truncateLast(value))
                    keepLast = true
                    col++
                } else if (nextState == INITIAL_STATE) {
                    number = Integer.parseInt(value)
                } else {
                    return null
                }
                return Token.of(TokenType.CONSTANT, number, col, lig)

            case SYMBOL_STATE:
                TokenType t
                if (nextState == WORD_STATE || nextState == CONSTANT_STATE) {
                    String symbol = truncateLast(value)
                    t = TokenUtils.SYMBOLS_MAP.getOrDefault(symbol, null)
                    keepLast = true
                    col++
                } else if (nextState == INITIAL_STATE) {
                    t = TokenUtils.SYMBOLS_MAP.getOrDefault(value, null)
                } else { // nextState == SYMBOL_STATE
                    t = TokenUtils.SYMBOLS_MAP.get(truncateLast(value))
                    keepLast = true
                    col++
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

    private int nextState(int currentState, Character entry) {
        nextChar = entry
        switch (currentState) {
            case INITIAL_STATE:
                if (entry.isDigit()) {
                    return CONSTANT_STATE
                } else if (entry.isLetter()) {
                    return WORD_STATE
                } else if (isInvisibleChar(entry) || entry in SINGLE_CHAR_SYMBOLS) {
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
                } else if (isInvisibleChar(entry)) {
                    return INITIAL_STATE
                } else if (!entry.isLetter()) {
                    return SYMBOL_STATE
                } else {
                    throw new IllegalTransitionStateException("Cannot have a character  next to a number encountered")

                }
                break
            case SYMBOL_STATE:
                if (isInvisibleChar(entry) || entry == EQUAL) {
                    return INITIAL_STATE
                } else if (entry.isLetter()) {
                    return WORD_STATE
                } else if (entry.isDigit()) {
                    return CONSTANT_STATE
                } else if (isInvisibleChar(entry)) {
                    return INITIAL_STATE
                } else {
                    return SYMBOL_STATE
                }
        }

        throw new IllegalTransitionStateException("Illegal character '$entry' encountered")
    }

    void reset() {
        lig = 0
        col = 0
        valueBuilder.clear()
        keepLast = false
        logicalController.setState(INITIAL_STATE)
    }
}
