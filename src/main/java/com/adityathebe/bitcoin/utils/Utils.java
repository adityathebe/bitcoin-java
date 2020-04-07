package com.adityathebe.bitcoin.utils;

import javax.xml.bind.DatatypeConverter;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class Utils {
    public static final String bytesToHex(byte[] bytes) {
        return new BigInteger(1, bytes).toString(16);
    }

    public static final byte[] hexToBytes(String hex) {
        return DatatypeConverter.parseHexBinary(hex);
    }

    public static final byte[] changeEndian(byte[] bytes) {
        byte[] copy = new byte[bytes.length];
        for (int i = 0; i < bytes.length; i += 1) {
            int start = i;
            int length =1;
            int destPos = bytes.length - i -1;
            System.arraycopy(bytes, start, copy, destPos, length);
        }
        return copy;
    }
}
