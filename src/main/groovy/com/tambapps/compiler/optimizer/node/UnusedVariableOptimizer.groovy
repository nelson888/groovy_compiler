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
    //get the name of all used variables (skip argument variable declarations)
    usedVariables.addAll(getVarRefs(function.getChild(function.nbChildren() - 1)))

    //unused variables are declared variables not in usedVariables
    List<TokenNode> unusedVarDecls = varDecls.findAll { varDecl -> isVarUsed(varDecl, usedVariables) }
    for (def toRemove : unusedVarDecls) {
      function.removeDescending(toRemove)
    }
  }

  private boolean isVarUsed(TokenNode varDecl, Set<String> usedVariables) {
    if (varDecl.type == TokenNodeType.SEQ) {
      return !isVarIn(varDecl.getChild(0), usedVariables)
    } else {
      return !isVarIn(varDecl, usedVariables)
    }
  }

  private boolean isVarIn(TokenNode varDecl, Set<String> usedVariables) {
    return varDecl.value in usedVariables
  }

  private Set<TokenNode> getVarRefs(TokenNode node) {
    if (node.type == TokenNodeType.VAR_DECL) {
      varDecls.add(node)
      return Collections.emptySet()
    } else if (node.type == TokenNodeType.VAR_REF) {
      return Collections.singleton(node.value)
    } else if (node.type == TokenNodeType.SEQ
        && node.getChild(0).type == TokenNodeType.VAR_DECL) { //when var a = 5; => SEQ  1) VAR_DECL 2) ASSIGNMENT
      varDecls.add(node.getChild(0))
      return Collections.emptySet()
    } else {
      Set<TokenNode> result = new HashSet<>()
      for (int i = 0; i < node.nbChildren(); i++) {
        result.addAll(getVarRefs(node.getChild(i)))
      }
      return result
    }
  }
}
