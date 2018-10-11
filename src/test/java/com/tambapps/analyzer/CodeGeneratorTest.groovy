package com.tambapps.analyzer

import com.tambapps.analyzer.token.Token
import com.tambapps.analyzer.token.TokenNode
import com.tambapps.analyzer.token.TokenNodeType
import com.tambapps.analyzer.token.TokenType

class CodeGeneratorTest extends GroovyTestCase{

    void testPlusU(){

        List childrenList = [new TokenNode(TokenNodeType.CONSTANT,new Token(1,1,1,TokenType.CONSTANT),[])]
        TokenNode tokPlusU = new TokenNode(TokenNodeType.PLUS_U, new Token(1,1,TokenType.PLUS),childrenList)
        String codeExpected = ".start\npush.i 0\npush.i 1\nadd.i\nout.i\npush.i 10\nout.c\nhalt\n"
        CodeGenerator codeGen = new CodeGenerator()
        assertEquals(codeExpected,codeGen.compile(tokPlusU))

    }

}
