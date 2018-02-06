package com.jesusgsdev.rest;

import com.jesusgsdev.model.Transaction;
import lombok.*;

import java.util.Arrays;

@Data
@ToString
public class TransactionRestOutput {

    private String transactionId;
    private String sender;
    private String recipient;
    private float value;
    private byte[] signature;

    public TransactionRestOutput(Transaction t) {
        this.transactionId = t.getTransactionId();
        this.sender = Arrays.toString(t.getSender().getEncoded());
        this.recipient = Arrays.toString(t.getRecipient().getEncoded());
        this.value = t.getValue();
        this.signature = t.getSignature();
    }
}
