package com.tambapps.analyzer.token

class TokenNode extends AbstractToken {

    final TokenNodeType type
    private final List<TokenNode> children = new ArrayList<>(3)

    TokenNode(Token token, TokenNodeType type) {
        super(token.l, token.c, token.value)
        this.type = type
    }

    TokenNode(Token token) {
        this(token, TokenUtils.TYPE_MAP.get(token.type))
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
        treeStringBuilder(builder, this,  0, new HashSet<Integer>())
        return builder.toString()
    }

    private static void treeStringBuilder(StringBuilder builder, TokenNode node, int l, Collection<Integer> branchesIndex) {
        for (int i = 0; i < 2*l; i++) {
            builder.append('_')
        }
        builder.append(node.toString())
                .append('\n')
        int children = node.nbChildren()
        int nbSpaces = 2 * ( l + l * 2 + (l * (l - 1))/2) as int //magic formula to print well
        branchesIndex.add(nbSpaces)
        if (children > 0) {
            for (int i = 0; i < children; i++) {
                for (int j = 0; j < nbSpaces; j++) {
                    if (j in branchesIndex) {
                        builder.append('|')
                    } else {
                        builder.append(' ')
                    }
                }
                builder.append('|')
                if (i == children - 1) {
                    branchesIndex.remove(nbSpaces)
                }
                treeStringBuilder(builder, node.getChild(i), l + 1, branchesIndex)
            }
        }
    }

}
