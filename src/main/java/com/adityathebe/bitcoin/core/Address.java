package com.adityathebe.bitcoin.core;

import com.adityathebe.bitcoin.utils.Base58;

import static com.adityathebe.bitcoin.crypto.Crypto.*;
import static com.adityathebe.bitcoin.utils.Utils.bytesToHex;
import static com.adityathebe.bitcoin.utils.Utils.hexToBytes;

public class Address {
    private static String MAINNET_PUBKEY_PREFIX = "00";
    private static String TESTNET_PUBKEY_PREFIX = "6F";

    ECPublicKey publicKey;
    byte[] publicKeyBytes;

    public Address(ECPublicKey pubKey) {
        this.publicKey = pubKey;
        this.publicKeyBytes = hexToBytes(pubKey.getCompressed());
    }

    private static String genAddress(String prefixHex, byte[] publicKeyBytes) {
        byte[] address = ripemd160(sha256(publicKeyBytes));
        String mainNetworkAddress = prefixHex + bytesToHex(address);
        String doubleSha256 = bytesToHex(hash256(mainNetworkAddress));
        String checkSum = doubleSha256.substring(0, 8);
        String hexAddress = mainNetworkAddress + checkSum;
        return Base58.encode(hexToBytes(hexAddress));
    }

    public String getHex() {
        return genAddress(MAINNET_PUBKEY_PREFIX, this.publicKeyBytes);
    }

    public String getTestNetAddressHex() {
        return genAddress(TESTNET_PUBKEY_PREFIX, this.publicKeyBytes);
    }

    public byte[] getPublicKeyBytes() {
        return publicKeyBytes;
    }

    public ECPublicKey getPublicKey() {
        return publicKey;
    }
}
