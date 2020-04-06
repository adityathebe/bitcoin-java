package com.adityathebe.bitcoin.transaction;

public class Transaction {
    String txHash;

    /**
     * double-SHA256 hash of the raw transaction
     */
    String txId;

    /**
     * Transaction format version
     */
    Integer nVersion;

    Integer txInputCount;
    TransactionInput[] vin;
    Integer txOutputCount;
    TransactionOutput[] vout;

    public void setTxId(String txId) {
        this.txId = txId;
    }

    /**
     * point in time past which the transaction should
     * be included in a block.
     * UNIX format or as a block number
     * Value Description
     * <p>
     * 0 Always locked.
     * < 5 ∗ 108 Block number at which transaction is locked.
     * ≥ 5 ∗ 108 UNIX timestamp at which transaction is locked
     */
    Long nLockTime;
}
