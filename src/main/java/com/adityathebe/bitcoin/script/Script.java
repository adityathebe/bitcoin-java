package com.adityathebe.bitcoin.script;

import com.adityathebe.bitcoin.utils.Utils;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.List;

import static com.adityathebe.bitcoin.script.ScriptOpCodes.*;

public class Script {
    protected List<ScriptChunk> chunks;
    protected byte[] program;

    Script(List<ScriptChunk> chunks) {
        this.chunks = new ArrayList<>(chunks);
    }

    public Script(byte[] programBytes) {
        program = programBytes;
        parse(programBytes);
    }

    private void parse(byte[] program) {
        chunks = new ArrayList<>();
        ByteArrayInputStream bis = new ByteArrayInputStream(program);
        while (bis.available() > 0) {
            // Each byte corresponds to an opCOde
            int opcode = bis.read();

            long numBytesOfData = -1;
            if (opcode >= 0 && opcode < OP_PUSHDATA1) {
                numBytesOfData = opcode;
            } else if (opcode == OP_PUSHDATA1) {
                numBytesOfData = bis.read();
            } else if (opcode == OP_PUSHDATA2) {
                numBytesOfData = bis.read();
                numBytesOfData += bis.read();
            } else if (opcode == OP_PUSHDATA4) {
                numBytesOfData = bis.read();
                numBytesOfData += bis.read();
                numBytesOfData += bis.read();
                numBytesOfData += bis.read();
            }

            ScriptChunk chunk;
            if (numBytesOfData == -1) {
                chunk = new ScriptChunk(opcode, null);
            } else {
                byte[] data = new byte[(int) numBytesOfData];
                bis.read(data, 0, (int) numBytesOfData);
                chunk = new ScriptChunk(opcode, data);
            }
            chunks.add(chunk);
        }
    }

    public static void main(String[] args) {
        String scriptPubKey = "76a91443849383122ebb8a28268a89700c9f723663b5b888ac";
        byte[] program = Utils.hexToBytes(scriptPubKey);

        Script s = new Script(program);
        for (ScriptChunk c : s.chunks) {
            System.out.println(c.opcode);
            if (c.data != null) {
                System.out.println(Utils.bytesToHex(c.data));
            }
        }
    }

//    /**
//     * Exposes the script interpreter. Normally you should not use this directly, instead use
//     * {@link TransactionInput#verify(TransactionOutput)} or
//     * {@link Script#correctlySpends(Transaction, int, TransactionWitness, Coin, Script, Set)}. This method
//     * is useful if you need more precise control or access to the final state of the stack. This interface is very
//     * likely to change in future.
//     */
//    public static void executeScript(@Nullable Transaction txContainingThis, long index,
//                                     Script script, LinkedList<byte[]> stack, Set<VerifyFlag> verifyFlags) throws ScriptException {
//        int opCount = 0;
//        int lastCodeSepLocation = 0;
//
//        LinkedList<byte[]> altstack = new LinkedList<>();
//        LinkedList<Boolean> ifStack = new LinkedList<>();
//
//        int nextLocationInScript = 0;
//        for (ScriptChunk chunk : script.chunks) {
//            boolean shouldExecute = !ifStack.contains(false);
//            int opcode = chunk.opcode;
//            nextLocationInScript += chunk.size();
//
//            if (OP_0 <= opcode && opcode <= OP_PUSHDATA4) {
//                if (opcode == OP_0)
//                    stack.add(new byte[]{});
//                else
//                    stack.add(chunk.data);
//            } else {
//
//                switch (opcode) {
//                    case OP_DUP:
//                        if (stack.size() < 1)
//                            throw new ScriptException(ScriptError.SCRIPT_ERR_INVALID_STACK_OPERATION, "Attempted OP_DUP on an empty stack");
//                        stack.add(stack.getLast());
//                        break;
//                    case OP_EQUALVERIFY:
//                        if (stack.size() < 2)
//                            throw new ScriptException(ScriptError.SCRIPT_ERR_INVALID_STACK_OPERATION, "Attempted OP_EQUALVERIFY on a stack with size < 2");
//                        if (!Arrays.equals(stack.pollLast(), stack.pollLast()))
//                            throw new ScriptException(ScriptError.SCRIPT_ERR_EQUALVERIFY, "OP_EQUALVERIFY: non-equal data");
//                        break;
//                    case OP_HASH160:
//                        if (stack.size() < 1)
//                            throw new ScriptException(ScriptError.SCRIPT_ERR_INVALID_STACK_OPERATION, "Attempted OP_HASH160 on an empty stack");
//                        stack.add(Utils.sha256hash160(stack.pollLast()));
//                        break;
//                    case OP_CHECKSIG:
//                        if (txContainingThis == null)
//                            throw new IllegalStateException("Script attempted signature check but no tx was provided");
//                        executeCheckSig(txContainingThis, (int) index, script, stack, lastCodeSepLocation, opcode, verifyFlags);
//                        break;
//
//                    default:
//                        throw new ScriptException(ScriptError.SCRIPT_ERR_BAD_OPCODE, "Script used a reserved or disabled opcode: " + opcode);
//                }
//            }
//
//            if (stack.size() + altstack.size() > MAX_STACK_SIZE || stack.size() + altstack.size() < 0)
//                throw new ScriptException(ScriptError.SCRIPT_ERR_STACK_SIZE, "Stack size exceeded range");
//        }
//
//        if (!ifStack.isEmpty())
//            throw new ScriptException(ScriptError.SCRIPT_ERR_UNBALANCED_CONDITIONAL, "OP_IF/OP_NOTIF without OP_ENDIF");
//    }
}
