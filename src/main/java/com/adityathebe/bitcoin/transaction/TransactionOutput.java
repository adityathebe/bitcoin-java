package com.adityathebe.bitcoin.transaction;

public class TransactionOutput {
    /**
     * Amount of Satoshi
     */
    Long nValue;

    String scriptPubkey;

    /**
     * The Lock Script (Challenge script)
     */
    Integer scriptPubkeyLen;
}
