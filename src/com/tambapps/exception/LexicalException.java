package com.tambapps.exception;

public class LexicalException extends RuntimeException {

  public LexicalException(String message, Throwable cause) {
    super(message, cause);
  }

  public LexicalException(String message) {
    super(message);
  }

}
