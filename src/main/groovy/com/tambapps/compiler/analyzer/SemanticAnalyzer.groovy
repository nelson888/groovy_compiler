package com.tambapps.compiler.analyzer

import com.tambapps.compiler.analyzer.token.TokenNode
import com.tambapps.compiler.analyzer.token.TokenNodeType
import com.tambapps.compiler.util.DequeMap
import com.tambapps.compiler.util.Symbol

class SemanticAnalyzer {

    private DequeMap dequeMap = new DequeMap()
    private int nbSlot = 0

    void process(TokenNode node){
        switch (node.type){
            case TokenNodeType.VAR_DECL:
                Symbol s = dequeMap.newSymbol(node.value.name)
                s.slot = nbSlot++
                break
            case TokenNodeType.VAR_REF:
                Symbol s = dequeMap.findSymbol(node.value.name)
                node.value.index = s.slot
                break
            case TokenNodeType.BLOC:
                dequeMap.newBlock()
                for(int i = 0; i<node.nbChildren(); i++){
                   process(node.getChild(i))
                }
                dequeMap.endBlock()
                break
            default:
                for(int i = 0; i<node.nbChildren(); i++){
                    process(node.getChild(i))
                }
        }
    }

    int getNbSlot() {
        return nbSlot
    }
}
