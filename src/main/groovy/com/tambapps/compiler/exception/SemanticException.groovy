package com.tambapps.compiler.exception

class SemanticException extends CompileException {

  SemanticException(String message, int l, int c) {
    super(message, l, c)
  }

}
