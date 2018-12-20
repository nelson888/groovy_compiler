package com.tambapps.compiler.util

import com.tambapps.compiler.exception.SymbolException

import java.util.concurrent.LinkedBlockingDeque

class DequeMap {

  private final Deque<Map<String, Symbol>> symbolsMap

  DequeMap() {
    symbolsMap = new LinkedBlockingDeque<>()
    newBlock()
  }

  void newBlock() {
    symbolsMap.push(new HashMap<>())
  }

  void endBlock() {
    try {
      symbolsMap.pop()
    } catch (NoSuchElementException e) {
      throw new SymbolException("There isn't any scope to end")
    }

  }

  Symbol newSymbol(String ident) {
    def map = symbolsMap.peek()
    if (map.containsKey(ident)) {
      throw new SymbolException("$ident is already defined")
    }
    Symbol s = new Symbol(ident)
    map.put(ident, s)
    return s
  }

  Symbol findSymbol(String ident) {
    for (def map : symbolsMap.descendingIterator()) {
      if (map.containsKey(ident)) {
        return map.get(ident)
      }
    }
    throw new SymbolException("Symbol not $ident found")
  }

  Symbol findSymbol(def varRef) {
    return findSymbol((String)varRef.name)
  }

  Symbol findSymbolWithSlot(int slot) {
    for (def map : symbolsMap.descendingIterator()) {
      for (Symbol s : map.values()) {
        if (s.slot == slot) {
          return s
        }
      }
    }
    throw new SymbolException("Symbol not found")
  }
}
