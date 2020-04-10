package com.adityathebe.bitcoin.crypto;


import java.math.BigInteger;

import static com.adityathebe.bitcoin.utils.Utils.bytesToHex;

public class ECDSASignature {
    BigInteger r = null;
    BigInteger s = null;

    public ECDSASignature(BigInteger r, BigInteger s) {
        this.r = r;
        this.s = s;
    }

    public BigInteger getR() {
        return r;
    }

    public void setR(BigInteger r) {
        this.r = r;
    }

    public BigInteger getS() {
        return s;
    }

    public void setS(BigInteger s) {
        this.s = s;
    }

    public String getDerEncoded() {
        String rHex = bytesToHex(r.toByteArray());
        String sHex = bytesToHex(s.toByteArray());
        String rLen = Integer.toHexString(rHex.length() / 2);
        String sLen = Integer.toHexString(sHex.length() / 2);
        String interim = "02" + rLen + rHex + "02" + sLen + sHex;
        return "30" + Integer.toHexString(interim.length() / 2) + interim;
    }
}