package com.tambapps.compiler.exception;

class LexicalException extends CompileException {

  LexicalException(String message, int l, int c) {
    super(message, l, c)
  }

}
