package com.tambapps.compiler.optimizer.node

import com.tambapps.compiler.analyzer.token.TokenNode
import com.tambapps.compiler.analyzer.token.TokenNodeType
import com.tambapps.compiler.model.TestTokenNode as TTN

class ExpressionOptimizerTest extends GroovyTestCase {

  private static final TokenNode ASSIGNMENT_TOKEN = new TTN(TokenNodeType.ASSIGNMENT)
  private static final TokenNode VAR_TOKEN = new TTN(TokenNodeType.VAR_REF)

  private ExpressionOptimizer optimizer = new ExpressionOptimizer()

  void testAdd() {
    TokenNode plus = new TTN(TokenNodeType.PLUS_B)

    plus.addChildren(new TTN(4),
        new TTN(4))

    initAssignTok(plus)
    optimizer.optimizeNode(ASSIGNMENT_TOKEN, plus)
    assertEquals("Should be 8", new TTN(8), ASSIGNMENT_TOKEN.getChild(1))
  }

  void testUnary() {
    ASSIGNMENT_TOKEN.removeAllChildren()

    TokenNode plus = new TTN(TokenNodeType.PLUS_U)
        .withChildren(new TTN(TokenNodeType.MINUS_U)
        .withChildren(new TTN(TokenNodeType.MINUS_U)
        .withChildren(new TTN(TokenNodeType.PLUS_U)
        .withChildren(new TTN(TokenNodeType.MINUS_U)
        .withChildren(new TTN(1))))))

    initAssignTok(plus)
    optimizer.optimizeNode(ASSIGNMENT_TOKEN, plus)
    assertEquals("Should be 8", new TTN(-1), ASSIGNMENT_TOKEN.getChild(1))
  }

  private static void initAssignTok(TokenNode node) {
    ASSIGNMENT_TOKEN.removeAllChildren()

    ASSIGNMENT_TOKEN.addChildren(VAR_TOKEN, node)
  }

  void testMulAd() {
    TokenNode plus = new TTN(TokenNodeType.PLUS_B)
    plus.addChildren(new TTN(TokenNodeType.MULTIPLY).withChildren(new TTN(3), new TTN(2)),
        new TTN(4))

    initAssignTok(plus)
    optimizer.optimizeNode(ASSIGNMENT_TOKEN, plus)
    assertEquals("Should be 10", new TTN(10), ASSIGNMENT_TOKEN.getChild(1))
  }

}
