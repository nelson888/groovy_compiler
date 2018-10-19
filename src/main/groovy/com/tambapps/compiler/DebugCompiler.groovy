package com.tambapps.compiler

import com.tambapps.compiler.analyzer.CodeGenerator
import com.tambapps.compiler.analyzer.LexicalAnalyzer
import com.tambapps.compiler.analyzer.Parser
import com.tambapps.compiler.analyzer.SementicAnalyzer
import com.tambapps.compiler.analyzer.token.Token
import com.tambapps.compiler.analyzer.token.TokenNode
import com.tambapps.compiler.exception.LexicalException
import com.tambapps.compiler.exception.ParsingException

if (args.length <= 0) {
    println 'You must provide at least one file to compile'
    return
}

lexicalAnalyzer = new LexicalAnalyzer()
parser = new Parser()
codeGenerator = new CodeGenerator()
sementicAnalyzor = new SementicAnalyzer()

for (String filePath : args) {
    File file = new File(filePath)
    if (!file.exists()) {
        println("The file with path $filePath doesn't exits")
        continue
    }
    println("Compiling $file.name...")
    try {
        compile(file)
        println("Compiled successfully")
    } catch (LexicalException e) {
        println('Error while performing lexical analysis')
        println("$e.message")
        return
    } catch (ParsingException e) {
        println('Error while performing parsing')
        println("$e.message")
    }

    println()
    lexicalAnalyzer.reset()
    codeGenerator.reset()
}

void compile(File file) throws LexicalException, ParsingException {
    String text = file.getText()
    println("Text:\n$text")
    List<Token> tokens = lexicalAnalyzer.toTokens(text)
    println("Token list: $tokens")
    TokenNode tree = parser.parse(tokens)
    println("Token tree:\n${tree.treeString()}")
    String code = codeGenerator.compile(tree, sementicAnalyzor.nbSlot)
    println("Generated code:\n************\n\n$code\n************")
}

