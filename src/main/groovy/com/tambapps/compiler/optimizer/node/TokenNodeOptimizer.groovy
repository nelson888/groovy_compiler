package com.tambapps.compiler.optimizer.node

import com.tambapps.compiler.analyzer.token.TokenNode

class TokenNodeOptimizer {

  private List<NodeOptimizer> beforeOptimizers =
      [new UnusedVariableOptimizer()]

  private List<NodeOptimizer> afterOptimizers =
      [new ExpressionOptimizer()]

  /** optimize before semantic analysis **/
  void beforeOptimize(TokenNode program) {
    optimize(program, beforeOptimizers)
  }

  /** optimize after semantic analysis **/
  void afterOptimize(TokenNode program) {
    optimize(program, afterOptimizers)
  }

  private void optimize(TokenNode program, List<NodeOptimizer> optimizers) {
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
