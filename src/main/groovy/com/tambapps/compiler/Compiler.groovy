package com.tambapps.compiler

import com.tambapps.compiler.analyzer.CodeGenerator
import com.tambapps.compiler.analyzer.LexicalAnalyzer
import com.tambapps.compiler.analyzer.Parser
import com.tambapps.compiler.analyzer.SemanticAnalyzer
import com.tambapps.compiler.analyzer.token.Token
import com.tambapps.compiler.analyzer.token.TokenNode
import com.tambapps.compiler.exception.LexicalException
import com.tambapps.compiler.exception.ParsingException
import com.tambapps.compiler.exception.SemanticException
import com.tambapps.compiler.optimizer.code.CodeOptimizer
import com.tambapps.compiler.optimizer.node.TokenNodeOptimizer

class Compiler {

  private lexicalAnalyzer = new LexicalAnalyzer()
  private parser = new Parser()
  private codeGenerator = new CodeGenerator()
  private semanticAnalyzer = new SemanticAnalyzer()

  private final TokenNodeOptimizer tkOptimizer
  private final CodeOptimizer codeOptimizer

  Compiler() {
    this(true)
  }

  Compiler(boolean optimize) {
    if (optimize) {
      tkOptimizer = new TokenNodeOptimizer()
      codeOptimizer = new CodeOptimizer()
    } else {
      tkOptimizer = null
      codeOptimizer = null
    }
  }

  String compile(String codeInput) throws LexicalException, ParsingException, SemanticException {
    try {
      List<Token> tokens = lexicalAnalyzer.toTokens(codeInput)
      TokenNode tree = parser.parse(tokens)
      if (tkOptimizer) {
        tkOptimizer.optimize(tree)
      }
      semanticAnalyzer.process(tree)
      String code = codeGenerator.compile(tree)
      if (codeOptimizer) {
        code = codeOptimizer.optimize(code)
      }
      return code
    } finally {
      lexicalAnalyzer.reset()
      parser.reset()
      codeGenerator.reset()
      semanticAnalyzer.reset()
    }
  }


  String compile(File file) throws LexicalException, ParsingException, SemanticException {
    return compile(file.getText())
  }

}