package com.tambapps.compiler

import com.tambapps.compiler.exception.LexicalException
import com.tambapps.compiler.exception.ParsingException
import com.tambapps.compiler.exception.SemanticException

if (args.length <= 0) {
  println 'You must provide at least one file to compile'
  return
}

Compiler compiler = new Compiler()

for (String filePath : args) {
  File file = new File(filePath)
  if (!file.exists()) {
    println("The file with path $filePath doesn't exits")
    continue
  }
  println("Compiling $file.name...")
  try {
    String code = compiler.compile(file)
    File compiled = new File(file.parentFile, file.name + '.code')
    compiled.bytes = code.getBytes()
    println("Compiled successfully")
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
}

