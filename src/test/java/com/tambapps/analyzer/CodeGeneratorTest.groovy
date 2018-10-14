package com.tambapps.analyzer

import com.tambapps.analyzer.token.Token
import com.tambapps.analyzer.token.TokenNode
import com.tambapps.analyzer.token.TokenNodeType
import com.tambapps.analyzer.token.TokenType

class CodeGeneratorTest extends GroovyTestCase{

    void testPlusU(){

        List childrenList = [new TokenNode(TokenNodeType.CONSTANT,new Token(1,2,1,TokenType.CONSTANT),[])]
        TokenNode tokPlusU = new TokenNode(TokenNodeType.PLUS_U, new Token(1,1,TokenType.PLUS),childrenList)
        String codeExpected = ".start\npush.i 0\npush.i 1\nadd.i\nout.i\npush.i 10\nout.c\nhalt\n"
        CodeGenerator codeGen = new CodeGenerator()
        assertEquals(codeExpected,codeGen.compile(tokPlusU))

    }

    void testPlusB(){

        List childrenList = [new TokenNode(TokenNodeType.CONSTANT,new Token(1,1,1,TokenType.CONSTANT),[]), new TokenNode(TokenNodeType.CONSTANT,new Token(1,3,2,TokenType.CONSTANT),[])]
        TokenNode tokPlusB = new TokenNode(TokenNodeType.PLUS_B, new Token(1,2,TokenType.PLUS),childrenList)
        String codeExpected = ".start\npush.i 1\npush.i 2\nadd.i\nout.i\npush.i 10\nout.c\nhalt\n"
        CodeGenerator codeGen = new CodeGenerator()
        assertEquals(codeExpected,codeGen.compile(tokPlusB))

    }

    void testMoinsU(){

        List childrenList = [new TokenNode(TokenNodeType.CONSTANT,new Token(1,1,1,TokenType.CONSTANT),[])]
        TokenNode tokMoinsU = new TokenNode(TokenNodeType.MINUS_U, new Token(1,2,TokenType.MINUS),childrenList)
        String codeExpected = ".start\npush.i 0\npush.i 1\nsub.i\nout.i\npush.i 10\nout.c\nhalt\n"
        CodeGenerator codeGen = new CodeGenerator()
        assertEquals(codeExpected,codeGen.compile(tokMoinsU))

    }

    void testMoinsB(){

        List childrenList = [new TokenNode(TokenNodeType.CONSTANT,new Token(1,1,1,TokenType.CONSTANT),[]), new TokenNode(TokenNodeType.CONSTANT,new Token(1,3,2,TokenType.CONSTANT),[])]
        TokenNode tokMoinsB = new TokenNode(TokenNodeType.MINUS_B, new Token(1,2,TokenType.MINUS),childrenList)
        String codeExpected = ".start\npush.i 1\npush.i 2\nsub.i\nout.i\npush.i 10\nout.c\nhalt\n"
        CodeGenerator codeGen = new CodeGenerator()
        assertEquals(codeExpected,codeGen.compile(tokMoinsB))

    }

    void testMul(){

        List childrenList = [new TokenNode(TokenNodeType.CONSTANT,new Token(1,1,1,TokenType.CONSTANT),[]), new TokenNode(TokenNodeType.CONSTANT,new Token(1,3,2,TokenType.CONSTANT),[])]
        TokenNode tokMul = new TokenNode(TokenNodeType.MULTIPLY, new Token(1,2,TokenType.MULTIPLY),childrenList)
        String codeExpected = ".start\npush.i 1\npush.i 2\nmul.i\nout.i\npush.i 10\nout.c\nhalt\n"
        CodeGenerator codeGen = new CodeGenerator()
        assertEquals(codeExpected,codeGen.compile(tokMul))

    }

    void testDiv(){

        List childrenList = [new TokenNode(TokenNodeType.CONSTANT,new Token(1,1,1,TokenType.CONSTANT),[]), new TokenNode(TokenNodeType.CONSTANT,new Token(1,3,2,TokenType.CONSTANT),[])]
        TokenNode tokDiv = new TokenNode(TokenNodeType.DIVIDE, new Token(1,2,TokenType.DIVIDE),childrenList)
        String codeExpected = ".start\npush.i 1\npush.i 2\ndiv.i\nout.i\npush.i 10\nout.c\nhalt\n"
        CodeGenerator codeGen = new CodeGenerator()
        assertEquals(codeExpected,codeGen.compile(tokDiv))
    }

    void testMod(){

        List childrenList = [new TokenNode(TokenNodeType.CONSTANT,new Token(1,1,1,TokenType.CONSTANT),[]), new TokenNode(TokenNodeType.CONSTANT,new Token(1,3,2,TokenType.CONSTANT),[])]
        TokenNode tokMod = new TokenNode(TokenNodeType.MODULO, new Token(1,2,TokenType.MODULO),childrenList)
        String codeExpected = ".start\npush.i 1\npush.i 2\nmod.i\nout.i\npush.i 10\nout.c\nhalt\n"
        CodeGenerator codeGen = new CodeGenerator()
        assertEquals(codeExpected,codeGen.compile(tokMod))
    }

}
