package com.tambapps.compiler.optimizer.code

class CodeOptimizer {

  void optimize(List<String> lines) {
    for (int i = 0; i < lines.size() - 1; i++) {
      String line0 = lines.get(i)
      String line1 = lines.get(i + 1)
      if (line0.startsWith("push.i") && line1.startsWith("drop")) {
        lines.remove(i + 1)
        lines.remove(i)
        i-= 2
      }
    }
  }

}
