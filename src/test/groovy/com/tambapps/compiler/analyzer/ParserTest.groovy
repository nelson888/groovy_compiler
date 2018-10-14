package com.tambapps.compiler.analyzer

import com.tambapps.compiler.analyzer.token.Token
import com.tambapps.compiler.analyzer.token.TokenNode
import com.tambapps.compiler.analyzer.token.TokenNodeType
import com.tambapps.compiler.analyzer.token.TokenType
import com.tambapps.compiler.exception.ParsingException

import java.util.stream.Collectors
import java.util.stream.Stream

class ParserTest extends GroovyTestCase {

    private final LexicalAnalyzer lexicalAnalyzer = new LexicalAnalyzer() //assuming that Lexical Analyzer works properly
    private final Parser parser = new Parser()
    private static final Token EOF = Token.of(TokenType.END_OF_FILE, 0, 1)

    void testParseSingleToken() {
        Token t = Token.of(TokenType.CONSTANT, 0, 0)
        assertEquals('Should be equal', of(TokenNodeType.CONSTANT), parser.parse([t, EOF]))
    }

    void testExpressions() {
        List<TokenNode> expected = [
            of(TokenNodeType.PLUS_B).withChildren(of(TokenNodeType.CONSTANT), of(TokenNodeType.CONSTANT)),
            of(TokenNodeType.MINUS_B).withChildren(of(TokenNodeType.CONSTANT), of(TokenNodeType.MINUS_U).withChildren(of(TokenNodeType.CONSTANT))),
            of(TokenNodeType.MULTIPLY).withChildren(of(TokenNodeType.MINUS_U).withChildren(of(TokenNodeType.CONSTANT)), of(TokenNodeType.PLUS_B).withChildren(of(TokenNodeType.CONSTANT), of(TokenNodeType.CONSTANT))),
            of(TokenNodeType.POWER).withChildren(of(TokenNodeType.CONSTANT), of(TokenNodeType.POWER).withChildren(of(TokenNodeType.CONSTANT), of(TokenNodeType.CONSTANT))),
            of(TokenNodeType.STRICT_SUP).withChildren(of(TokenNodeType.CONSTANT), of(TokenNodeType.CONSTANT)),
            of(TokenNodeType.STRICT_INF).withChildren(of(TokenNodeType.CONSTANT), of(TokenNodeType.CONSTANT)),
            of(TokenNodeType.SUP).withChildren(of(TokenNodeType.CONSTANT), of(TokenNodeType.CONSTANT)),
            of(TokenNodeType.INF).withChildren(of(TokenNodeType.CONSTANT), of(TokenNodeType.CONSTANT)),
            of(TokenNodeType.EQUAL).withChildren(of(TokenNodeType.CONSTANT), of(TokenNodeType.CONSTANT)),
            of(TokenNodeType.NOT_EQUAL).withChildren(of(TokenNodeType.CONSTANT), of(TokenNodeType.NOT).withChildren(of(TokenNodeType.CONSTANT))),
            of(TokenNodeType.NOT).withChildren(of(TokenNodeType.NOT).withChildren(of(TokenNodeType.NOT).withChildren(of(TokenNodeType.CONSTANT))))
        ]

        performTest(TestsHolder.EXPRESSION_TESTS, expected)
    }

    void testExpressionErrors() {
        List<List<Token>> tests = Stream.of(
            '123 435',
            '43^^4',
            '1234 + *45',
            '*45',
            '1234 + /',
            '1234 + ',
        )
        .map({ s ->
            lexicalAnalyzer.reset()
            lexicalAnalyzer.toTokens(s)
        }).collect(Collectors.toList())

        performErrorTest(tests)
    }

    void performTest(tests, expectedList) {
        for (int i = 0; i < tests.size(); i++) {
            String test = tests[i]
            TokenNode expected = expectedList[i]
            List<Token> tokens = lexicalAnalyzer.toTokens(test)
            TokenNode result = parser.parse(tokens)
            assertTrue("Failed test $i, Input: $test\n" +
                "Expected:\n" + expected.treeString() + "\n" +
                "Got:\n" + result.treeString()
                , expected == result)
            lexicalAnalyzer.reset()
        }
    }

    void performErrorTest(List<List<Token>> tests) {
        for (int i = 0; i < tests.size(); i++) {
            List<Token> test = tests[i]
            try {
                TokenNode node = parser.parse(test)
                fail("An Exception should have been thrown for test $i: $test\nBut got: " + node.treeString())
            } catch (ParsingException ignored) {

            }
        }
    }

    private static TokenNode of(TokenNodeType type) {
        return new TestTokenNode(type)
    }

    private static class TestTokenNode extends TokenNode {

        TestTokenNode(TokenNodeType type) {
            super(EOF, type) //we're only interested in
        }

        @Override
        boolean equals(Object o) {
            if (!(o instanceof TokenNode)) return false
            for (int i = 0; i < nbChildren(); i++) {
                if (o.getChild(i) == getChild(i)) return false
            }
            return type == o.type && value == o.value
        }
    }
}
