package com.adityathebe.bitcoin.crypto;

import com.adityathebe.bitcoin.utils.Utils;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Sha256 {

    public static byte[] getHash(String hexstr) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        byte[] bytes = Utils.hexToBytes(hexstr);
        byte[] digest = md.digest(bytes);
        return digest;
    }
}
