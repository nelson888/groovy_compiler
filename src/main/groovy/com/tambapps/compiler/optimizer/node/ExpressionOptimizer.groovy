package com.tambapps.compiler.optimizer.node

import com.tambapps.compiler.analyzer.token.TokenNode
import com.tambapps.compiler.analyzer.token.TokenNodeType
import groovy.transform.PackageScope

/**
 * Optimize expression with constant operations by replacing the operation
 * by the actual value
 */
@PackageScope
class ExpressionOptimizer implements NodeOptimizer {

  static final Map<TokenNodeType, Closure> OPERATOR_MAP

  static {
    def map = new HashMap<TokenNodeType, Closure>()
    map.put(TokenNodeType.PLUS_B, {a1, a2 -> return a1 + a2 })
    map.put(TokenNodeType.MULTIPLY, {a1, a2 -> return a1 * a2 })
    map.put(TokenNodeType.MODULO, {a1, a2 -> return a1 % a2 })
    map.put(TokenNodeType.DIVIDE, {a1, a2 -> return a1 / a2 })
    map.put(TokenNodeType.POWER, {a1, a2 -> return power(a1, a2) })
    map.put(TokenNodeType.MINUS_B, {a1, a2 -> return a1 - a2 })

    map.put(TokenNodeType.EQUAL, {a1, a2 -> return intBool(a1 == a2) })
    map.put(TokenNodeType.NOT_EQUAL, {a1, a2 -> return intBool(a1 != a2) })
    map.put(TokenNodeType.STRICT_INF, {a1, a2 -> return intBool(a1 < a2) })
    map.put(TokenNodeType.STRICT_SUP, {a1, a2 -> return intBool(a1 > a2) })
    map.put(TokenNodeType.SUP, {a1, a2 -> return intBool(a1 >= a2) })
    map.put(TokenNodeType.INF, {a1, a2 -> return intBool(a1 <= a2) })
    map.put(TokenNodeType.AND, {a1, a2 -> return intBool(a1 && a2) })
    map.put(TokenNodeType.OR, {a1, a2 -> return intBool(a1 || a2) })

    map.put(TokenNodeType.MINUS_U, {a -> return - a })
    map.put(TokenNodeType.PLUS_U, {a -> return a })
    map.put(TokenNodeType.NOT, {a -> return intBool(!a) })

    OPERATOR_MAP = Collections.unmodifiableMap(map)
  }

  @Override
  boolean isOptimizable(TokenNodeType type) {
    return type.isBinaryOperator() || type.isUnaryOperator()
  }

  @Override
  void optimizeNode(TokenNode parent, TokenNode node) {
    if (node.type.binaryOperator) {
      TokenNode arg1 = node.getChild(0)
      TokenNode arg2 = node.getChild(1)
      optimizeNode(node, arg1)
      optimizeNode(node, arg2)
      if (isConstant(arg1) && isConstant(arg2)) {
        parent.replaceChild(node, new TokenNode(node, TokenNodeType.CONSTANT,
            OPERATOR_MAP.get(node.type).call(arg1.value, arg2.value)))
      }
    } else if (node.type.unaryOperator) {
      TokenNode arg = node.getChild(0)
      optimizeNode(node, arg)
      if (isConstant(arg)) {
        parent.replaceChild(node, new TokenNode(node, TokenNodeType.CONSTANT,
            OPERATOR_MAP.get(node.type).call(arg.value)))
      }
    }
  }

  private static boolean isConstant(TokenNode node) {
    return node.type == TokenNodeType.CONSTANT
  }

  private def power(a, b) {
    //TODO
  }

  private int intBool(boolean b) {
    return b ? 1 : 0
  }
}
