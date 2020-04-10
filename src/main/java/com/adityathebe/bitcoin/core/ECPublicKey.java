package com.adityathebe.bitcoin.core;

import org.bouncycastle.jce.ECNamedCurveTable;
import org.bouncycastle.jce.spec.ECNamedCurveParameterSpec;
import org.bouncycastle.math.ec.ECPoint;

import java.math.BigInteger;

public class ECPublicKey {
    private static final ECNamedCurveParameterSpec secp256k1Spec = ECNamedCurveTable.getParameterSpec("secp256k1");

    private BigInteger x;
    private BigInteger y;
    private ECPoint point;

    public ECPublicKey(ECPrivateKey privateKey) {
        BigInteger pk = new BigInteger(1, privateKey.getBytes());
        this.point = secp256k1Spec.getG().multiply(pk);
        this.x = this.point.normalize().getXCoord().toBigInteger();
        this.y = this.point.normalize().getYCoord().toBigInteger();
    }

    public ECPublicKey(byte[] privateKey) {
        BigInteger pk = new BigInteger(1, privateKey);
        this.point = secp256k1Spec.getG().multiply(pk);
        this.x = this.point.normalize().getXCoord().toBigInteger();
        this.y = this.point.normalize().getYCoord().toBigInteger();
    }

    public ECPoint getPoint() {
        return point;
    }

    public String getCompressed() {
        BigInteger y = new BigInteger(point.normalize().getYCoord().toString(), 16);
        if (y.mod(BigInteger.valueOf(2L)).equals(BigInteger.valueOf(0))) {
            return "02" + point.normalize().getXCoord().toString();
        }
        return "03" + point.normalize().getXCoord().toString();
    }

    public String getUncompressed() {
        return "04" + point.normalize().getXCoord().toString() + point.normalize().getYCoord().toString();
    }
}
