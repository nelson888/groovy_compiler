package com.tambapps

import com.tambapps.analyzer.LexicalAnalyzer

LexicalAnalyzer lexicalAnalyzer = new LexicalAnalyzer()

println lexicalAnalyzer.toTokens("while(a){b=4+a-23;}")
lexicalAnalyzer.reset()
println lexicalAnalyzer.toTokens("while (a)\n{\n b = 4 + a - 23 ;\n}")
