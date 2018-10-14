package com.tambapps.compiler

import com.tambapps.compiler.analyzer.CodeGenerator
import com.tambapps.compiler.analyzer.LexicalAnalyzer
import com.tambapps.compiler.analyzer.Parser
import com.tambapps.compiler.analyzer.token.Token
import com.tambapps.compiler.analyzer.token.TokenNode
import com.tambapps.compiler.exception.LexicalException
import com.tambapps.compiler.exception.ParsingException

if (args.length <= 0) {
    println 'You must provide at least file to compile'
    return
}

lexicalAnalyzer = new LexicalAnalyzer()
parser = new Parser()
codeGenerator = new CodeGenerator()

for (String fileName : args) {
    compile(fileName)
    lexicalAnalyzer.reset()
    codeGenerator.reset()

}

void compile(String fileName) {
    List<Token> tokens
    try {
        tokens = lexicalAnalyzer.toTokens(new File(fileName))
    } catch(LexicalException e) {
        println('Error while performing lexical analysis')
        println("$e.message")
        return
    }

    TokenNode tree
    try {
        tree = parser.parse(tokens)
    } catch(ParsingException e) {
        println('Error while performing parsing')
        println("$e.message")
        return
    }

    String code = codeGenerator.compile(tree)

    File file = new File(fileName + '.code')
    file.bytes = code.getBytes()

    println("Compiled successfully")
}

