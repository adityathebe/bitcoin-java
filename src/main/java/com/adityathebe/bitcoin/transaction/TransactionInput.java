package com.adityathebe.bitcoin.transaction;

public class TransactionInput {
    public static final String NO_SEQUENCE = "FFFFFFFF";

    private String txId;
    private UTXO prevOut;
    private Integer n;

    private String scriptSig;
    private Integer scriptSigLen;

    private String nSequence;

    public TransactionInput(UTXO utxo, int outputIndex) {
        this.prevOut = utxo;
        this.n = outputIndex;
        this.nSequence = NO_SEQUENCE;
    }


}
