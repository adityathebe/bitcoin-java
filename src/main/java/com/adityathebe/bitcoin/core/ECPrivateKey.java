package com.adityathebe.bitcoin.core;

import com.adityathebe.bitcoin.utils.Utils;

import static com.adityathebe.bitcoin.utils.Utils.bytesToHex;
import static com.adityathebe.bitcoin.utils.Utils.hexToBytes;

public class ECPrivateKey {
    private byte[] privateKeyBytes;
    private String privateKeyHex;

    public ECPrivateKey(byte[] bytes) {
        privateKeyBytes = bytes;
        privateKeyHex = bytesToHex(bytes);
    }

    public ECPrivateKey() {
        privateKeyBytes = Utils.generateRandom256Bytes();
        privateKeyHex = bytesToHex(privateKeyBytes);
    }

    public ECPrivateKey(String hex) {
        privateKeyHex = hex;
        this.privateKeyBytes = hexToBytes(hex);
    }

    public byte[] getBytes() {
        return privateKeyBytes;
    }

    public String getHex() {
        return privateKeyHex;
    }
}
