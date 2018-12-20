package com.tambapps.compiler.model

import com.tambapps.compiler.analyzer.token.Token
import com.tambapps.compiler.analyzer.token.TokenNode
import com.tambapps.compiler.analyzer.token.TokenNodeType
import com.tambapps.compiler.analyzer.token.TokenType

class TestTokenNode extends TokenNode {

  private static final Token BASE_TOKEN = new Token(0, 0, TokenType.END_OF_FILE)
  TestTokenNode(TokenNodeType type) { //we're only interested in type
    super(BASE_TOKEN, type)
  }

  TestTokenNode(TokenNodeType type, value) {
    super(BASE_TOKEN, type, value)
  }

  TestTokenNode(int value) {
    super(BASE_TOKEN, TokenNodeType.CONSTANT, value)
  }

  @Override
  boolean equals(Object o) {
    if(! (o instanceof TokenNode)) return false
    return type == o.type  && value == o.value
  }
}
