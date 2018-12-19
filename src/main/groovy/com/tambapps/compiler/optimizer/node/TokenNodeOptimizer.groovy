package com.tambapps.compiler.optimizer.node

import com.tambapps.compiler.analyzer.token.TokenNode

/* TODO optimize AFTER semantic analysis
 */
class TokenNodeOptimizer {

  private List<NodeOptimizer> optimizers =
      [new ExpressionOptimizer(),
       new UnusedVariableOptimizer()]

  void optimize(TokenNode program) {
    for (def optimizer : optimizers) {
      if (optimizer.isOptimizable(program.type)) {
        optimizer.optimizeNode(program)
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
