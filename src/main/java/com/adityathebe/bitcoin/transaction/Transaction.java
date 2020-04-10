package com.adityathebe.bitcoin.transaction;

import com.adityathebe.bitcoin.crypto.ECDSA;

import static com.adityathebe.bitcoin.crypto.Crypto.sha256;
import static com.adityathebe.bitcoin.script.ScriptOpCodes.*;
import static com.adityathebe.bitcoin.script.ScriptOpCodes.OP_CHECKSIG;
import static com.adityathebe.bitcoin.utils.Utils.*;
import static com.adityathebe.bitcoin.utils.Utils.bytesToHex;

public class Transaction {
    String txHash;

    /**
     * double-SHA256 hash of the raw transaction
     */
    String txId;

    /**
     * Transaction format version
     */
    Integer nVersion;

    Integer txInputCount;
    TransactionInput[] vin;
    Integer txOutputCount;
    TransactionOutput[] vout;

    public void setTxId(String txId) {
        this.txId = txId;
    }

    /**
     * point in time past which the transaction should
     * be included in a block.
     * UNIX format or as a block number
     * Value Description
     * <p>
     * 0 Always locked.
     * < 5 ∗ 108 Block number at which transaction is locked.
     * ≥ 5 ∗ 108 UNIX timestamp at which transaction is locked
     */
    Long nLockTime;

    public Transaction(TransactionInput inputs, String receiverAddress, byte[] privateKey, int amount) {

    }

//    public String toRawHex() {
//        String nVersion = "01000000";
//        String inCount = "01";
//        String prevOutHash = bytesToHex(changeEndian(hexToBytes("658d202be6b0bfba4d5325580fb39ea782701ef1586806d4e0a43c6d4aa27116")));
//        String prevOutN = "01000000";
//        String hashFromAddress = getHashFromAddress(address);
//        String hashLength = Integer.toHexString((hashFromAddress.length() / 2));
//        String scriptSig = Integer.toHexString(OP_DUP) + Integer.toHexString(OP_HASH160) + hashLength + hashFromAddress + Integer.toHexString(OP_EQUALVERIFY) + Integer.toHexString(OP_CHECKSIG);
//        String scriptSigLength = Integer.toHexString(scriptSig.length() / 2);
//        String sequence = "FFFFFFFF";
//        String outCount = "01";
//
//        // Satoshi Hex
//        String amountHex = Integer.toHexString(satoshi);
//        while (amountHex.length() < 16) {
//            amountHex = "0" + amountHex;
//        }
//        String amount = bytesToHex(changeEndian(hexToBytes(amountHex)));
//
//        String lockField = "00000000";
//        String hashCodeType = "01000000";
//        String preTxData = nVersion + inCount + prevOutHash + prevOutN + scriptSigLength + scriptSig + sequence + outCount + amount + scriptSigLength + scriptSig + lockField + hashCodeType;
//        byte[] doubleHash = sha256(sha256(preTxData));
//        byte[] revDoubleHash = changeEndian(doubleHash);
//        byte[] signature = ECDSA.sign(privateKey, revDoubleHash);
//        String finalSign = bytesToHex(signature) + "01";
//
//        String sigLength = Integer.toHexString(finalSign.length() / 2);
//        String pubKeyLength = Integer.toHexString((getPublicKeyHex().length() / 2));
//        String sigScript = sigLength + finalSign + pubKeyLength + getPublicKeyHex();
//        String sigScriptLength = Integer.toHexString(sigScript.length() / 2);
//        return nVersion + inCount + prevOutHash + prevOutN + sigScriptLength + sigScript + sequence + outCount + amount + scriptSigLength + scriptSig + lockField;
//    }
}
