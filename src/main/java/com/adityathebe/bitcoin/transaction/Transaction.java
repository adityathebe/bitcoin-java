package com.adityathebe.bitcoin.transaction;

import org.python.modules.struct;

public class Transaction {
    String txId;
    String nVersion = "01000000";
    Integer txInputCount;
    TransactionInput[] vin;
    Integer txOutputCount;
    TransactionOutput[] vout;
    String lockTime = "00000000";

    public Transaction(TransactionInput inputs, String receiverAddress, byte[] privateKey, int amount) {

    }

    public Transaction() {

    }
}
