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
    println("Error performing lexical analyze")
    println("$e.message")
    return
}

Parser parser = new Parser()

TokenNode tree
try {
    tree = parser.parse(tokens)
} catch(RuntimeException e) {
    println("Error while parsing file:")
    println("$e.message")
    return
}

CodeGenerator codeGenerator = new CodeGenerator()
String code
try {
    code = codeGenerator.compile(tree)
} catch (RuntimeException e) {
    println("Error generating processor code:")
    println("$e.message")
    return
}

File file = new File(args[0] + '.code')
file.bytes = []
file << code

println("Compiled successfully")