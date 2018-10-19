package com.tambapps.compiler.analyzer

import com.tambapps.compiler.analyzer.token.TokenNode
import com.tambapps.compiler.analyzer.token.TokenNodeType

import java.util.concurrent.LinkedBlockingDeque

class CodeGenerator {

  private static final Map<TokenNodeType, String> COMMAND_MAP
  static {
    Map<TokenNodeType, String> commandMap = new HashMap<>()
    commandMap.put(TokenNodeType.PLUS_U, "add.i")
    commandMap.put(TokenNodeType.PLUS_B, "add.i")
    commandMap.put(TokenNodeType.MINUS_U, "sub.i")
    commandMap.put(TokenNodeType.MINUS_B, "sub.i")
    commandMap.put(TokenNodeType.MULTIPLY, "mul.i")
    commandMap.put(TokenNodeType.DIVIDE, "div.i")
    commandMap.put(TokenNodeType.MODULO, "mod.i")
    commandMap.put(TokenNodeType.NOT, "not")
    commandMap.put(TokenNodeType.AND, "and")
    commandMap.put(TokenNodeType.OR, "or")
    commandMap.put(TokenNodeType.EQUAL, "cmpeq.i")
    commandMap.put(TokenNodeType.NOT_EQUAL, "cmpne.i")
    commandMap.put(TokenNodeType.STRICT_INF, "cmplt.i")
    commandMap.put(TokenNodeType.STRICT_SUP, "cmpgt.i")
    commandMap.put(TokenNodeType.INF, "cmple.i")
    commandMap.put(TokenNodeType.SUP, "cmpge.i")
    commandMap.put(TokenNodeType.STRICT_INF, "cmplt.i")
    // commandMap.put(TokenNodeType.POWER, CodeGenerator.class.getResourceAsStream("pow.txt").getText())

    COMMAND_MAP = Collections.unmodifiableMap(commandMap)
  }

  private final StringBuilder builder
  private final Deque<Integer> loopExitDeque = new LinkedBlockingDeque<>()
  private int labels = 0

  CodeGenerator() {
    builder = new StringBuilder()
  }

  String compile(TokenNode node, int nbslot) {
    println(".start")
    for (int i = 0; i < nbslot; i++) {
      println("push.i 0")
    }
    genCode(node)

    println("halt")
    return builder.toString()
  }

  private void genCode(TokenNode node) {
    TokenNodeType t = node.type

    if (t.isBinaryOperator() && t != TokenNodeType.POWER) { //TODO POWER NOT HANDLED YET
      genCode(node.getChild(0))
      genCode(node.getChild(1))
      println(COMMAND_MAP.get(t))
      return
    } else if (t.isUnaryOperator()) {
      println("push.i 0")
      genCode(node.getChild(0))
      println(COMMAND_MAP.get(t))
    }

    switch (t) {
      case TokenNodeType.PROG:
      case TokenNodeType.SEQ:
        for (int i = 0; i < node.nbChildren(); i++) {
          genCode(node.getChild(i))
        }
        break
      case TokenNodeType.CONSTANT:
        println("push.i $node.value")
        break
      case TokenNodeType.DROP:
        genCode(node.getChild(0))
        println("drop")
        break
      case TokenNodeType.VAR_REF:
        println("get $node.value.index")
        break
      case TokenNodeType.ASSIGNMENT:
        genCode(node.getChild(1))
        println("dup")
        TokenNode nodeChild = node.getChild(0)
        println("set $nodeChild.value.index")
        break
      case TokenNodeType.BLOC:
        for (int i = 0; i < node.nbChildren(); i++) {
          genCode(node.getChild(i))
        }
        break
      case TokenNodeType.COND:
        if (node.nbChildren() == 2) { //if without else
          int l = labels++
          genCode(node.getChild(0))
          println("jumpf l$l")
          genCode(node.getChild(1))
          println(".l$l")
        } else { //if with else
          int l1 = labels++
          int l2 = labels++
          genCode(node.getChild(0))
          println("jumpf l$l1")
          genCode(node.getChild(1)) //print body
          println("jump l$l2")
          println(".l$l1")
          genCode(node.getChild(2))
          println(".l$l2")
        }
        break
      case TokenNodeType.PRINT:
        genCode(node.getChild(0))
        println("out.i")
        println("push.i 10")
        println("out.c")
        break
      case TokenNodeType.BREAK:
        println("jump l${loopExitDeque.removeLast()}")
        break
      case TokenNodeType.LOOP:
        int lStart = labels++
        int lExit = labels++
        loopExitDeque.push(lExit)
        println(".l$lStart")
        genCode(node.getChild(0))
        println("jump l$lStart")
        println(".l$lExit") //loop exit
    }
  }

  private void println(String s) {
    builder.append(s).append('\n')
  }

  void reset() {
    builder.setLength(0)
  }
}
