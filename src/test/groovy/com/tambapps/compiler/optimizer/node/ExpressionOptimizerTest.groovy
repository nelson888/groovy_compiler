package com.tambapps.compiler.optimizer.node

import com.tambapps.compiler.analyzer.token.TokenNode
import com.tambapps.compiler.analyzer.token.TokenNodeType
import com.tambapps.compiler.model.TestTokenNode

class ExpressionOptimizerTest extends GroovyTestCase {

  private static final TokenNode ASSIGNMENT_TOKEN = new TestTokenNode(TokenNodeType.ASSIGNMENT)

  private ExpressionOptimizer optimizer = new ExpressionOptimizer()


  void testAdd() {
    ASSIGNMENT_TOKEN.removeAllChildren()

    TokenNode plus = new TestTokenNode(TokenNodeType.PLUS_B)

    plus.addChildren(new TestTokenNode(TokenNodeType.CONSTANT, 4),
        new TestTokenNode(TokenNodeType.CONSTANT, 4))

    ASSIGNMENT_TOKEN.addChild(plus)
    optimizer.optimizeNode(ASSIGNMENT_TOKEN, plus)
    assertEquals("Should be 8", new TestTokenNode(TokenNodeType.CONSTANT, 8), ASSIGNMENT_TOKEN.getChild(0))
  }

  void testUnary() {
    ASSIGNMENT_TOKEN.removeAllChildren()

    TokenNode plus = new TestTokenNode(TokenNodeType.PLUS_U)
        .withChildren(new TestTokenNode(TokenNodeType.MINUS_U)
        .withChildren(new TestTokenNode(TokenNodeType.MINUS_U)
        .withChildren(new TestTokenNode(TokenNodeType.PLUS_U)
        .withChildren(new TestTokenNode(TokenNodeType.CONSTANT, 1)))))

    ASSIGNMENT_TOKEN.addChild(plus)
    optimizer.optimizeNode(ASSIGNMENT_TOKEN, plus)
    assertEquals("Should be 8", new TestTokenNode(TokenNodeType.CONSTANT, 1), ASSIGNMENT_TOKEN.getChild(0))
  }

}
