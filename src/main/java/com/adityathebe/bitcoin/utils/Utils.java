package com.adityathebe.bitcoin.utils;

import com.sun.xml.internal.ws.util.StringUtils;

import javax.xml.bind.DatatypeConverter;
import java.math.BigInteger;

public class Utils {
    public static String bytesToHex(byte[] bytes) {
        return new BigInteger(1, bytes).toString(16);
    }

    public static byte[] hexToBytes(String hexstr) {
        byte[] val = new byte[hexstr.length() / 2];
        for (int i = 0; i < val.length; i++) {
            int index = i * 2;
            int j = Integer.parseInt(hexstr.substring(index, index + 2), 16);
            val[i] = (byte) j;
        }
        return val;
    }
}
