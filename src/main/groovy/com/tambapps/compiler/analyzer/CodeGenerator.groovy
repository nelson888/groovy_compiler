package com.tambapps.compiler.analyzer

import com.tambapps.compiler.analyzer.token.Token
import com.tambapps.compiler.analyzer.token.TokenNode
import com.tambapps.compiler.analyzer.token.TokenNodeType

import java.util.concurrent.LinkedBlockingDeque

class CodeGenerator {

  private static final String POWER_FUNC_NAME = "power"
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

    COMMAND_MAP = Collections.unmodifiableMap(commandMap)
  }

  private final StringBuilder builder
  private final Deque<Integer> loopExitDeque = new LinkedBlockingDeque<>()
  private int labels = 0 // because function power use 3 labels

  CodeGenerator() {
    builder = new StringBuilder()
  }

  String compile(TokenNode node) {
    println(".start")
    println("prep main")
    println("call 0")
    println("halt")
    genCode(node)
    return builder.toString()
  }

  private void genCode(TokenNode node) {
    TokenNodeType t = node.type

    if (t.isBinaryOperator()) {
      if (t != TokenNodeType.POWER) {
        genCode(node.getChild(0))
        genCode(node.getChild(1))
        println(COMMAND_MAP.get(t))
      } else {
        println("prep $POWER_FUNC_NAME")
        genCode(node.getChild(0))
        genCode(node.getChild(1))
        println("call 2")
      }
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
        if (node.getChild(0).type == TokenNodeType.D_REF){
          genCode(node.getChild(0).getChild(0))
          genCode(node.getChild(1))
          println("write")
        } else if (node.getChild(0).type == TokenNodeType.VAR_REF){
          genCode(node.getChild(1))
          println("dup")
          TokenNode nodeChild = node.getChild(0)
          println("set $nodeChild.value.index")
        }
        break
      case TokenNodeType.D_REF:
        genCode(node.getChild(0))
        println("read")
        break
      case TokenNodeType.TAB_REF:
        genCode(node.getChild(0))
        genCode(node.getChild(1))
        println("add.i")
        println("read")
        break
      case TokenNodeType.TAB_DECL:
        println("prep malloc")
        genCode(node.getChild(0))
        println("call 1")
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
        println("jump l${loopExitDeque.pop()}")
        break
      case TokenNodeType.LOOP:
        int lStart = labels++
        int lExit = labels++
        loopExitDeque.push(lExit)
        println(".l$lStart")
        genCode(node.getChild(0))
        println("jump l$lStart")
        println(".l$lExit") //loop exit
        break
      case TokenNodeType.FUNCTION:
        println("."+node.value.name)
        for (int i = 0; i < node.value.nbSlot; i++) {
          println("push.i 0")
        }
        genCode(node.getChild(node.nbChildren()-1))
        println("push.i 0")
        println("ret")
        break
      case TokenNodeType.FUNCTION_CALL:
        println("prep "+node.value.name)
        for (int i = 0; i<node.nbChildren(); i++){
          genCode(node.getChild(i))
        }
        println("call "+node.nbChildren())
        break
      case TokenNodeType.RETURN:
        genCode(node.getChild(0))
        println("ret")
        break
    }
  }

  private void println(String s) {
    builder.append(s).append('\n')
  }

  void reset() {
    builder.setLength(0)
  }
}
