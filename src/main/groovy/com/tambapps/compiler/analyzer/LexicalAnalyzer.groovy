package com.tambapps.compiler.analyzer

import com.tambapps.compiler.analyzer.token.Token
import com.tambapps.compiler.analyzer.token.TokenType
import com.tambapps.compiler.analyzer.token.TokenUtils
import com.tambapps.compiler.exception.LexicalException
import com.tambapps.compiler.util.LogicalController
import com.tambapps.compiler.util.ReturnTable
import com.tambapps.compiler.util.TransitionTable

import java.util.stream.Collectors

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
    private static final Character TAB = '\t' as Character
    private static final Character EQUAL = '=' as Character
    private static final List<Character> SINGLE_CHAR_SYMBOLS =  Arrays.stream(TokenType.values())
            .filter({t -> t.isOnlySingleCharSymbol()}).map({t -> t.value as Character})
            .collect(Collectors.toList()) //['(', ')', '+', '-', '*', '/', ',', '%', '^', ';', '{', '}']

    private final LogicalController<Token> logicalController
    private final StringBuilder valueBuilder = new StringBuilder()
    private int col
    private int lig
    private boolean keepLast
    private Character nextChar

    LexicalAnalyzer() {
        logicalController = new LogicalController(transitionTable, returnTable)
        col = 0
        lig = 0
        keepLast = false
        valueBuilder.metaClass.clear { //add method clear() to this instance
            valueBuilder.setLength(0)
        }
    }

    List<Token> toTokens(File file) throws LexicalException {
        return toTokens(file.getText())
    }

    List<Token> toTokens(String content) throws LexicalException {
        List<Token> tokens = new ArrayList<>()
        content = content + LINE_BREAK //add line return to simulate end of file

        for (int i = 0; i < content.size(); i++) {
            char c = content.charAt(i)
            if (!c.isWhitespace() && !(c == LINE_BREAK)) {
                valueBuilder.append(c)
            }
            Token token = logicalController.act(c)

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
            throw new LexicalException("Unexpected end of file", lig, col)
        }
        tokens.add(Token.of(TokenType.END_OF_FILE, col, lig))
        return tokens
    }

    private static String truncateLast(String s) {
        return s.substring(0, s.length() - 1)
    }

    private Token returnValue(int currentState, int nextState) {
        String value = valueBuilder.toString().trim()
        int col = this.col
        keepLast = false

        switch (currentState) {
            case INITIAL_STATE:
                if (nextState == INITIAL_STATE && !isInvisibleChar(nextChar)) {
                    TokenType t = TokenUtils.SYMBOLS_MAP.get(value)
                    if (t == null) {
                        throw new LexicalException("Couldn't resolve symbol: $value", lig, col)
                    }
                    return Token.of(t, col > 0 ? col - 1: 0, lig)
                } else {
                    return null
                }

            case WORD_STATE:
                String name

                if (nextState == SYMBOL_STATE) {
                    name = truncateLast(value)
                    keepLast = true
                    //col++
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
                    String sNumber = truncateLast(value)
                    number = Integer.parseInt(sNumber)
                    keepLast = true
                    col -=  sNumber.size() //to get the position of the beginning of the number
                    //col++
                } else if (nextState == INITIAL_STATE) {
                    number = Integer.parseInt(value)
                    col -=  value.size() //to get the position of the beginning of the number
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
                    col--
                    //col++
                } else if (nextState == INITIAL_STATE) {
                    t = TokenUtils.SYMBOLS_MAP.getOrDefault(value, null)
                } else { // nextState == SYMBOL_STATE
                    t = TokenUtils.SYMBOLS_MAP.get(truncateLast(value))
                    keepLast = true
                    col--
                    //col++
                }

                if (t == null) {
                    throw new LexicalException("Couldn't resolve symbol: $value", lig, col)
                }
                return Token.of(t, col, lig)
        }
        return null
    }

    private static boolean isInvisibleChar(char c) {
        return c == SPACE || c == LINE_BREAK || c == TAB
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
                    throw new LexicalException("Cannot have a character next to a number", lig, col)

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

        throw new LexicalException("Illegal character '$entry' encountered", lig, col)
    }

    void reset() {
        lig = 0
        col = 0
        valueBuilder.clear()
        keepLast = false
        logicalController.setState(INITIAL_STATE)
    }

}
