package com.adityathebe.bitcoin.transaction;

import com.adityathebe.bitcoin.core.Coin;

import java.util.ArrayList;
import java.util.List;

public class TransactionBuilder {
    Transaction tx;
    Coin coin;
    List<TransactionInput> inputs = new ArrayList<>();
    List<TransactionOutput> outputs = new ArrayList<>();

    public TransactionBuilder() {
        tx = new Transaction();
    }

    public TransactionBuilder addInput(TransactionInput inp) {
        inputs.add(inp);
        return this;
    }

    public TransactionBuilder addOutput(TransactionOutput output) {
        outputs.add(output);
        return this;
    }

    public TransactionBuilder setCoin(Coin c) {
        coin = c;
        return this;
    }

    public Transaction build() {
        assert coin != null : "No Coin Provided";
        assert inputs.size() != 0 : "No Inputs Provided";
        assert outputs.size() != 0 : "No Outputs provided";

        // 1. Unsigned Tx Hash
        // 1a. Input Hex
        // 1b. Output Hex

        // 2. Signed Tx

        return tx;
    }

    public static void main(String[] args) {
        TransactionBuilder t = new TransactionBuilder();
        Coin c = new Coin(1000L);
        UTXO prevTx = new UTXO("76a914745e794502c6307f10e71c66e066c8d7f714066688ac");
        TransactionInput inp = new TransactionInput(prevTx, 0);
        TransactionOutput out = new TransactionOutput("2NGZrVvZG92qGYqzTLjCAewvPZ7JE8S8VxE", c);

        Transaction tr = t.addInput(inp).addOutput(out).build();
        System.out.println(tr);
    }
}
