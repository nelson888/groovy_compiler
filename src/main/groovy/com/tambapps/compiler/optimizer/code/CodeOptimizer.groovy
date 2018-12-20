package com.tambapps.compiler.optimizer.code

import com.tambapps.compiler.analyzer.CodeGenerator

class CodeOptimizer {

  void optimize(List<String> lines) {
    for (int i = 0; i < lines.size() - 2; i++) {
      def frame = [lines.get(i), lines.get(i + 1), lines.get(i + 2)]
      if (frame[0].startsWith(CodeGenerator.DUP) && frame[2].startsWith(CodeGenerator.DROP)) {
        lines.remove(i + 2)
        lines.remove(i)
        i-= 2
      }
    }
  }

}
