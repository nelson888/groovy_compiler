package com.tambapps.analyzer

import com.tambapps.exception.UnsupportedCharacterException
import com.tambapps.util.LogicalController
import com.tambapps.util.ReturnTable
import com.tambapps.util.TransitionTable

import java.util.stream.Collectors

class LexicalAnalyzer {

    static {
        OPERATOR_STATE_MAP.put('+' as Character, PLUS_STATE)
        OPERATOR_STATE_MAP.put('-' as Character, MINUS_STATE)
        //TODO add others operators
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

    private static final TransitionTable TRANSITION_TABLE = { int currentState, char entry ->
        nextState(currentState, entry)
    } as TransitionTable

    private static final ReturnTable RETURN_TABLE  = { int currentState, int nextState ->
        returnValue(currentState, nextState)
    } as ReturnTable

    private static final Character SPACE = ' ' as Character
    private static final Character LINE_BREAK = '\n' as Character

    private final LogicalController<TokenType> logicalController

    LexicalAnalyzer() {
        logicalController = new LogicalController(TRANSITION_TABLE, RETURN_TABLE)
    }

    List<Token> toTokens(String content) {
        content = content + LINE_BREAK //add line return to simulate end of file
        List<Token> tokens = new ArrayList<>(content.size())
        int col = 0
        int lig = 0
        List<Character> characters = new ArrayList<>()
        for (int i = 0; i < content.size(); i++) {
            char c = content.charAt(i)
            if (!c.isWhitespace() && !(c == LINE_BREAK)) {
                characters.add(c)
            }
            Token token = processCharacter(characters, c, col, lig)
            if (token) {
                tokens.add(token)
                characters.clear()
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

    private Token processCharacter(List<Character> characters, char c, int col, int lig) {
        TokenType type = logicalController.act(c)
        if (type != null) {
            String value = characters.stream()
                    .map({ch -> String.valueOf(ch)})
                    .collect(Collectors.joining())
            Token token = new Token(type: type, c: col - characters.size(), l: lig)
            if (type == TokenType.CONSTANT) {
                token.setValue(Long.parseLong(value))
            } else if (type == TokenType.IDENTIFIER) {
                token.setValue(value)
            }

            return token
        }

        return null
    }

    Token[] toTokens(File file) {
        return toTokens(file.getText())
    }

    private static TokenType returnValue(int currentState, int nextState) {
        switch (currentState) {
            case INITIAL_STATE:
                return null
            case IDENTIFIER_STATE:
                return nextState == INITIAL_STATE ? TokenType.IDENTIFIER : null
            case CONSTANT_STATE:
                return nextState == INITIAL_STATE ? TokenType.CONSTANT : null
            case PLUS_STATE:
                return nextState == INITIAL_STATE ? TokenType.PLUS : null
            case MINUS_STATE:
                return nextState == INITIAL_STATE ? TokenType.MINUS : null
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
                } else if (OPERATORS.contains(entry)) {
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
                return INITIAL_STATE

        }

        throw new IllegalStateException("Illegal character '$entry' encountered") //TODO indiquer ligne et col
    }

}
