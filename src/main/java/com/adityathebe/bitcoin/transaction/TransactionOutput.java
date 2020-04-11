package com.adityathebe.bitcoin.transaction;

import com.adityathebe.bitcoin.core.Coin;

public class TransactionOutput {
    String scriptPubkey;
    Integer scriptPubkeyLen;
    String address;

    public TransactionOutput(String scriptPubkey) {
        this.scriptPubkey = scriptPubkey;
        this.scriptPubkeyLen = (int) Math.floor(scriptPubkey.length() / 2);
    }

    public TransactionOutput(String recieverAddress, Coin c) {
        address = recieverAddress;
    }
}
