package com.tambapps.compiler.util

import java.util.concurrent.LinkedBlockingDeque

class DequeMap {

    private final Deque<Map<String,Symbol>> symbolsMap = new LinkedBlockingDeque<>();

    void newBlock(){
        symbolsMap.push(new HashMap<>())
    }

    void endBlock(){
        symbolsMap.pop()
    }

    Symbol newSymbol(String ident){
        def map = symbolsMap.peek()
        if (map.containsKey(ident)){
            throw new RuntimeException("Already defined variable")
        }
        Symbol s = new Symbol(ident)
        map.put(ident, s)
        return s
    }

    Symbol findSymbol(String ident){
        for (def map:symbolsMap.descendingIterator()){
            if (map.containsKey(ident)) {
                return map.get(ident)
            }
        }
        throw new RuntimeException("Symbol not found")
    }
}
