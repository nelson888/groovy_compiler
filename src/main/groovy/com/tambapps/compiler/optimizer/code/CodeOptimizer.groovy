package com.tambapps.compiler.optimizer.code

class CodeOptimizer {

  void optimize(List<String> lines) {
    for (int i = 0; i < lines.size() - 2; i++) {
      def frame = [lines.get(i), lines.get(i + 1), lines.get(i + 2)]
      if (frame[0].startsWith("dup") && frame[2].startsWith("drop")) {
        lines.remove(i + 2)
        lines.remove(i)
        i-= 2
      }
    }
  }

}
