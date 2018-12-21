package com.tambapps.compiler.eval

import static com.tambapps.compiler.analyzer.token.TokenUtils.OPERATOR_MAP

import com.tambapps.compiler.analyzer.token.TokenNode
import com.tambapps.compiler.analyzer.token.TokenNodeType
import com.tambapps.compiler.exception.PointerException
import com.tambapps.compiler.util.DequeMap
import com.tambapps.compiler.util.Symbol
//TODO ERROR WITH ARRAYS
class Evaluator {

  private final List<TokenNode> functions
  private final Closure printer
  private DequeMap dequeMap
  private Integer returnValue = null
  private int nbSlot = 0


  Evaluator(List<TokenNode> functions, Closure printer) {
    this.functions = functions
    this.printer = printer
    dequeMap = new DequeMap()
  }

  Evaluator(List<TokenNode> functions) {
    this(functions, System.out.&println)
  }

  void process(TokenNode node) throws PointerException {
    switch (node.type) {
      case TokenNodeType.VAR_DECL:
        Symbol s = dequeMap.newSymbol(node.value)
        s.slot = nbSlot++
        break
      case TokenNodeType.TAB_DECL: //1st ident, 2nd size
        String tabName = node.getChild(0).value
        Symbol s = dequeMap.newSymbol(tabName)
        s.slot = nbSlot++
        int size = evaluate(node.getChild(1))
        for (int i = 0; i < size; i++) {
          s = dequeMap.newSymbol(i + tabName)
          s.slot = nbSlot++
        }
        break
      case TokenNodeType.BLOC:
        dequeMap.newBlock()
        for (int i = 0; i < node.nbChildren(); i++) {
          process(node.getChild(i))
        }
        dequeMap.endBlock()
        break
      case TokenNodeType.ASSIGNMENT:
        TokenNode left = node.getChild(0)
        Symbol s
        int value = evaluate(node.getChild(1))
        if (left.type == TokenNodeType.D_REF) {
          s = dequeMap.findSymbol(left.getChild(0).value)
          if (s.slot > nbSlot) {
            throw new PointerException("Pointed variable with address $s.value doesn't exist")
          }
          s = dequeMap.findSymbolWithSlot(s.value)
        } else if (left.type == TokenNodeType.TAB_REF) {
          int index = evaluate(node.getChild(0)) + 1 //because 0 is the tab variable itself
          s = dequeMap.findSymbol(node.value)
          if (s.slot + index > nbSlot) {
            throw new PointerException("Tried to access element $index of array $e.value")
          }
          s = dequeMap.findSymbolWithSlot(s.slot + index)
        } else {
          s = dequeMap.findSymbol(left.value)
        }
        s.value = value
        break
      case TokenNodeType.COND:
        TokenNode condition = node.getChild(0)
        int test =  evaluate(condition)
        if (test) { //non zero numbers are truthy
          process(node.getChild(1))
        } else if (node.nbChildren() > 2) {
          process(node.getChild(2))
        }
        break
      case TokenNodeType.LOOP:
        TokenNode condNode = node.getChild(0)
        TokenNode testNode = condNode.getChild(0)
        int test = evaluate(testNode)
        while (test) {
          process(condNode.getChild(1))
          test = evaluate(testNode)
        }
        break
      case TokenNodeType.RETURN:
        if (node.nbChildren() > 0) {
          TokenNode returnExpression = node.getChild(0)
          returnValue = evaluate(returnExpression)
        }
        break
      case TokenNodeType.FUNCTION_CALL: //like a procedure call
        evaluate(node)
        break
      case TokenNodeType.PRINT:
        printer(evaluate(node.getChild(0)))
        break
      case TokenNodeType.SEQ:
        for (int i = 0; i < node.nbChildren(); i++) {
          process(node.getChild(i))
        }
        break
      default:
        for (int i = 0; i < node.nbChildren(); i++) {
          process(node.getChild(i))
        }
    }
  }

  private Integer evaluate(TokenNode e) { //evaluates an expression
    if (e.type.isUnaryOperator()) {
      def arg = evaluate(e.getChild(0))
      return OPERATOR_MAP.get(e.type).call(arg)
    } else if (e.type.binaryOperator) {
      def arg1 = evaluate(e.getChild(0))
      def arg2 = evaluate(e.getChild(1))
      return OPERATOR_MAP.get(e.type).call(arg1, arg2)
    }
    switch (e.type) {
      case TokenNodeType.CONSTANT:
        return e.value
      case TokenNodeType.VAR_REF:
        return dequeMap.findSymbol(e.value).value
      case TokenNodeType.D_REF:
        return dequeMap.findSymbol(e.getChild(0).value).slot
      case TokenNodeType.TAB_REF:
        int index = evaluate(e.getChild(0)) + 1 //because 0 is the tab variable itself
        Symbol s = dequeMap.findSymbol(e.value)
        if (s.slot + index > nbSlot) {
          throw new PointerException("Tried to access element $index of array $e.value")
        }
        Symbol pointedSymbol = dequeMap.findSymbolWithSlot(s.slot + index)
        return pointedSymbol.value

      /*
      case TokenNodeType.INCREMENT:
      case TokenNodeType.DECREMENT:
        int arg1 = evaluate(e.getChild(0));
        Symbol s = dequeMap.findSymbol(e.value)
        return s.value++;*/
      case TokenNodeType.FUNCTION_CALL:
        Symbol funcData = dequeMap.findSymbol(e.value)
        TokenNode function = functions.find({ f -> funcData == f.value })
        Evaluator evaluator = new Evaluator(functions, printer)
        List<Symbol> arguments = new ArrayList<>()
        for (int i = 0; i < funcData.nbArgs; i++) {
          Symbol argument = dequeMap.findSymbol(function.getChild(i).value).copy()
          argument.value = evaluate(e.getChild(i))
          arguments.add(argument)
        }
        evaluator.initVariables(arguments)
        evaluator.process(function.getChild(function.nbChildren() - 1)) //skip variable declarations
        return evaluator.getReturnValue()

      default:
        throw new RuntimeException("This shouldn't happen")
    }
  }

  private void initVariables(List<Symbol> symbols) { //for function call
    for (Symbol symbol : symbols) {
      dequeMap.newSymbol(symbol)
    }
  }

  Integer getReturnValue() {
    return returnValue
  }

  private power(int a, int n) {
    if (n == 0) return 1
    if (n == 1) return a
    if (n % 2 == 0) {
      int p = power(a, n / 2)
      return p * p
    }
    return a * power(a, n - 1)
  }

}
