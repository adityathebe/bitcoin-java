package com.adityathebe.bitcoin.wallet;

import com.adityathebe.bitcoin.crypto.ECDSA;
import com.adityathebe.bitcoin.utils.Base58;
import com.adityathebe.bitcoin.utils.Utils;
import org.bouncycastle.jce.ECNamedCurveTable;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.jce.spec.ECNamedCurveParameterSpec;
import org.bouncycastle.math.ec.ECPoint;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.Security;

import static com.adityathebe.bitcoin.crypto.Crypto.ripemd160;
import static com.adityathebe.bitcoin.crypto.Crypto.sha256;
import static com.adityathebe.bitcoin.script.ScriptOpCodes.*;
import static com.adityathebe.bitcoin.utils.Utils.*;

public class Wallet {
    private byte[] privateKey;
    private ECPoint publicKey;
    private String uncompressed_address;

    private static final String RANDOM_NUMBER_ALGORITHM = "SHA1PRNG";
    private static final String RANDOM_NUMBER_ALGORITHM_PROVIDER = "SUN";
    public static final BigInteger MAX_PRIVATE_KEY = new BigInteger("00FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFEBAAEDCE6AF48A03BBFD25E8CD0364140", 16);
    private static final ECNamedCurveParameterSpec secp256k1Spec = ECNamedCurveTable.getParameterSpec("secp256k1");


    public byte[] getPrivateKey() {
        return privateKey;
    }

    public void setPrivateKey(byte[] privateKey) {
        this.privateKey = privateKey;
    }

    public ECPoint getPublicKey() {
        return publicKey;
    }

    public String getPublicKeyHex() {
        ECPoint normPoint = publicKey.normalize();
        return "04" + normPoint.getXCoord().toString() + normPoint.getYCoord().toString();
    }

    public String getUncompressed_address() {
        return uncompressed_address;
    }

    public Wallet() {
        init();
    }

    public Wallet(String privateKeyHex) {
        privateKey = Utils.hexToBytes(privateKeyHex);
        init();
    }

    public void init() {
        if (this.privateKey == null) {
            this.privateKey = genPrivateKey();
        }
        this.publicKey = genPublicKey(this.privateKey);
        this.uncompressed_address = genAddress(this.publicKey);
    }

    /**
     * Generate a random private key that can be used with Secp256k1.
     */
    public static byte[] genPrivateKey() {
        SecureRandom secureRandom;
        try {
            secureRandom = SecureRandom.getInstance(RANDOM_NUMBER_ALGORITHM, RANDOM_NUMBER_ALGORITHM_PROVIDER);
        } catch (Exception e) {
            secureRandom = new SecureRandom();
        }

        // Generate the key, skipping as many as desired.
        byte[] privateKeyAttempt = new byte[32];
        secureRandom.nextBytes(privateKeyAttempt);
        BigInteger privateKeyCheck = new BigInteger(1, privateKeyAttempt);
        while (privateKeyCheck.compareTo(BigInteger.ZERO) == 0 || privateKeyCheck.compareTo(MAX_PRIVATE_KEY) == 1) {
            secureRandom.nextBytes(privateKeyAttempt);
            privateKeyCheck = new BigInteger(1, privateKeyAttempt);
        }

        return privateKeyAttempt;
    }

    /**
     * Converts a private key into its corresponding public key.
     */
    public static ECPoint genPublicKey(byte[] privateKey) {
        BigInteger pk = new BigInteger(1, privateKey);
        return secp256k1Spec.getG().multiply(pk);
    }

    public static ECPoint genPublicKey(String privateKeyHex) {
        BigInteger pk = new BigInteger(privateKeyHex, 16);
        return secp256k1Spec.getG().multiply(pk);
    }

    public static String genAddress(String pubKey) throws NoSuchAlgorithmException {
        byte[] addressByte = ripemd160(pubKey);
        String address = Utils.bytesToHex(addressByte);
        String mainNetworkAddress = "00" + address;

        String doubleSha256 = Utils.bytesToHex(sha256(sha256(mainNetworkAddress)));
        String checkSum = doubleSha256.substring(0, 8);
        String hexAddress = mainNetworkAddress + checkSum;

        return Base58.encode(Utils.hexToBytes(hexAddress));
    }

    public static String genAddress(ECPoint pubKey) {
        try {
            String pubKeyStr = "04" + pubKey.normalize().getXCoord().toString() + pubKey.normalize().getYCoord().toString();
            return Wallet.genAddress(pubKeyStr);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String getHashFromAddress(String address) throws Exception {
        String decoded = bytesToHex(Base58.decode(address));
        return decoded.substring(0, decoded.length() - 8);
    }

    /**
     * @param address P2PKH Legacy address
     * @param satoshi Amount in Satoshi
     * @return The hex encoded transaction
     */
    public String createTransaction(String address, int satoshi) throws Exception {
        String nVersion = "01000000";
        String inCount = "01";
        String prevOutHash = bytesToHex(changeEndian(hexToBytes("658d202be6b0bfba4d5325580fb39ea782701ef1586806d4e0a43c6d4aa27116")));
        String prevOutN = "01000000";
        String hashFromAddress = getHashFromAddress(address);
        String hashLength = Integer.toHexString((hashFromAddress.length() / 2));
        String scriptSig = Integer.toHexString(OP_DUP) + Integer.toHexString(OP_HASH160) + hashLength + hashFromAddress + Integer.toHexString(OP_EQUALVERIFY) + Integer.toHexString(OP_CHECKSIG);
        String scriptSigLength = Integer.toHexString(scriptSig.length() / 2);
        String sequence = "FFFFFFFF";
        String outCount = "01";

        // Satoshi Hex
        String amountHex = Integer.toHexString(satoshi);
        while (amountHex.length() < 16) {
            amountHex = "0" + amountHex;
        }
        String amount = bytesToHex(changeEndian(hexToBytes(amountHex)));

        String lockField = "00000000";
        String hashCodeType = "01000000";
        String preTxData = nVersion + inCount + prevOutHash + prevOutN + scriptSigLength + scriptSig + sequence + outCount + amount + scriptSigLength + scriptSig + lockField + hashCodeType;
        byte[] doubleHash = sha256(sha256(preTxData));
        byte[] revDoubleHash = changeEndian(doubleHash);
        byte[] signature = ECDSA.sign(privateKey, revDoubleHash);
        String finalSign = bytesToHex(signature) + "01";

        String sigLength = Integer.toHexString(finalSign.length() / 2);
        String pubKeyLength = Integer.toHexString((getPublicKeyHex().length() / 2));
        String sigScript = sigLength + finalSign + pubKeyLength + getPublicKeyHex();
        String sigScriptLength = Integer.toHexString(sigScript.length() / 2);
        return nVersion + inCount + prevOutHash + prevOutN + sigScriptLength + sigScript + sequence + outCount + amount + scriptSigLength + scriptSig + lockField;
    }

    public static void main(String[] args) throws Exception {
        String pk = "0ecd20654c2e2be708495853e8da35c664247040c00bd10b9b13e5e86e6a808d";
        Wallet w = new Wallet(pk);
        System.out.println(w.getPublicKeyHex());
        System.out.println(w.getUncompressed_address());

        // Generate Signature
        String transaction = "0100000001be66e10da854e7aea9338c1f91cd489768d1d6d7189f586d7a3613f2a24d5396000000001976a914dd6cce9f255a8cc17bda8ba0373df8e861cb866e88acffffffff0123ce0100000000001976a914a2fd2e039a86dbcf0e1a664729e09e8007f8951088ac0000000001000000";
        byte[] doubleHash = sha256(sha256(transaction));
        byte[] revDoubleHash = changeEndian(doubleHash);
        byte[] signature = ECDSA.sign(Utils.hexToBytes(pk), revDoubleHash);
        String finalSign = bytesToHex(signature) + "01";
        System.out.println("Double Hash: " + bytesToHex(doubleHash));
        System.out.println("Reverse Double Hash: " + bytesToHex(revDoubleHash));
        System.out.println(finalSign);
        System.out.println(finalSign.length());

        // Verify Signature
        boolean isValid = ECDSA.verify(signature, revDoubleHash, hexToBytes(w.getPublicKeyHex()));
        System.out.println(isValid);

        // Generate sigScript (unlocking script)
        String sigLength = Integer.toHexString(finalSign.length() / 2);
        String pubKeyLength = Integer.toHexString((w.getPublicKeyHex().length() / 2));
        String sigScript = sigLength + finalSign + pubKeyLength + w.getPublicKeyHex();
        System.out.println(sigScript);
    }
}