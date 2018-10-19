package com.tambapps.compiler.analyzer

import com.tambapps.compiler.analyzer.token.TokenNode
import com.tambapps.compiler.analyzer.token.TokenNodeType
import com.tambapps.compiler.util.DequeMap
import com.tambapps.compiler.util.Symbol

class SementicAnalyzer {

    DequeMap dequeMap = new DequeMap()

    int nbSlot = 0

    void process(TokenNode node){
        switch (node.type){
            case TokenNodeType.VAR_DECL:
                Symbol s = dequeMap.newSymbol(node.value)
                s.slot = nbSlot++
            case TokenNodeType.VAR_REF:
                Symbol s = dequeMap.findSymbol(node.value)
                node.value.index = s.slot
            case TokenNodeType.BLOC:
                dequeMap.newBlock()
                for(int i = 0; i<node.nbChildren(); i++){
                   process(node.getChild(i))
                }
                dequeMap.endBlock()
            /*default:
                for(int i = 0; i<node.nbChildren(); i++){
                    process(node.getChild(i))
                }
            */
        }

    }
}
