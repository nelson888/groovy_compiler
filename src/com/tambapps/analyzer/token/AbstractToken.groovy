package com.tambapps.analyzer.token

import groovy.transform.PackageScope

@PackageScope
abstract class AbstractToken {
    final int l
    final int c
    final def value

    AbstractToken(int l, int c, def value) {
        this.l = l
        this.c = c
        this.value = value
    }

    AbstractToken(int l, int c) {
        this(l, c, null)
    }

    @Override
    final String toString() {
        def type = getType()
        StringJoiner joiner = new StringJoiner(', ', '{', '}')
                .add("type=$type")
        if (value) {
            joiner.add("value=$value")
        }
        joiner.add("l=$l")
                .add("c=$c")
        return joiner.toString()
    }

    abstract def getType()
}
