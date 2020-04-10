package com.adityathebe.bitcoin.utils;

public class Utils {
    private static final char[] HEX_ARRAY = "0123456789ABCDEF".toCharArray();

    public static String bytesToHex(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        for (int j = 0; j < bytes.length; j++) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = HEX_ARRAY[v >>> 4];
            hexChars[j * 2 + 1] = HEX_ARRAY[v & 0x0F];
        }
        return new String(hexChars);
    }

    public static byte[] hexToBytes(String s) {
        byte[] b = new byte[s.length() / 2];
        for (int i = 0; i < b.length; i++) {
            int index = i * 2;
            int v = Integer.parseInt(s.substring(index, index + 2), 16);
            b[i] = (byte) v;
        }
        return b;
    }

//    public static String bytesToHex(byte[] bytes) {
//        return DatatypeConverter.printHexBinary(bytes);
//    }
//
//    public static byte[] hexToBytes(String hex) {
//        assert (hex.length() % 2 == 0);
//        if (hex.length() % 2 != 0) {
//            hex = "0" + hex;
//        }
//        return DatatypeConverter.parseHexBinary(hex);
//    }


    public static byte[] changeEndian(byte[] bytes) {
        byte[] copy = new byte[bytes.length];
        for (int i = 0; i < bytes.length; i += 1) {
            int destPos = bytes.length - i - 1;
            System.arraycopy(bytes, i, copy, destPos, 1);
        }
        return copy;
    }
}
