package com.adityathebe.bitcoin.wallet;

import com.adityathebe.bitcoin.crypto.ECDSA;
import com.adityathebe.bitcoin.utils.Base58;
import org.bouncycastle.jce.ECNamedCurveTable;
import org.bouncycastle.jce.spec.ECNamedCurveParameterSpec;
import org.bouncycastle.math.ec.ECPoint;

import java.math.BigInteger;
import java.security.SecureRandom;

import static com.adityathebe.bitcoin.crypto.Crypto.*;
import static com.adityathebe.bitcoin.script.ScriptOpCodes.*;
import static com.adityathebe.bitcoin.utils.Utils.*;

public class Wallet {
    private static String MAINNET_PUBKEY_PREFIX = "00";
    private static String TESTNET_PUBKEY_PREFIX = "6F";
    private static final String RANDOM_NUMBER_ALGORITHM = "SHA1PRNG";
    private static final String RANDOM_NUMBER_ALGORITHM_PROVIDER = "SUN";
    private static final BigInteger MAX_PRIVATE_KEY = new BigInteger("00FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFEBAAEDCE6AF48A03BBFD25E8CD0364140", 16);
    private static final ECNamedCurveParameterSpec secp256k1Spec = ECNamedCurveTable.getParameterSpec("secp256k1");


    private byte[] privateKey;
    private ECPoint publicKey;
    private String address;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

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
        return getCompressedPubKey(publicKey);
    }

    public Wallet() {
        init();
    }

    public Wallet(String privateKeyHex) {
        privateKey = hexToBytes(privateKeyHex);
        init();
    }

    public void init() {
        if (this.privateKey == null) {
            this.privateKey = genPrivateKey();
        }
        this.publicKey = genPublicKey(this.privateKey);
        this.address = genAddress(this.publicKey);
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

    /**
     * Returns hex value of public key in X9.63 uncompressed form
     */
    public static String genAddress(String pubKey) {
        byte[] address = ripemd160(sha256(pubKey));
        String mainNetworkAddress = TESTNET_PUBKEY_PREFIX + bytesToHex(address);
        String doubleSha256 = bytesToHex(hash256(mainNetworkAddress));
        String checkSum = doubleSha256.substring(0, 8);
        String hexAddress = mainNetworkAddress + checkSum;
        return Base58.encode(hexToBytes(hexAddress));
    }

    /**
     * Returns hex value of public key in X9.63 uncompressed form
     */
    public static String genAddress(ECPoint pubKey) {
        return genAddress(getCompressedPubKey(pubKey));
    }

    public static String getHashFromAddress(String address) throws Exception {
        String dec = bytesToHex(Base58.decodeChecked(address));
        return dec.substring(2);
    }

    private static String getCompressedPubKey(ECPoint pubKey) {
        BigInteger y = new BigInteger(pubKey.normalize().getYCoord().toString(), 16);
        if (y.mod(BigInteger.valueOf(2L)).equals(BigInteger.valueOf(0))) {
            return "02" + pubKey.normalize().getXCoord().toString();
        }
        return "03" + pubKey.normalize().getXCoord().toString();
    }

    private static String getUncompressedPubKey(ECPoint pubKey) {
        return "04" + pubKey.normalize().getXCoord().toString() + pubKey.normalize().getYCoord().toString();
    }

    /**
     * @param address P2PKH Legacy address
     * @param satoshi Amount in Satoshi
     * @return The hex encoded transaction
     */
    public String createTransaction(String address, int satoshi, String parentTxHash, String prevScriptPubKey) throws Exception {
        String nVersion = "01000000";
        String inCount = "01";

        // Input
        String prevOutHash = bytesToHex(changeEndian(hexToBytes(parentTxHash)));
        String prevOutN = "00000000";
        String prevScriptPubKeyLength = Integer.toHexString(prevScriptPubKey.length() / 2);
        String sequence = "ffffffff";

        // Output
        String outCount = "01";
        String amountHex = Integer.toHexString(satoshi);
        while (amountHex.length() < 16) {
            amountHex = "0" + amountHex;
        }
        String amount = bytesToHex(changeEndian(hexToBytes(amountHex)));

        // Finalize the output
        String hashFromAddress = getHashFromAddress(address);
        String hashLength = Integer.toHexString((hashFromAddress.length() / 2));
        String scriptPubKey = Integer.toHexString(OP_DUP) + Integer.toHexString(OP_HASH160) + hashLength + hashFromAddress + Integer.toHexString(OP_EQUALVERIFY) + Integer.toHexString(OP_CHECKSIG);
        String scriptPubKeyLength = Integer.toHexString(scriptPubKey.length() / 2);

        String lockField = "00000000";
        String hashCodeType = "01000000";

        // Craft temp tx data
        String preTxData = nVersion + inCount + prevOutHash + prevOutN + prevScriptPubKeyLength + prevScriptPubKey + sequence + outCount + amount + scriptPubKeyLength + scriptPubKey + lockField + hashCodeType;
        System.out.println("\nPreTxData: " + preTxData);
        byte[] doubleHash = hash256(preTxData);
        byte[] signature = ECDSA.sign(privateKey, doubleHash);
        String signatureHex = bytesToHex(signature) + "01";
        System.out.println("Verified: " + ECDSA.verify(signature, doubleHash, hexToBytes(getPublicKeyHex())));

        // Create actual scriptSig
        String sigLength = Integer.toHexString(signatureHex.length() / 2);
        String pubKeyLength = Integer.toHexString((getPublicKeyHex().length() / 2));
        String scriptSig = sigLength + signatureHex + pubKeyLength + getPublicKeyHex();
        String scriptSigLength = Integer.toHexString(scriptSig.length() / 2);

        return nVersion + inCount + prevOutHash + prevOutN + scriptSigLength + scriptSig + sequence + outCount + amount + scriptPubKeyLength + scriptPubKey + lockField;
    }

    public static void main(String[] args) throws Exception {
        Wallet w = new Wallet("080BE07427F2DA803518329B24FB9BEA2890E26B8141AFEA840902DCD1ED6F09");
        System.out.println("Private Key (Hex): " + bytesToHex(w.getPrivateKey()));
        System.out.println("Public Key (Hex): " + w.getPublicKeyHex());
        System.out.println("Address: " + w.getAddress());

        System.out.println("\nWallet 2\n");
        Wallet w2 = new Wallet("2C414569FEE1D8356459AC512DC32D0E8F808E25B7C262E538D836FCC843FCC2");
        System.out.println("Private Key (Hex): " + bytesToHex(w2.getPrivateKey()));
        System.out.println("Public Key (Hex): " + w2.getPublicKeyHex());
        System.out.println("Address: " + w2.getAddress());

        String tx = w2.createTransaction(
            w.getAddress(),
            400,
            "ece2880671e7795568a8793437eac5809f4c47525a1814acde6f010fe12f0a28",
            "76a914c974090512e9e5839ae332f9d5e7e4434212d79588ac");
        System.out.println("\nTx (Raw):" + tx);

        System.out.println("\nTx Hash: " + bytesToHex(changeEndian(hash256(tx))));
    }
}