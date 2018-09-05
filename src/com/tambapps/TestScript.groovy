package com.tambapps

import com.tambapps.analyzer.LexicalAnalyzer

LexicalAnalyzer lexicalAnalyzer = new LexicalAnalyzer()

println lexicalAnalyzer.toTokens("while (a) do {b;}") //TODO doesn't work
println lexicalAnalyzer.toTokens("1+2+3+4") //TODO doesn't work
