package com.adityathebe.bitcoin.core;

import static com.adityathebe.bitcoin.utils.Utils.*;

public class Coin {
    private long value;

    public Coin(long satoshis) {
        this.value = satoshis;
    }

    /**
     * @return Returns 8byte little-endian hex string
     */
    String toHex() {
        String amountHex = Long.toHexString(this.value);
        while (amountHex.length() < 16) {
            amountHex = "0" + amountHex;
        }
        return bytesToHex(changeEndian(hexToBytes(amountHex)));
    }
}
