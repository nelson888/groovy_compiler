package com.tambapps.compiler.optimizer.node

import com.tambapps.compiler.analyzer.LexicalAnalyzer
import com.tambapps.compiler.analyzer.Parser
import com.tambapps.compiler.analyzer.token.TokenNode

class UnusedVariableOptimizerTest extends GroovyTestCase {
  private static final String TESTS = UnusedVariableOptimizerTest
      .getResourceAsStream("/unusedVarTests.txt").text
  //assuming LA and Parser works well
  private final LexicalAnalyzer analyzer = new LexicalAnalyzer()
  private final Parser parser = new Parser()
  private final UnusedVariableOptimizer optimizer = new UnusedVariableOptimizer()

  void test() {
    for(String test : TESTS.split("__")) {
      String[] fields = test.split("_")
      TokenNode input = generate(fields[0])
      optimizer.optimizeNode(input, input.getChild(0))
      println(input.treeString())

      for (String unusedVariable : fields[1].split(",").collect {s -> s.trim()}) {
        assertFalse("Shouldn't have var '$unusedVariable'", hasVar(unusedVariable, input))
      }
    }
  }

  TokenNode generate(String test) {
    analyzer.reset()
    parser.reset()
    return parser.parse(analyzer.toTokens(test))
  }


  private boolean hasVar(String name, TokenNode node) {
    def value = node.value
    if (value && (value == name || (value instanceof Map) && value.name == name)) {
      return true
    }
    for (int i = 0; i < node.nbChildren(); i++) {
      if (hasVar(name, node.getChild(i))) {
        return true
      }
    }
    return false
  }
}
