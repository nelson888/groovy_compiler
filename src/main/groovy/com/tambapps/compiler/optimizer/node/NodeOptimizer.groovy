package com.tambapps.compiler.optimizer.node

import com.tambapps.compiler.analyzer.token.TokenNode
import com.tambapps.compiler.analyzer.token.TokenNodeType
import groovy.transform.PackageScope

@PackageScope
interface NodeOptimizer {
  boolean isOptimizable(TokenNodeType type)
  void optimizeNode(TokenNode parent, TokenNode node)
}