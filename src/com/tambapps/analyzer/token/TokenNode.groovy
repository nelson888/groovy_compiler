package com.tambapps.analyzer.token

class TokenNode extends AbstractToken {

    final TokenNodeType type
    private final List<TokenNode> children = new ArrayList<>(3)

    TokenNode(Token token, TokenNodeType type) {
        super(token.l, token.c, token.value)
        this.type = type
    }

    TokenNode(Token token) {
        this(token, TokenNodeType.TYPE_MAP.get(token.type))
    }

    TokenNode(TokenNodeType type, Token token, List<TokenNode> children) {
        this(token, type)
        this.children.addAll(children)
    }

    void addChild(TokenNode node) {
        this.children.add(node)
    }

    TokenNode getChild(int i) {
        if (i >= children.size()) {
            throw new IndexOutOfBoundsException("Leaf $i doesn't exist")
        }
        return children.get(i)
    }

    int nbChildren() {
        return children.size()
    }

    String treeString() {
        StringBuilder builder = new StringBuilder()
        toStringBuilder(builder, this,  0)
        return builder.toString()
    }

    private static void toStringBuilder(StringBuilder builder, TokenNode node, int l) {
        appenNTimes(builder, l, '__')
        builder.append(node.toString())
                .append('\n')
        appenNTimes(builder, l + l * 2 + (l * (l - 1))/2 as int, '  ') //magic formula to print well
        int children = node.nbChildren()
        if (children > 0) {
            builder.append('|')
            for (int i = 0; i < node.nbChildren(); i++) {
                toStringBuilder(builder, node.getChild(i), l + 1)
            }
        }

    }

    static void appenNTimes(StringBuilder builder, int l, String s) {
        for (int i = 0; i < l; i++) {
            builder.append(s)
        }
    }
}
