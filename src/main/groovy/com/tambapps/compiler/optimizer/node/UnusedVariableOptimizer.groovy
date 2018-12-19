package com.tambapps.compiler.optimizer.node

import com.tambapps.compiler.analyzer.token.TokenNode
import com.tambapps.compiler.analyzer.token.TokenNodeType
import groovy.transform.PackageScope

@PackageScope
class UnusedVariableOptimizer implements NodeOptimizer {

  List<TokenNode> varDecls = []

  @Override
  boolean isOptimizable(TokenNodeType type) {
    return type == TokenNodeType.FUNCTION
  }

  @Override
  void optimizeNode(TokenNode parent, TokenNode function) {
    Set<String> usedVariables = new HashSet<>()
    for (int i = 0; i < function.nbChildren(); i++) {
      usedVariables.addAll(checkVarRefs(function.getChild(i)))
    }

    List<TokenNode> unusedVarDecls = varDecls.findAll { varDecl -> !(varDecl.value in usedVariables) }
    for (def toRemove : unusedVarDecls) {
      parent.remove(toRemove)
    }
  }


  private Set<TokenNode> checkVarRefs(TokenNode node) {
    if (node.type == TokenNodeType.VAR_DECL) {
      varDecls.add(node)
      return Collections.emptySet()
    } else if (node.type == TokenNodeType.VAR_REF) {
      return Collections.singleton(node.value)
    } else {
      Set<TokenNode> result = new HashSet<>()
      for (int i = 0; i < node.nbChildren(); i++) {
        result.addAll(checkVarRefs(node.getChild(i)))
      }
      return result
    }
  }
}
