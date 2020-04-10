package com.adityathebe.bitcoin.crypto;


import com.adityathebe.bitcoin.utils.Utils;
import org.bouncycastle.crypto.params.ECDomainParameters;
import org.bouncycastle.crypto.params.ECPrivateKeyParameters;
import org.bouncycastle.crypto.params.ParametersWithRandom;
import org.bouncycastle.crypto.signers.ECDSASigner;
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
import java.util.LinkedList;

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
        Security.addProvider(new BouncyCastleProvider());

        // Create Private Key Object
        PrivateKey pk = privateKeyFromBytes(privateKey);

        // Sign the message
        Signature ecdsaSign = null;
        try {
            ecdsaSign = Signature.getInstance("SHA256withECDSA", "BC");
        } catch (NoSuchAlgorithmException | NoSuchProviderException e) {
            e.printStackTrace();
        }
        ecdsaSign.initSign(pk, new SecureRandom());
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
        String privateKeyStr = "8f892dfe781e2337f3be08ab18f74bc7865f26fb8e6c52318feab05317029c46";
        String pubKeyStr = "0473d034d4599e7e99ab5208eac411e91df0a024573487ec50f8ac88483fe45caec350ecbd5ef74d7f0b080d60173c57d50a0cb28da690ed68cec75a616be917cc";
        String message = "aaa";

        // Sign
        byte[] sig = sign(Utils.hexToBytes(privateKeyStr), message.getBytes(StandardCharsets.UTF_8));
        System.out.println("Signature: " + Utils.bytesToHex(sig));

        // Verify
        boolean verified = verify(sig, message.getBytes(StandardCharsets.UTF_8), Utils.hexToBytes(pubKeyStr));
        System.out.println("Verified: " + verified);
    }
}
