package com.tambapps

import com.tambapps.analyzer.CodeGenerator
import com.tambapps.analyzer.LexicalAnalyzer
import com.tambapps.analyzer.Parser
import com.tambapps.analyzer.token.Token
import com.tambapps.analyzer.token.TokenNode

if (args.length <= 0) {
    println 'You must provide a file to compile'
    return
}


LexicalAnalyzer lexicalAnalyzer = new LexicalAnalyzer()
List<Token> tokens
try {
    tokens = lexicalAnalyzer.toTokens(new File(args[0]))
} catch(RuntimeException e) {
    println("Error")
    println("$e.message")
    return
}

println "Tokens:"
println tokens

Parser parser = new Parser(tokens)

TokenNode tree
try {
    tree = parser.parse()
} catch(RuntimeException e) {
    println("Error")
    println("$e.message")
    return
}

println "TokenNode tree"
println tree.treeString()

CodeGenerator codeGenerator = new CodeGenerator()
String code  = codeGenerator.compile(tree)
println "code generated:"
println code
File file = new File(args[0] + '.bin')
file.bytes = []
file << code