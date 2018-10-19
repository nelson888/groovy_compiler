package com.tambapps.compiler.analyzer

import com.tambapps.compiler.analyzer.token.TokenNode
import com.tambapps.compiler.analyzer.token.TokenNodeType

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

    CodeGenerator() {
        builder = new StringBuilder()
    }

    String compile(TokenNode node) {
        println(".start")
        println("push.i 0")
        genCode(node)
        println("out.i")
        println("push.i 10")
        println("out.c")
        println("halt")
        return builder.toString()
    }

    private void genCode(TokenNode node) {
        TokenNodeType t = node.type
        if (t == TokenNodeType.PROG){
            for(int i = 0; i<node.nbChildren();i++){
                genCode(node.getChild(i))
            }
        } else if (t == TokenNodeType.CONSTANT) {
            println("push.i $node.value")
        } else if (t.isUnaryOperator()) {
            println("push.i 0")
            genCode(node.getChild(0))
            println(COMMAND_MAP.get(t))
        } else if (t.isBinaryOperator() && t != TokenNodeType.POWER) {
            genCode(node.getChild(0))
            genCode(node.getChild(1))
            println(COMMAND_MAP.get(t))
        } else if (t == TokenNodeType.DROP) {
            genCode(node.getChild(0))
            println("drop")
        } else if (t == TokenNodeType.VAR_REF){
            println("get $node.value.index")
        } else if (t == TokenNodeType.ASSIGNMENT){
            genCode(node.getChild(1))
            println("dup")
            TokenNode nodeChild = node.getChild(0)
            println("set $nodeChild.value.index")
        }
    }

    private void println(String s) {
        builder.append(s).append('\n')
    }

  void reset() {
    builder.setLength(0)
  }
}
