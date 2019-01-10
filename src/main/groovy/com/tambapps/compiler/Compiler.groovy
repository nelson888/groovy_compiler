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

class Compiler {

  private lexicalAnalyzer = new LexicalAnalyzer()
  private parser = new Parser()
  private codeGenerator = new CodeGenerator()
  private semanticAnalyzor = new SemanticAnalyzer()

  String compile(String codeInput) throws LexicalException, ParsingException, SemanticException {
    List<Token> tokens = lexicalAnalyzer.toTokens(codeInput)
    TokenNode tree = parser.parse(tokens)
    semanticAnalyzor.process(tree)
    try {
      return codeGenerator.compile(tree)
    } finally {
      lexicalAnalyzer.reset()
      parser.reset()
      codeGenerator.reset()
    }
  }


  String compile(File file) throws LexicalException, ParsingException, SemanticException {
    return compile(file.getText())
  }

}