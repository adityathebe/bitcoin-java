package com.adityathebe.bitcoin.wallet;

import com.adityathebe.bitcoin.core.Address;
import com.adityathebe.bitcoin.core.ECPrivateKey;
import com.adityathebe.bitcoin.core.ECPublicKey;
import com.adityathebe.bitcoin.crypto.ECDSA;
import com.adityathebe.bitcoin.crypto.ECDSASignature;
import com.adityathebe.bitcoin.utils.Base58;

import static com.adityathebe.bitcoin.crypto.Crypto.hash256;
import static com.adityathebe.bitcoin.script.ScriptOpCodes.*;
import static com.adityathebe.bitcoin.utils.Utils.*;

public class Wallet {
    private ECPrivateKey privateKey;
    private ECPublicKey publicKey;
    private Address address;

    public Wallet() {
        init();
    }

    public Wallet(String privateKeyHex) {
        privateKey = new ECPrivateKey(privateKeyHex);
        init();
    }

    private void init() {
        if (this.privateKey == null) {
            this.privateKey = new ECPrivateKey();
        }
        this.publicKey = new ECPublicKey(this.privateKey);
        this.address = new Address(this.publicKey);
    }

    public ECPrivateKey getPrivateKey() {
        return privateKey;
    }

    public Address getAddress() {
        return address;
    }

    public ECPublicKey getPublicKey() {
        return publicKey;
    }

    public static String getHashFromAddress(String address) throws Exception {
        String dec = bytesToHex(Base58.decodeChecked(address));
        return dec.substring(2);
    }

    /**
     * @param address P2PKH Legacy address
     * @param satoshi Amount in Satoshi
     * @return The hex encoded transaction
     */
    public String createTransaction(String address, int satoshi, String parentTxHash, String inputIdx, String prevScriptPubKey) throws Exception {
        String nVersion = "01000000";
        String inCount = "01";

        // Input
        String prevOutHash = bytesToHex(changeEndian(hexToBytes(parentTxHash)));
        String prevOutN = inputIdx;
        String prevScriptPubKeyLength = Integer.toHexString(prevScriptPubKey.length() / 2);
        String sequence = "ffffffff";

        // Output
        String outCount = "01";
        String amount = Integer.toHexString(satoshi);
        while (amount.length() < 16) {
            amount = "0" + amount;
        }
        amount = bytesToHex(changeEndian(hexToBytes(amount)));

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
        ECDSASignature signature = ECDSA.sign(privateKey, doubleHash);
        String finalSignature = signature.getDerEncoded() + "01";

        System.out.println("Sig verified: " + ECDSA.verify(signature, getPublicKey(), doubleHash));

        // Create actual scriptSig
        String sigLength = Integer.toHexString(finalSignature.length() / 2);
        String pubKeyLength = Integer.toHexString((publicKey.getCompressed().length() / 2));
        String scriptSig = sigLength + finalSignature + pubKeyLength + publicKey.getCompressed();
        String scriptSigLength = Integer.toHexString(scriptSig.length() / 2);

        return nVersion + inCount + prevOutHash + prevOutN + scriptSigLength + scriptSig + sequence + outCount + amount + scriptPubKeyLength + scriptPubKey + lockField;
    }

    public static void main(String[] args) throws Exception {
        System.out.println("Wallet");
        Wallet w = new Wallet("3E402774803FCBCA1CA7CC841E55A659182CCE5FF3A249275DA7EDC32FDF8BB2");
        System.out.println("Private Key (Hex): " + w.getPrivateKey().getHex());
        System.out.println("Public Key (Hex): " + w.getPublicKey().getCompressed());
        System.out.println("Address: " + w.getAddress().getTestNetAddressHex());

        String tx = w.createTransaction(
            "2NGZrVvZG92qGYqzTLjCAewvPZ7JE8S8VxE",
            9900,
            "7e5c93f34b09877abea9b2f1793469e54d196aae8f7a434351bfd3ca4c428410",
            "00000000",
            "76a914745e794502c6307f10e71c66e066c8d7f714066688ac");
        System.out.println("Tx (Raw):" + tx);
        System.out.println("Tx Hash: " + bytesToHex(changeEndian(hash256(tx))));
    }
}