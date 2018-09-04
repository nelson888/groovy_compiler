package com.tambapps.analyzer

class Token {

    TokenType type
    int l
    int c
    def value = null

    @Override
    String toString() {
        StringJoiner joiner = new StringJoiner(', ', '{', '}')
                .add("type=$type")
        if (value) {
            joiner.add("value=$value")
        }
        joiner.add("l=$l")
                .add("c=$c")
        return joiner.toString()
    }

}