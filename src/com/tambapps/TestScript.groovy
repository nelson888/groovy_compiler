package com.tambapps

import com.tambapps.analyzor.LexicalAnalyzer

LexicalAnalyzer lexicalAnalyzer = new LexicalAnalyzer()

println lexicalAnalyzer.toTokens("aa + 2 - 45 + zeze")
