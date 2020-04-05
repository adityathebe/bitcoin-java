package com.adityathebe.bitcoin.utils;

import java.math.BigInteger;

public class Utils {
    public static String bytesToHex(byte[] bytes) {
        return new BigInteger(1, bytes).toString(16);
    }

    public static byte[] hexToBytes(String hex) {
        byte[] val = new byte[hex.length() / 2];
        for (int i = 0; i < val.length; i++) {
            int index = i * 2;
            int j = Integer.parseInt(hex.substring(index, index + 2), 16);
            val[i] = (byte) j;
        }
        return val;
    }
}
