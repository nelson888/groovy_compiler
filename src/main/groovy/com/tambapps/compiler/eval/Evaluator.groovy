package com.tambapps.compiler.eval

import com.tambapps.compiler.analyzer.token.TokenNode
import com.tambapps.compiler.analyzer.token.TokenNodeType
import com.tambapps.compiler.exception.PointerException
import com.tambapps.compiler.exception.SemanticException
import com.tambapps.compiler.exception.SymbolException
import com.tambapps.compiler.util.DequeMap
import com.tambapps.compiler.util.Symbol

class Evaluator {

  private final List<TokenNode> functions
  private final Closure printer
  private DequeMap dequeMap
  private Object returnValue = null

  Evaluator(List<TokenNode> functions, Closure printer) {
    this.functions = functions
    this.printer = printer
    dequeMap = new DequeMap()
  }

  Evaluator(List<TokenNode> functions) {
    this(functions, { o -> System.out.println(o) })
  }

  void process(TokenNode node) throws PointerException {
    switch (node.type) {
      case TokenNodeType.VAR_DECL:
        dequeMap.newSymbol(node.value)
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
          try { //find symbol who's slot == s.value and modify its value
            Symbol pointedVariable = dequeMap.findSymbolWithSlot(s.value)
            pointedVariable.value = value
          } catch (SymbolException e) {
            throw new PointerException("Pointed variable doesn't exist")
          }
        } else {
          s = dequeMap.findSymbol(left.value)
          s.value = value
        }
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
      /*
      case TokenNodeType.INCREMENT:
        Symbol s = dequeMap.findSymbol(node.getChild(0).value)
        s.value++
        break
        */
      case TokenNodeType.RETURN:
        if (node.nbChildren() > 0) {
          TokenNode returnExpression = node.getChild(0)
          returnValue = evaluate(returnExpression)
        }
        break
      case TokenNodeType.FUNCTION_CALL:
        evaluate(node) //like a procedure call
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
    switch (e.type) {
      case TokenNodeType.VAR_REF:
        return dequeMap.findSymbol(e.value).value
      case TokenNodeType.PLUS_U:
        return evaluate(e.getChild(0))
      case TokenNodeType.MINUS_U:
        return - evaluate(e.getChild(0))
      case TokenNodeType.MINUS_B:
        return evaluate(e.getChild(0)) - evaluate(e.getChild(1))
      case TokenNodeType.PLUS_B:
        return evaluate(e.getChild(0)) + evaluate(e.getChild(1))

      /*
      case TokenNodeType.INCREMENT:
      case TokenNodeType.DECREMENT:
        arg1 = evaluate(e.getChild(0));
        return arg1;*/
      case TokenNodeType.MULTIPLY:
        return evaluate(e.getChild(0)) * evaluate(e.getChild(1))
      case TokenNodeType.DIVIDE:
        return evaluate(e.getChild(0)) / evaluate(e.getChild(1))
      case TokenNodeType.OR:
        return intBool(evaluate(e.getChild(0)) || evaluate(e.getChild(1)))
      case TokenNodeType.AND:
        return intBool(evaluate(e.getChild(0)) && evaluate(e.getChild(1)))
      case TokenNodeType.EQUAL:
        return intBool(evaluate(e.getChild(0)) == evaluate(e.getChild(1)))
      case TokenNodeType.NOT_EQUAL:
        return intBool(evaluate(e.getChild(0)) != evaluate(e.getChild(1)))
      case TokenNodeType.SUP:
        return intBool(evaluate(e.getChild(0)) >= evaluate(e.getChild(1)))
      case TokenNodeType.STRICT_SUP:
        return intBool(evaluate(e.getChild(0)) > evaluate(e.getChild(1)))
      case TokenNodeType.INF:
        return intBool(evaluate(e.getChild(0)) <= evaluate(e.getChild(1)))
      case TokenNodeType.STRICT_INF:
        return intBool(evaluate(e.getChild(0)) < evaluate(e.getChild(1)))
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

  private int intBool(boolean b) {
    return b ? 1 : 0
  }

  private Object getReturnValue() {
    return returnValue
  }
}
