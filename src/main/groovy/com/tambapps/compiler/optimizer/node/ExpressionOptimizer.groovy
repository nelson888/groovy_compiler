package com.tambapps.compiler.optimizer.node

import static com.tambapps.compiler.analyzer.token.TokenUtils.OPERATOR_MAP


import com.tambapps.compiler.analyzer.token.TokenNode
import com.tambapps.compiler.analyzer.token.TokenNodeType
import groovy.transform.PackageScope

/**
 * Optimize expression with constant operations by replacing the operation
 * by the actual value
 */
@PackageScope
class ExpressionOptimizer implements NodeOptimizer {

  @Override
  boolean isOptimizable(TokenNodeType type) {
    return type.binaryOperator || type.unaryOperator
  }

  @Override
  void optimizeNode(TokenNode parent, TokenNode node) {
    if (node.type.binaryOperator) {
      optimizeNode(node, node.getChild(0))
      optimizeNode(node, node.getChild(1))
      TokenNode arg1 = node.getChild(0) //decl here and not above because we must update value of node after optimizing it
      TokenNode arg2 = node.getChild(1)
      if (isConstant(arg1) && isConstant(arg2)) {
        parent.replaceChild(node, new TokenNode(node, TokenNodeType.CONSTANT,
            OPERATOR_MAP.get(node.type).call(arg1.value, arg2.value)))
      }
    } else if (node.type.unaryOperator) {
      optimizeNode(node, node.getChild(0))
      TokenNode arg = node.getChild(0)
      if (isConstant(arg)) {
        parent.replaceChild(node, new TokenNode(node, TokenNodeType.CONSTANT,
            OPERATOR_MAP.get(node.type).call(arg.value)))
      }
    }
  }

  private static boolean isConstant(TokenNode node) {
    return node.type == TokenNodeType.CONSTANT
  }

}
