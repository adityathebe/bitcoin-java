package com.adityathebe.bitcoin.wallet;

public class TestWallet {
    public static void main(String[] args) throws Exception {
        Wallet w = new Wallet("48087c0ad530b5c9edea94f713a8dc0894da43dae001741bdb1369b956c999d9");

        String address = "1DNYWaLQuWmc9UqZakY2LWk8XSNX8JBECh";
        String transaction = w.createTransaction(address, 2200);
        System.out.println(transaction);
    }
}

