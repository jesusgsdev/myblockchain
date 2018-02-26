package com.jesusgsdev.service;

import com.jesusgsdev.config.CoinCore;
import com.jesusgsdev.model.Transaction;
import com.jesusgsdev.model.TransactionInput;
import com.jesusgsdev.model.TransactionOutput;
import com.jesusgsdev.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.PrivateKey;

import static java.util.Objects.nonNull;

@Service
public class TransactionService {

    private static final Logger LOGGER = LoggerFactory.getLogger(TransactionService.class);

    @Autowired
    private CoinCore coinCore;

    public boolean processTransaction(Transaction transaction) {

        if(!verifiySignature(transaction)) {
            LOGGER.warn("#Transaction Signature failed to verify");
            return false;
        }

        //Gathers transaction inputs (Making sure they are unspent):
        for(TransactionInput i : transaction.getInputs()) {
            i.UTXO = coinCore.getUTXOs().get(i.transactionOutputId);
        }

        //Checks if transaction is valid:
        if(getInputsValue(transaction) < coinCore.getMinimumTransaction()) {
            LOGGER.info("Transaction Inputs to small: " + getInputsValue(transaction));
            return false;
        }

        //Generate transaction outputs:
        //get transaction.getValue() of inputs then the left over change:
        float leftOver = getInputsValue(transaction) - transaction.getValue();
        transaction.setTransactionId(calculateHash(transaction));
        //send transaction.getValue() to recipient
        transaction.getOutputs().add(new TransactionOutput( transaction.getRecipient(), transaction.getValue(), transaction.getTransactionId()));
        //send the left over 'change' back to transaction.getSender()
        transaction.getOutputs().add(new TransactionOutput( transaction.getSender(), leftOver, transaction.getTransactionId()));

        //Add outputs to Unspent list
        transaction.getOutputs().forEach(o -> coinCore.getUTXOs().put(o.getId() , o));

        //Remove transaction inputs from UTXO lists as spent:
        transaction.getInputs()
                .stream()
                .filter(i -> nonNull(i.UTXO)) //if Transaction can't be found skip it
                .forEach(i -> coinCore.getUTXOs().remove(i.UTXO.getId()));

        return true;
    }

    public float getInputsValue(Transaction transaction) {
        return (float)transaction.getInputs()
                .stream()
                .filter(i -> nonNull(i.UTXO)) //if Transaction can't be found skip it, This behavior may not be optimal.
                .mapToDouble(i -> i.UTXO.getValue())
                .sum();
    }

    public void generateSignature(PrivateKey privateKey, Transaction transaction) {
        String data = StringUtil.getStringFromKey(transaction.getSender()) + 
                StringUtil.getStringFromKey(transaction.getRecipient()) +
                Float.toString(transaction.getValue())    ;
        transaction.setSignature(StringUtil.applyECDSASig(privateKey,data));
    }

    public boolean verifiySignature(Transaction transaction) {
        String data = StringUtil.getStringFromKey(transaction.getSender()) + StringUtil.getStringFromKey(transaction.getRecipient()) + Float.toString(transaction.getValue())    ;
        return StringUtil.verifyECDSASig(transaction.getSender(), data, transaction.getSignature());
    }

    public float getOutputsValue(Transaction transaction) {
        float total = 0;
        for(TransactionOutput o : transaction.getOutputs()) {
            total += o.getValue();
        }
        return total;
    }

    private String calculateHash(Transaction transaction) {
        //increase the sequence to avoid 2 identical transactions having the same hash
        transaction.setSequence(transaction.getSequence() + 1);
        return StringUtil.applySha256(
                StringUtil.getStringFromKey(transaction.getSender()) +
                        StringUtil.getStringFromKey(transaction.getRecipient()) +
                        Float.toString(transaction.getValue()) + transaction.getSequence()
        );
    }

}

