package com.tambapps.compiler.util

import groovy.transform.PackageScope

class Symbol {

  String ident
  int slot
  int nbArgs = -1

  @PackageScope
  Symbol(String s) {
    ident = s
  }

  @PackageScope
  Symbol(String s, int nbArgs) {
    ident = s
    this.nbArgs = nbArgs
  }

  boolean isFunction() {
    return nbArgs >= 0
  }

}
