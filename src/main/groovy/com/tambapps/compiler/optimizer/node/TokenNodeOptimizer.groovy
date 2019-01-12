package com.tambapps.compiler.optimizer.node

import com.tambapps.compiler.analyzer.token.TokenNode

class TokenNodeOptimizer {

  private List<NodeOptimizer> optimizers = [new ExpressionOptimizer()]

  /** optimizeProgram after semantic analysis **/
  void optimize(TokenNode program) {
    optimizeProgram(program, optimizers)
  }

  private void optimizeProgram(TokenNode program, List<NodeOptimizer> optimizers) {
    for (def optimizer : optimizers) {
      if (optimizer.isOptimizable(program.type)) {
        optimizer.optimizeNode(program, null)
      } else {
        optimizeWith(optimizer, program)
      }
    }
  }

  private optimizeWith(NodeOptimizer optimizer, TokenNode node) {
    for (int i = 0; i < node.nbChildren(); i++) {
      TokenNode child = node.getChild(i)
      if (optimizer.isOptimizable(child.type)) {
        optimizer.optimizeNode(node, child)
      } else {
        optimizeWith(optimizer, child)
      }
    }
  }

}
