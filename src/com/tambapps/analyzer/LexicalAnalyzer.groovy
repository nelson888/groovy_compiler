package com.tambapps.analyzer

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
    private static final int IDENTIFIER_STATE = 1
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

    LexicalAnalyzer() {
        logicalController = new LogicalController(transitionTable, returnTable)
    }

    List<Token> toTokens(String content) {
        List<Token> tokens = new ArrayList<>()
        content = content + LINE_BREAK //add line return to simulate end of file

        int col = 0
        int lig = 0

        for (int i = 0; i < content.size(); i++) {
            char c = content.charAt(i)
            if (!c.isWhitespace() && !(c == LINE_BREAK)) {
                valueBuilder.append(c)
            }
            Token token = logicalController.act(c)
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
        return tokens
    }

    Token[] toTokens(File file) {
        return toTokens(file.getText())
    }

    private Token returnValue(int currentState, int nextState) {
        String value = valueBuilder.toString()
        switch (currentState) {
            case INITIAL_STATE:
                return null
            case IDENTIFIER_STATE:
                return nextState == INITIAL_STATE ? Token.of(TokenType.IDENTIFIER, value) : null
            case CONSTANT_STATE:
                return nextState == INITIAL_STATE ? Token.of(TokenType.CONSTANT, Integer.parseInt(value)) : null
            case PLUS_STATE:
                return nextState == INITIAL_STATE ? Token.of(TokenType.PLUS) : null
            case MINUS_STATE:
                return nextState == INITIAL_STATE ? Token.of(TokenType.MINUS) : null
            case DIVIDE_STATE:
                return nextState == INITIAL_STATE ? Token.of(TokenType.DIVIDE) : null
            case MULTIPLY_STATE:
                return nextState == INITIAL_STATE ? Token.of(TokenType.MULTIPLY) : null
            case POWER_STATE:
                return nextState == INITIAL_STATE ? Token.of(TokenType.POWER) : null
            case MODULO_STATE:
                return nextState == INITIAL_STATE ? Token.of(TokenType.MODULO) : null
        }
        return null
    }

    private static int nextState(int currentState, Character entry) {
        switch (currentState) {
            case INITIAL_STATE:
                if (entry.isDigit()) {
                    return CONSTANT_STATE
                } else if (entry.isLetter()) {
                    return IDENTIFIER_STATE
                } else if (entry in OPERATORS) {
                    return OPERATOR_STATE_MAP.get(entry)
                } else if (entry == SPACE || entry == LINE_BREAK) { //== in groovy calls .equals() ??
                    return INITIAL_STATE
                }
                break

            case IDENTIFIER_STATE:
                if (entry.isDigit() || entry.isLetter()) {
                    return IDENTIFIER_STATE
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
                    throw new IllegalStateException("Syntax error") //TODO indiquer ligne col
                }
                break

            case PLUS_STATE:
            case MINUS_STATE:
            case MULTIPLY_STATE:
            case DIVIDE_STATE:
            case MODULO_STATE:
                if (entry in (['*', '/', '%'] as Character[])) {
                    throw new IllegalStateException("Cannot have two operator")
                }
                return INITIAL_STATE
        }

        throw new IllegalStateException("Illegal character '$entry' encountered") //TODO indiquer ligne et col
    }

}
