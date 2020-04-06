package com.adityathebe.bitcoin.transaction;

public class TransactionInput {
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
     * length of the signature script field scriptSig in bytes
     */
    Integer scriptSigLen;

    /**
     * The unlocking script.
     * Response script corresponding to the challenge script.
     */
    String scriptSig;

    public static final long nSequence = 0xFFFFFFFFL;


}
