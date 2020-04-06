package com.adityathebe.bitcoin.script;


import org.jetbrains.annotations.Nullable;

import static com.adityathebe.bitcoin.script.ScriptOpCodes.*;

public class ScriptChunk {
    public int opcode;

    @Nullable
    public final byte[] data;

    public ScriptChunk(int opcode, @Nullable byte[] data) {
        this.opcode = opcode;
        this.data = data;
    }

    public int size() {
        int opcodeLength = 1;
        int pushDataLength = 0;
        if (opcode == OP_PUSHDATA1) pushDataLength = 1;
        else if (opcode == OP_PUSHDATA2) pushDataLength = 2;
        else if (opcode == OP_PUSHDATA4) pushDataLength = 4;

        int dataLength = data == null ? 0 : data.length;
        return opcodeLength + pushDataLength + dataLength;
    }

    public boolean equalsOpCode(int opDup) {
        return this.opcode == opDup;
    }
}
