package com.adityathebe.bitcoin.crypto;

import com.adityathebe.bitcoin.core.ECPrivateKey;
import com.adityathebe.bitcoin.core.ECPublicKey;
import com.adityathebe.bitcoin.utils.Utils;
import org.bouncycastle.jce.ECNamedCurveTable;
import org.bouncycastle.math.ec.ECPoint;

import java.math.BigInteger;


public class ECDSA {
    private static final BigInteger CURVE_N = ECNamedCurveTable.getParameterSpec("secp256k1").getN();
    private static final ECPoint CURVE_G = ECNamedCurveTable.getParameterSpec("secp256k1").getG();

    public static ECDSASignature sign(ECPrivateKey privateKey, byte[] hash) {
        BigInteger z = new BigInteger(1, hash);
        BigInteger e = new BigInteger(1, privateKey.getBytes());

        BigInteger r = BigInteger.ZERO;
        BigInteger s = BigInteger.ZERO;

        while (r.compareTo(BigInteger.ZERO) == 0 || s.compareTo(BigInteger.ZERO) == 0) {
            BigInteger k = new BigInteger(1, Utils.generateRandom256Bytes());
            BigInteger kInv = k.modInverse(CURVE_N);
            org.bouncycastle.math.ec.ECPoint R = CURVE_G.multiply(k);
            r = R.normalize().getXCoord().toBigInteger();
            s = kInv.multiply(z.add(r.multiply(e))).mod(CURVE_N);
            if (s.compareTo(CURVE_N.divide(BigInteger.valueOf(2L))) > 0) {
                s = CURVE_N.subtract(s);
            }
        }

        return new ECDSASignature(r, s);
    }

    public static boolean verify(ECDSASignature signature, ECPublicKey publicKey, byte[] hash) {
        BigInteger r = signature.getR();
        BigInteger s = signature.getS();
        BigInteger z = new BigInteger(1, hash);
        BigInteger sInv = s.modInverse(CURVE_N);
        BigInteger u = z.multiply(sInv).mod(CURVE_N);
        BigInteger v = r.multiply(sInv).mod(CURVE_N);
        ECPoint uPoint = CURVE_G.multiply(u);
        ECPoint vPoint = publicKey.getPoint().multiply(v);
        ECPoint sigPoint = uPoint.add(vPoint);
        return r.compareTo(sigPoint.normalize().getXCoord().toBigInteger()) == 0;
    }
}
