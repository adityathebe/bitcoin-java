package com.adityathebe.bitcoin.crypto;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.Security;

public class Ripemd160 {
    static public byte[] getHash(byte[] bytes) throws NoSuchAlgorithmException {
        Security.addProvider(new BouncyCastleProvider());

        // Digest bytes
        MessageDigest md = MessageDigest.getInstance("RIPEMD160");
        return md.digest(bytes);
    }
}
