package com.tambapps.compiler.analyzer.token

class TokenNode extends AbstractToken {

  final TokenNodeType type
  private final List<TokenNode> children = new ArrayList<>(3)

  TokenNode(Token token, TokenNodeType type) {
    this(token, type, token.value)
  }

  TokenNode(Token token, TokenNodeType type, def value) {
    super(token.l, token.c, value)
    if (type == null) {
      throw new IllegalArgumentException("$type cannot bu null")
    }
    this.type = type
  }

  TokenNode(Token token) {
    this(token, TokenUtils.TYPE_MAP.get(token.type))
  }

  TokenNode(TokenNodeType type, Token token, List<TokenNode> children) {
    this(token, type)
    this.children.addAll(children)
  }

  TokenNode(TokenNodeType type, int l, int c) {
    super(l, c, null)
    this.type = type
  }

  void addChild(TokenNode node) {
    this.children.add(node)
  }

  void addChildren(TokenNode... nodes) {
    for (TokenNode node : nodes) {
      this.children.add(node)
    }
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
    treeStringBuilder(builder, this, 0, new HashSet<Integer>())
    return builder.toString()
  }

  TokenNode withChildren(TokenNode... nodes) {
    for (TokenNode node : nodes) {
      addChild(node)
    }
    return this
  }

  private static void treeStringBuilder(StringBuilder builder, TokenNode node, int l, Collection<Integer> branchesIndex) {
    for (int i = 0; i < 2 * l; i++) {
      builder.append('_')
    }
    builder.append(node.toString())
        .append('\n')
    int children = node.nbChildren()
    int nbSpaces = 2 * (l + l * 2 + (l * (l - 1)) / 2) as int //magic formula to print well
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

  boolean equals(o) {
    if (this.is(o)) return true
    if (!(o instanceof TokenNode)) return false
    if (!Object.equals(o)) return false

    TokenNode tokenNode = (TokenNode) o

    if (children != tokenNode.children) return false
    if (type != tokenNode.type) return false

    return true
  }

  int hashCode() {
    int result = Object.hashCode()
    result = 31 * result + (type != null ? type.hashCode() : 0)
    result = 31 * result + (children != null ? children.hashCode() : 0)
    return result
  }
}
