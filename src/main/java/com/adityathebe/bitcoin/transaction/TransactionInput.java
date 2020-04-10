package com.adityathebe.bitcoin.transaction;

public class TransactionInput {
    public static final long NO_SEQUENCE = 0xFFFFFFFFL;

    /**
     * Hash of a past transaction
     */
    String hash;
    TransactionOutput prevOut;

    /**
     * Index of a transaction output within the transaction specified by hash.
     */
    Integer n;


    /**
     * The unlocking script.
     * Response script corresponding to the challenge script.
     */
    String scriptSig;
    Integer scriptSigLen;

    private long nSequence = NO_SEQUENCE;

    public TransactionInput(TransactionOutput parenttx, int outputIndex) {
        this.prevOut = parenttx;
        this.n = outputIndex;
    }


}
