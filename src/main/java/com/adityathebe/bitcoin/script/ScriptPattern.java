package com.adityathebe.bitcoin.script;

import java.util.List;

import static com.adityathebe.bitcoin.script.ScriptOpCodes.*;

public class ScriptPattern {
    public static boolean isP2PKH(Script script) {
        List<ScriptChunk> chunks = script.chunks;
        if (chunks.size() != 5)
            return false;
        if (!chunks.get(0).equalsOpCode(OP_DUP))
            return false;
        if (!chunks.get(1).equalsOpCode(OP_HASH160))
            return false;

        // Check Hash
        byte[] chunk2data = chunks.get(2).data;
        if (chunk2data == null) return false;

        if (!chunks.get(3).equalsOpCode(OP_EQUALVERIFY))
            return false;
        if (!chunks.get(4).equalsOpCode(OP_CHECKSIG))
            return false;
        return true;
    }

    public static byte[] extractHashFromP2PKH(Script script) {
        return script.chunks.get(2).data;
    }

}
