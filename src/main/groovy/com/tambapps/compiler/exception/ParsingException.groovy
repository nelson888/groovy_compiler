package com.tambapps.compiler.exception

class ParsingException extends CompileException {

  ParsingException(String message, int l, int c) {
    super(message, l, c)
  }

}
