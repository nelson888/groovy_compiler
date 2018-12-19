package com.tambapps.compiler.analyzer.token

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
  String toString() {
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

  boolean equals(o) {
    if (this.is(o)) return true
    if (!(o instanceof AbstractToken)) return false

    AbstractToken that = (AbstractToken) o

    if (c != that.c) return false
    if (l != that.l) return false
    if (value != that.value) return false

    return true
  }

  int hashCode() {
    int result
    result = l
    result = 31 * result + c
    result = 31 * result + (value != null ? value.hashCode() : 0)
    return result
  }
}
