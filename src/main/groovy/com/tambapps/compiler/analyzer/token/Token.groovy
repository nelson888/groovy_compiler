package com.tambapps.compiler.analyzer.token

class Token extends AbstractToken {

  final TokenType type

  Token(int l, int c, value, TokenType type) {
    super(l, c, value)
    this.type = type
  }

  Token(int l, int c, TokenType type) {
    super(l, c, null)
    this.type = type
  }

  static Token of(TokenType type, int c, int l) {
    return new Token(l, c, type)
  }

  static Token of(TokenType type, def value, int c, int l) {
    return new Token(l, c, value, type)
  }

  boolean equals(o) {
    if (this.is(o)) return true
    if (!(o instanceof Token)) return false
    if (!super.equals(o)) return false

    Token token = (Token) o

    if (type != token.type) return false

    return true
  }

  int hashCode() {
    int result = super.hashCode()
    result = 31 * result + (type != null ? type.hashCode() : 0)
    return result
  }
}