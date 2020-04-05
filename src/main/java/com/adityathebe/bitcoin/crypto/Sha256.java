package com.adityathebe.bitcoin.crypto;

import com.adityathebe.bitcoin.utils.Utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Sha256 {

    public static byte[] getHash(String hex) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        byte[] bytes = Utils.hexToBytes(hex);
        byte[] digest = md.digest(bytes);
        return digest;
    }
}
