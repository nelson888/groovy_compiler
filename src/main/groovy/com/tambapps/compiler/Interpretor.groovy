package com.tambapps.compiler

import com.tambapps.compiler.analyzer.LexicalAnalyzer
import com.tambapps.compiler.analyzer.Parser
import com.tambapps.compiler.analyzer.SemanticAnalyzer
import com.tambapps.compiler.analyzer.token.Token
import com.tambapps.compiler.analyzer.token.TokenNode
import com.tambapps.compiler.eval.Evaluator
import com.tambapps.compiler.exception.LexicalException
import com.tambapps.compiler.exception.ParsingException
import com.tambapps.compiler.exception.PointerException
import com.tambapps.compiler.exception.SemanticException

if (args.length <= 0) {
  println 'You must provide at least one file to run'
  return
}

lexicalAnalyzer = new LexicalAnalyzer()
parser = new Parser()
semanticAnalyzor = new SemanticAnalyzer()

String filePath = args[0]
File file = new File(filePath)
if (!file.exists()) {
  println("The file with path $filePath doesn't exits")
  return
}

try {
  interpret(file)
} catch (LexicalException e) {
  println('Error while performing lexical analysis')
  println("$e.message")
} catch (ParsingException e) {
  println('Error while performing parsing')
  println("$e.message")
} catch (SemanticException e) {
  println('Error while performing semantic analysis')
  println("$e.message")
}

println()
lexicalAnalyzer.reset()

void interpret(File file) throws LexicalException, ParsingException, SemanticException, PointerException {
  List<Token> tokens = lexicalAnalyzer.toTokens(file)
  TokenNode tree = parser.parse(tokens)
  semanticAnalyzor.process(tree)
  List<TokenNode> functions = new ArrayList<>()
  TokenNode main = null
  for (int i = 0; i < tree.nbChildren(); i++) {
    TokenNode function = tree.getChild(i)
    if (function.value.name.equals("main")) {
      main = function
    } else {
      functions.add(function)
    }
  }
  if (!main) {
    println("Doesn't have a main function!!!")
    return
  }
  Evaluator evaluator = new Evaluator(functions)
  try {
    evaluator.process(main)
  } catch (PointerException e) {
    println("Pointer exception:\n$e.message")
  }

}

