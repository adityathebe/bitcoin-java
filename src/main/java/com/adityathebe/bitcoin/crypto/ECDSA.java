package com.adityathebe.bitcoin.crypto;


import com.adityathebe.bitcoin.utils.Utils;
import org.bouncycastle.jcajce.provider.asymmetric.util.EC5Util;
import org.bouncycastle.jce.ECNamedCurveTable;
import org.bouncycastle.jce.ECPointUtil;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.jce.spec.ECNamedCurveParameterSpec;
import org.bouncycastle.jce.spec.ECPrivateKeySpec;
import org.bouncycastle.math.ec.ECCurve;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.spec.InvalidKeySpecException;

public class ECDSA {
    public static PrivateKey privateKeyFromBytes(byte[] privateKey) {
        try {
            BigInteger s = new BigInteger(1, privateKey);
            ECNamedCurveParameterSpec ecParameterSpec = ECNamedCurveTable.getParameterSpec("SECP256K1");
            ECPrivateKeySpec ecPrivateKeySpec = new org.bouncycastle.jce.spec.ECPrivateKeySpec(s, ecParameterSpec);
            KeyFactory factory = KeyFactory.getInstance("ECDSA", "BC");
            return factory.generatePrivate(ecPrivateKeySpec);
        } catch (NoSuchAlgorithmException | NoSuchProviderException | InvalidKeySpecException e) {
            return null;
        }
    }

    public static PublicKey publicKeyFromBytes(byte[] pubKey) {
        try {
            /* Source: https://stackoverflow.com/a/33347595/6199444 */
            ECNamedCurveParameterSpec params = ECNamedCurveTable.getParameterSpec("secp256k1");
            KeyFactory fact = KeyFactory.getInstance("ECDSA", "BC");
            ECCurve curve = params.getCurve();
            java.security.spec.EllipticCurve ellipticCurve = EC5Util.convertCurve(curve, params.getSeed());
            java.security.spec.ECPoint point = ECPointUtil.decodePoint(ellipticCurve, pubKey);
            java.security.spec.ECParameterSpec params2 = EC5Util.convertSpec(ellipticCurve, params);
            java.security.spec.ECPublicKeySpec keySpec = new java.security.spec.ECPublicKeySpec(point, params2);
            return fact.generatePublic(keySpec);
        } catch (NoSuchAlgorithmException | NoSuchProviderException | InvalidKeySpecException e) {
            return null;
        }
    }

    /**
     * Returns the signature in ASN1-DER
     */
    public static byte[] sign(byte[] privateKey, byte[] message) throws SignatureException, InvalidKeyException {
        // Create Private Key Object
        PrivateKey pk = privateKeyFromBytes(privateKey);

        // Sign the message
        Signature ecdsaSign = null;
        try {
            ecdsaSign = Signature.getInstance("SHA256withECDSA", "BC");
        } catch (NoSuchAlgorithmException | NoSuchProviderException e) {
            e.printStackTrace();
        }
        ecdsaSign.initSign(pk);
        ecdsaSign.update(message);
        return ecdsaSign.sign();
    }

    public static boolean verify(byte[] signature, byte[] message, byte[] pubKey) throws Exception {
        // Create Public Key Object
        PublicKey pk = publicKeyFromBytes(pubKey);

        // Verify the message
        Signature ecdsaVerify = Signature.getInstance("SHA256withECDSA", "BC");
        ecdsaVerify.initVerify(pk);
        ecdsaVerify.update(message);
        return ecdsaVerify.verify(signature);
    }

    public static void main(String[] args) throws Exception {
        Security.addProvider(new BouncyCastleProvider());
        String privateKeyStr = "4a5b37137bf369b404be05735116ccefc171ab99a87835fa8bdb5916be3bbfab";
        String pubKeyStr = "04a9aa2a30e8bf24bc667bbc4a9a5aaa4e3be1af7cb5c7a388a6f60a875b58f1d7c0abee1a88abd693841ad2f86f7f689b3c402ecf97e303b78ddaee13cfb075a3";
        String message = "Aditya Thebe";

        // Sign
        byte[] sig = sign(Utils.hexToBytes(privateKeyStr), message.getBytes(StandardCharsets.UTF_8));
        System.out.println("Signature: " + Utils.bytesToHex(sig));

        // Verify
        boolean verified = verify(sig, message.getBytes(StandardCharsets.UTF_8), Utils.hexToBytes(pubKeyStr));
        System.out.println("Verified: " + verified);
    }
}
