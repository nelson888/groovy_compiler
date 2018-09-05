package com.tambapps.analyzer

import com.tambapps.exception.IllegalTransitionStateException
import com.tambapps.exception.LexicalException
import com.tambapps.util.LogicalController
import com.tambapps.util.ReturnTable
import com.tambapps.util.TransitionTable

class LexicalAnalyzer {

    static {
        OPERATOR_STATE_MAP.put('+' as Character, PLUS_STATE)
        OPERATOR_STATE_MAP.put('-' as Character, MINUS_STATE)
        OPERATOR_STATE_MAP.put('/' as Character, DIVIDE_STATE)
        OPERATOR_STATE_MAP.put('*' as Character, MULTIPLY_STATE)
        OPERATOR_STATE_MAP.put('^' as Character, POWER_STATE)
        OPERATOR_STATE_MAP.put('%' as Character, MODULO_STATE)

        OPERATORS = OPERATOR_STATE_MAP.keySet()
    }

    //logical controller stuff
    private static final Collection<Character> OPERATORS
    private static final Map<Character, Integer> OPERATOR_STATE_MAP = new HashMap<>() //map an operator to its corresponding state
    private static final int INITIAL_STATE = 0
    private static final int WORD_STATE = 1
    private static final int CONSTANT_STATE = 2
    private static final int PLUS_STATE = 3
    private static final int MINUS_STATE = 4
    private static final int DIVIDE_STATE = 5
    private static final int MULTIPLY_STATE = 6
    private static final int POWER_STATE = 7
    private static final int MODULO_STATE = 8

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

    LexicalAnalyzer() {
        logicalController = new LogicalController(transitionTable, returnTable)
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
            } catch (IllegalTransitionStateException e) {
                throw new LexicalException("Lexical error at l:$lig c:$col\n$e.message", e)
            }

            if (token) {
                tokens.add(token)
                valueBuilder.setLength(0) //clear the string builder
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
                TokenType t = TokenType.KEYWORDS_MAP.getOrDefault(value, TokenType.IDENTIFIER)
                if (nextState != currentState) {
                    return t == TokenType.IDENTIFIER ? Token.of(t, value, col, lig) : Token.of(t, col, lig)
                }
                return null
            case CONSTANT_STATE:
                return nextState == INITIAL_STATE ? Token.of(TokenType.CONSTANT, Integer.parseInt(value), col, lig) : null
            case PLUS_STATE:
                return nextState == INITIAL_STATE ? Token.of(TokenType.PLUS, col, lig) : null
            case MINUS_STATE:
                return nextState == INITIAL_STATE ? Token.of(TokenType.MINUS, col, lig) : null
            case DIVIDE_STATE:
                return nextState == INITIAL_STATE ? Token.of(TokenType.DIVIDE, col, lig) : null
            case MULTIPLY_STATE:
                return nextState == INITIAL_STATE ? Token.of(TokenType.MULTIPLY, col, lig) : null
            case POWER_STATE:
                return nextState == INITIAL_STATE ? Token.of(TokenType.POWER, col, lig) : null
            case MODULO_STATE:
                return nextState == INITIAL_STATE ? Token.of(TokenType.MODULO, col, lig) : null
        }
        return null
    }

    private static int nextState(int currentState, Character entry) {
        switch (currentState) {
            case INITIAL_STATE:
                if (entry.isDigit()) {
                    return CONSTANT_STATE
                } else if (entry.isLetter()) {
                    return WORD_STATE
                } else if (entry in OPERATORS) {
                    return OPERATOR_STATE_MAP.get(entry)
                } else if (entry == SPACE || entry == LINE_BREAK) { //== in groovy calls .equals() ??
                    return INITIAL_STATE
                }
                break

            case WORD_STATE:
                if (entry.isDigit() || entry.isLetter()) {
                    return WORD_STATE
                } else if (entry == SPACE || entry == LINE_BREAK) {
                    return INITIAL_STATE
                }
                break
            case CONSTANT_STATE:
                if (entry.isDigit()) {
                    return CONSTANT_STATE
                } else if (entry == SPACE || entry == LINE_BREAK) {
                    return INITIAL_STATE
                } else {
                    throw new IllegalTransitionStateException("Syntax error")
                }
                break

            case PLUS_STATE:
            case MINUS_STATE:
            case MULTIPLY_STATE:
            case DIVIDE_STATE:
            case MODULO_STATE:
                if (entry in (['*', '/', '%'] as Character[])) {
                    throw new IllegalTransitionStateException("Cannot have two operator")
                }
                return INITIAL_STATE
        }

        throw new IllegalTransitionStateException("Illegal character '$entry' encountered")
    }

}
