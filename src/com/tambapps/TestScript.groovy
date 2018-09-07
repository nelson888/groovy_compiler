package com.tambapps

import com.tambapps.analyzer.LexicalAnalyzer
import com.tambapps.analyzer.Parser
import com.tambapps.analyzer.token.Token

LexicalAnalyzer lexicalAnalyzer = new LexicalAnalyzer()

List<Token> tokens = lexicalAnalyzer.toTokens("4 + 4 + 4 + 4")
println "Tokens:"
println tokens

Parser parser = new Parser(tokens)

println "TokenNode tree"
tree = parser.parse()
println tree.treeString()

/*
println lexicalAnalyzer.toTokens("while(a){b=4+a-23;}")
lexicalAnalyzer.reset()
println lexicalAnalyzer.toTokens("while (a)\n{\n b = 4 + a - 23 ;\n}")
*/