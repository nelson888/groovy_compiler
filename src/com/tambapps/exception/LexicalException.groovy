package com.tambapps.exception;

class LexicalException extends RuntimeException {

  LexicalException(String message, Throwable cause) {
    super(message, cause)
  }

  LexicalException(String message) {
    super(message)
  }

}
