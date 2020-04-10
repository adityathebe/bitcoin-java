package com.adityathebe.bitcoin.utils;

import javax.xml.bind.DatatypeConverter;

public class Utils {
    public static String bytesToHex(byte[] bytes) {
        return DatatypeConverter.printHexBinary(bytes);
    }

    public static byte[] hexToBytes(String hex) {
        assert (hex.length() % 2 == 0);
        if (hex.length() % 2 != 0) {
            hex = "0" + hex;
        }
        return DatatypeConverter.parseHexBinary(hex);
    }

    public static byte[] changeEndian(byte[] bytes) {
        byte[] copy = new byte[bytes.length];
        for (int i = 0; i < bytes.length; i += 1) {
            int destPos = bytes.length - i - 1;
            System.arraycopy(bytes, i, copy, destPos, 1);
        }
        return copy;
    }
}
