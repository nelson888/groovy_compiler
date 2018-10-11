package com.tambapps.analyzer

import com.tambapps.analyzer.token.TokenNode
import com.tambapps.analyzer.token.TokenNodeType

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
        COMMAND_MAP = Collections.unmodifiableMap(commandMap)
    }

    private final StringBuilder builder

    CodeGenerator() {
        builder = new StringBuilder()
    }

    String compile(TokenNode node) {
        println(".start")
        genCode(node)
        println("out.i")
        println("push.i 10")
        println("out.c")
        println("halt")
        return builder.toString()
    }


    private void genCode(TokenNode node) {
        TokenNodeType t = node.type
        if (t == TokenNodeType.CONSTANT) {
            println("push.i $node.value")
        } else if (t.isUnaryOperator()) {
            println("push.i 0")
            genCode(node.getChild(0))
            println(COMMAND_MAP.get(t))
        } else if (t.isBinaryOperator() && t != TokenNodeType.POWER) {
            genCode(node.getChild(0))
            genCode(node.getChild(1))
            println(COMMAND_MAP.get(t))
        }
    }

    private void println(String s) {
        builder.append(s).append('\n')
    }

}
