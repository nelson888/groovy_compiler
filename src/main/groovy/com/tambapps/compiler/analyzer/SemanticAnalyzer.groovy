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

      case TokenNodeType.D_REF:
        //TODO

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

      case TokenNodeType.FUNCTION:
        nbSlot = 0
        Symbol s
        try {
          s = dequeMap.newSymbol(node.value.name)
        } catch (SymbolException e) {
          throw new SemanticException(e.message, node.l, node.c)
        }
        int nbArgs = node.nbChildren() - 1
        s.nbArgs = nbArgs
        node.value.nbArgs = nbArgs
        dequeMap.newBlock()
        for (int i = 0; i < node.nbChildren(); i++) {
          process(node.getChild(i))
        }
        dequeMap.endBlock()
        s.slot = nbSlot - s.nbArgs
        break

      case TokenNodeType.FUNCTION_CALL:
        Symbol s
        try {
          s = dequeMap.findSymbol(node.value.name)
        } catch (SymbolException e) {
          throw new SemanticException(e.message, node.l, node.c)
        }
        if (!s.function) {
          throw new SemanticException("Cannot call a variable ($s.ident)", node.l, node.c)
        }
        if (s.nbArgs != node.nbChildren()) {
          throw new SemanticException("Function $s.ident expects $s.nbArgs parameters (got ${node.nbChildren()})",
                  node.l, node.c)
        }
        for (int i = 0; i < node.nbChildren(); i++) {
          process(node.getChild(i))
        }
        break

      default:
        for (int i = 0; i < node.nbChildren(); i++) {
          process(node.getChild(i))
        }
    }
  }

  void reset() {
    dequeMap = new DequeMap()
    nbSlot = 0
  }

}
