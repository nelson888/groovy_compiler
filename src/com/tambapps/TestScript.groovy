package com.tambapps

import com.tambapps.analyzer.LexicalAnalyzer
import com.tambapps.analyzer.SyntaxicAnalyzer
import com.tambapps.analyzer.token.Token

LexicalAnalyzer lexicalAnalyzer = new LexicalAnalyzer()
List<Token> tokens = lexicalAnalyzer.toTokens("+-+-2")
println "Tokens:"
println tokens

SyntaxicAnalyzer syntaxicAnalyzer = new SyntaxicAnalyzer(tokens)

println "TokenNode tree"
println syntaxicAnalyzer.atome().treeString()

/*
println lexicalAnalyzer.toTokens("while(a){b=4+a-23;}")
lexicalAnalyzer.reset()
println lexicalAnalyzer.toTokens("while (a)\n{\n b = 4 + a - 23 ;\n}")
*/