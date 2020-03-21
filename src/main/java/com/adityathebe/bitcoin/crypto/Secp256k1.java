package com.adityathebe.bitcoin.crypto;

import com.sun.org.apache.bcel.internal.generic.BIPUSH;
import sun.misc.FloatingDecimal;

import java.math.BigInteger;

class ECPoint {
    public BigInteger x;
    public BigInteger y;

    public ECPoint(BigInteger x, BigInteger y) {
        this.x = x;
        this.y = y;
    }
}

public class Secp256k1 {
    static BigInteger Gx = new BigInteger("79BE667EF9DCBBAC55A06295CE870B07029BFCDB2DCE28D959F2815B16F81798", 16);
    static BigInteger Gy = new BigInteger("483ADA7726A3C4655DA4FBFC0E1108A8FD17B448A68554199C47D08FFB10D4B8", 16);
    private static final ECPoint GenPoint = new ECPoint(Gx, Gy);
    private static final Double PCurveDouble = Math.pow(2, 256) - Math.pow(2, 32) - Math.pow(2, 9) - Math.pow(2, 8) - Math.pow(2, 7) - Math.pow(2, 6) - Math.pow(2, 4) - 1;
    private static final BigInteger PCurve = BigInteger.valueOf(PCurveDouble.longValue());

    private static BigInteger modulo(BigInteger x, BigInteger y) {
        BigInteger result = x.mod(y);
        if (result.compareTo(new BigInteger("0", 16)) > 0) {
            result.add(y);
        }
        return result;
    }

    private static BigInteger modInverse(BigInteger a) {
        BigInteger lm = new BigInteger("1", 16);
        BigInteger hm = new BigInteger("0", 16);
        BigInteger low = modulo(a, PCurve);
        BigInteger high = PCurve;

        while (low.compareTo(BigInteger.valueOf(1)) > 0) {
            BigInteger ratio = high.divide(low);
            BigInteger nm = hm.subtract(lm.multiply(ratio));
            BigInteger nw = high.subtract(low.multiply(ratio));

            hm = lm;
            lm = nm;
            high = low;
            low = nw;
        }

        return modulo(lm, PCurve);
    }

    public static ECPoint ecAdd(ECPoint a, ECPoint b) {
        BigInteger lamAdd = modulo((b.y.subtract(a.y)).multiply(modInverse(b.x.subtract(a.x))), PCurve);
        BigInteger x = modulo((lamAdd.multiply(lamAdd).subtract(a.x).subtract(b.x)), PCurve);
        BigInteger y = modulo((lamAdd.multiply((a.x.subtract(x))).subtract(a.y)), PCurve);
        return new ECPoint(x, y);
    }

    public static ECPoint ecDouble(ECPoint a) {
        BigInteger t11 = BigInteger.valueOf(3).multiply(a.x).multiply(a.x);
        BigInteger t12 = a.y.multiply(BigInteger.valueOf(2)).modInverse(PCurve);
        BigInteger lam = modulo(t11.multiply(t12), PCurve);

        BigInteger t21 = lam.multiply(lam).subtract(BigInteger.valueOf(2).multiply(a.x));
        BigInteger x = modulo(t21, PCurve);

        BigInteger t22 = lam.multiply(a.x.subtract(x)).subtract(a.y);
        BigInteger y = modulo(t22, PCurve);

        return new ECPoint(x, y);
    }

    public static ECPoint ecMultiply(BigInteger scalar) throws Exception {
        String scalarBinary = scalar.toString(2);

//        if (Long.parseLong(scalarhex, 16) == 0 || Long.parseLong(scalarhex, 16) >= 115792089237316195423570985008687907852837564279074904382605163141518161494337d) {
//            throw new Exception("Invalid scalar range");
//        }

//        String scalarBinary = hexToBinary(scalarhex);
        while (scalarBinary.length() != 256) {
            scalarBinary = "0" + scalarBinary;
        }

        ECPoint Q = GenPoint;
        for (char x : scalarBinary.toCharArray()) {
            Q = ecDouble(Q);
            System.out.println("Double" + Q.x + " " + Q.y);
            if (x == '1') {
                Q = ecAdd(Q, GenPoint);
                System.out.println("Add" + Q.x + " " + Q.y);
            }
        }
        return Q;
    }

    public static void main(String[] args) throws Exception {
        BigInteger privateKey = new BigInteger("0da2585cdeaebb7c1ccd1de6f0bb7f0fc75bd2f9026ff9377ec9b0c4909e20c8", 16);
        ECPoint pubKey = ecMultiply(privateKey);
        System.out.println(pubKey.x);

    }
}
