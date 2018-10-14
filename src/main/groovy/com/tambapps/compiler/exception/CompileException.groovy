package com.tambapps.compiler.exception

import groovy.transform.PackageScope

@PackageScope
class CompileException extends RuntimeException {

  CompileException(String message, int l, int c) {
    super("At l:$l c:$c: " + message)
  }

}
