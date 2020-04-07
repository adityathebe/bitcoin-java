package com.adityathebe.bitcoin.utils;

import javax.xml.bind.DatatypeConverter;
import java.math.BigInteger;

public class Utils {
    public static final String bytesToHex(byte[] bytes) {
        return new BigInteger(1, bytes).toString(16);
    }

    public static final byte[] hexToBytes(String hex) {
        return DatatypeConverter.parseHexBinary(hex);
    }
}
