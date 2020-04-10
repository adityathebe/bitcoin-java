package com.adityathebe.bitcoin.crypto;

import org.bouncycastle.crypto.digests.RIPEMD160Digest;

import java.security.MessageDigest;

import static com.adityathebe.bitcoin.utils.Utils.hexToBytes;

public class Crypto {
    public static byte[] ripemd160(byte[] bytes) {
        RIPEMD160Digest digest = new RIPEMD160Digest();
        digest.update(bytes, 0, bytes.length);
        byte[] out = new byte[20];
        digest.doFinal(out, 0);
        return out;
    }

    public static byte[] ripemd160(String hex) {
        return ripemd160(hexToBytes(hex));
    }

    public static byte[] sha256(String hex) {
        return sha256(hexToBytes(hex));
    }

    public static byte[] sha256(byte[] bytes) {
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("SHA-256");
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        assert md != null;
        return md.digest(bytes);
    }

    public static byte[] hash256(String hex) {
        return sha256(sha256(hex));
    }

    public static byte[] hash256(byte[] bytes) {
        return sha256(sha256(bytes));
    }

    public static byte[] hash160(byte[] bytes) {
        return ripemd160(sha256(bytes));
    }

    public static byte[] hash160(String hexVal) {
        return hash160(hexToBytes(hexVal));
    }
}
