package com.tambapps.compiler.optimizer.code

import com.tambapps.compiler.analyzer.CodeGenerator

class CodeOptimizer {

  String optimize(String code) {
    List<String> lines = code.split('\\r?\\n')
    optimizeLines(lines)
    return lines.inject { String result, String line -> result + '\n' + line }
  }

  private void optimizeLines(List<String> lines) {
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
