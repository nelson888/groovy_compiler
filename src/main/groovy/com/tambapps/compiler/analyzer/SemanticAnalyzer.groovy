package com.tambapps.compiler.analyzer

import com.tambapps.compiler.analyzer.token.TokenNode
import com.tambapps.compiler.analyzer.token.TokenNodeType
import com.tambapps.compiler.exception.SemanticException
import com.tambapps.compiler.exception.SymbolException
import com.tambapps.compiler.util.DequeMap
import com.tambapps.compiler.util.Symbol

class SemanticAnalyzer {

  private DequeMap dequeMap = new DequeMap()
  private int nbSlot = 0

  void process(TokenNode node) throws SemanticException {
    switch (node.type) {
      case TokenNodeType.VAR_DECL:
        Symbol s
        try {
          s = dequeMap.newSymbol(node.value)
        } catch (SymbolException e) {
          throw new SemanticException(e.message, node.l, node.c)
        }
        s.slot = nbSlot++
        break
      case TokenNodeType.VAR_REF:
        Symbol s
        try {
          s = dequeMap.findSymbol(node.value.name)
        } catch (SymbolException e) {
          throw new SemanticException(e.message, node.l, node.c)
        }

        node.value.index = s.slot
        break
      case TokenNodeType.BLOC:
        dequeMap.newBlock()
        for (int i = 0; i < node.nbChildren(); i++) {
          process(node.getChild(i))
        }
        try {
          dequeMap.endBlock()
        } catch (SymbolException e) {
          throw new SemanticException(e.message, node.l, node.c)
        }

        break
      default:
        for (int i = 0; i < node.nbChildren(); i++) {
          process(node.getChild(i))
        }
    }
  }

  int getNbSlot() {
    return nbSlot
  }
}
