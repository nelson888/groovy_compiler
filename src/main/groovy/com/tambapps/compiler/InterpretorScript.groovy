package com.tambapps.compiler

import com.tambapps.compiler.eval.Interpreter

if (args.length == 0) {
  println 'You must provide one file to run'
  return
}
String filePath = args[0]
File file = new File(filePath)
if (!file.exists()) {
  println("The file with path $filePath doesn't exits")
  return
}
Interpreter interpreter = new Interpreter()
interpreter.interpret(file.text, this.&println)