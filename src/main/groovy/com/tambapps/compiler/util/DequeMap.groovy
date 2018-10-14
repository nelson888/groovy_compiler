package com.tambapps.compiler.util

import java.util.concurrent.LinkedBlockingDeque

class DequeMap {

    private final Deque<Map<String,Object>> symbolsMap = new LinkedBlockingDeque<>();

    void newBlock(){
        symbolsMap.push(new HashMap<>())
    }

    void endBlock(){
        symbolsMap.pop()
    }

    Object newSymbol(String ident){
        def map = symbolsMap.peek()
        if (map.containsKey(ident)){
            throw new RuntimeException("Already defined variable")
        }
        def s = new Object()
        map.put(ident, s)
        return s
    }

    Object findSymbol(String ident){
        for (def map:symbolsMap.descendingIterator()){
            if (map.containsKey(ident)) {
                return map.get(ident)
            }
        }
        throw new RuntimeException("Symbol not found")
    }
}
