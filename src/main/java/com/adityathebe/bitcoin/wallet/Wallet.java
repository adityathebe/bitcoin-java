package com.adityathebe.bitcoin.wallet;

import com.adityathebe.bitcoin.crypto.Sha256;
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

public class Wallet {
    private byte[] privateKey;
    private ECPoint publicKey;
    private String uncompressed_address;

    private static final String RANDOM_NUMBER_ALGORITHM = "SHA1PRNG";
    private static final String RANDOM_NUMBER_ALGORITHM_PROVIDER = "SUN";
    public static final BigInteger MAX_PRIVATE_KEY = new BigInteger("00FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFEBAAEDCE6AF48A03BBFD25E8CD0364140", 16);

    public byte[] getPrivateKey() {
        return privateKey;
    }

    public String getPrivateKeyHex() {
        return new BigInteger(1, this.privateKey).toString(16);
    }

    public void setPrivateKey(byte[] privateKey) {
        this.privateKey = privateKey;
    }

    public ECPoint getPublicKey() {
        return publicKey;
    }

    public String getPublicKeyHex() {
        return "04" + this.publicKey.getXCoord().toString() + this.publicKey.getYCoord().toString();
    }

    public void setPublicKey(ECPoint publicKey) {
        this.publicKey = publicKey;
    }

    public String getUncompressed_address() {
        return uncompressed_address;
    }

    public void setUncompressed_address(String uncompressed_address) {
        this.uncompressed_address = uncompressed_address;
    }

    public Wallet() {
    }

    public void init() throws NoSuchAlgorithmException {
        this.privateKey = Wallet.genPrivateKey();
        this.publicKey = Wallet.genPublicKey(this.privateKey);
        this.uncompressed_address = Wallet.genAddress(this.publicKey);
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
        ECNamedCurveParameterSpec spec = ECNamedCurveTable.getParameterSpec("SECP256K1");
        ECPoint pointQ = spec.getG().multiply(new BigInteger(1, privateKey));
        return pointQ;
    }

    public static String genAddress(String pubKey) throws NoSuchAlgorithmException {
        Security.addProvider(new BouncyCastleProvider());
        MessageDigest md = MessageDigest.getInstance("RIPEMD160");
        byte[] addressByte = md.digest(Sha256.getHash(pubKey));
        String address = Utils.bytesToHex(addressByte);
        String mainNetworkAddress = "00" + address;

        String doubleSha256 = Utils.bytesToHex(Sha256.getHash(Sha256.getHash(mainNetworkAddress)));
        String checkSum = doubleSha256.substring(0, 8);
        String hexAddress = mainNetworkAddress + checkSum;

        return Base58.encode(Utils.hexToBytes(hexAddress));
    }

    public static String genAddress(ECPoint pubKey) throws NoSuchAlgorithmException {
        String pubKeyStr = "04" + pubKey.getXCoord().toString() + pubKey.getYCoord().toString();
        return Wallet.genAddress(pubKeyStr);
    }

    public static void main(String[] args) throws NoSuchAlgorithmException {
        Wallet w = new Wallet();
        w.init();
        System.out.println(w.getPrivateKeyHex());
        System.out.println(w.getPublicKeyHex());
        System.out.println(w.getUncompressed_address());
    }
}