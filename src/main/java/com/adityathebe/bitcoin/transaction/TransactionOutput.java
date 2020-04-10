package com.adityathebe.bitcoin.transaction;

import com.adityathebe.bitcoin.core.Coin;

public class TransactionOutput {
    String scriptPubkey;
    Integer scriptPubkeyLen;
    Coin coin;

    TransactionOutput(String scriptPubkey, Coin coin) {
        this.scriptPubkey = scriptPubkey;
        this.scriptPubkeyLen = (int) Math.floor(scriptPubkey.length() / 2);
        this.coin = coin;
    }
}
