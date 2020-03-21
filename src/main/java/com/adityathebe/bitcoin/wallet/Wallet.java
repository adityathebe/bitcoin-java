package com.adityathebe.bitcoin.wallet;

import com.adityathebe.bitcoin.crypto.Ripemd160;
import com.adityathebe.bitcoin.crypto.Sha256;
import com.adityathebe.bitcoin.utils.Base58;
import com.adityathebe.bitcoin.utils.Utils;

import java.security.NoSuchAlgorithmException;

public class Wallet {
    String privateKey;
    String publicKey;
    String address;


    public Wallet(String privateKey) {


    }


    public static String pubKeyToAddress(String pubKey) throws NoSuchAlgorithmException {
        byte[] addressByte = Ripemd160.getHash(Sha256.getHash(pubKey));
        String address = Utils.bytesToHex(addressByte);
        String mainNetworkAddress = "00" + address;

        String doubleSha256 = Utils.bytesToHex(Sha256.getHash(Utils.bytesToHex(Sha256.getHash(mainNetworkAddress))));
        String checkSum = doubleSha256.substring(0, 8);
        String hexAddress = mainNetworkAddress + checkSum;

        return Base58.encode(Utils.hexToBytes(hexAddress));
    }

    public static void main(String[] args) throws NoSuchAlgorithmException {
        String pubKey = "022d077614408e7c1e35a42991928b1fe8e859fa493c0d85df04a58d90dfc8f540";
        System.out.println(pubKeyToAddress(pubKey));
    }
}
