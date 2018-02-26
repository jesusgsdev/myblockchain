package com.jesusgsdev.rest;

import com.jesusgsdev.model.Transaction;
import com.jesusgsdev.model.Wallet;
import lombok.*;

import java.security.PublicKey;
import java.util.Arrays;
import java.util.HashMap;

@Data
@ToString
public class TransactionRestOutput {

    private String transactionId;
    private String sender;
    private String recipient;
    private float value;
    private byte[] signature;

    public TransactionRestOutput(Transaction t, HashMap<String, Wallet> wallets) {
        this.transactionId = t.getTransactionId();
        this.sender = getPublicKeyId(wallets, t.getSender());
        this.recipient = getPublicKeyId(wallets, t.getRecipient());
        this.value = t.getValue();
        this.signature = t.getSignature();
    }

    private String getPublicKeyId(final HashMap<String, Wallet> wallets, final PublicKey publicKey){
        return wallets.entrySet().stream().filter(kv -> kv.getValue().getPublicKey().equals(publicKey)).findFirst().get().getKey();
    }
}
