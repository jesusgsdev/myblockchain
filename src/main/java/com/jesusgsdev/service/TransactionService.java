package com.jesusgsdev.service;

import com.jesusgsdev.config.CoinCore;
import com.jesusgsdev.model.Transaction;
import com.jesusgsdev.model.TransactionInput;
import com.jesusgsdev.model.TransactionOutput;
import com.jesusgsdev.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.PrivateKey;

@Service
public class TransactionService {

    @Autowired
    private CoinCore coinConfig;
    
    public boolean processTransaction(Transaction transaction) {

        if(verifiySignature(transaction) == false) {
            System.out.println("#Transaction Signature failed to verify");
            return false;
        }

        //Gathers transaction inputs (Making sure they are unspent):
        for(TransactionInput i : transaction.getInputs()) {
            i.UTXO = coinConfig.getUTXOs().get(i.transactionOutputId);
        }

        //Checks if transaction is valid:
        if(getInputsValue(transaction) < coinConfig.getMinimumTransaction()) {
            System.out.println("Transaction Inputs to small: " + getInputsValue(transaction));
            return false;
        }

        //Generate transaction outputs:
        //get transaction.getValue() of inputs then the left over change:
        float leftOver = getInputsValue(transaction) - transaction.getValue();
        transaction.setTransactionId(calulateHash(transaction));
        //send transaction.getValue() to recipient
        transaction.getOutputs().add(new TransactionOutput( transaction.getRecipient(), transaction.getValue(), transaction.getTransactionId()));
        //send the left over 'change' back to transaction.getSender()
        transaction.getOutputs().add(new TransactionOutput( transaction.getSender(), leftOver, transaction.getTransactionId()));

        //Add outputs to Unspent list
        for(TransactionOutput o : transaction.getOutputs()) {
            coinConfig.getUTXOs().put(o.getId() , o);
        }

        //Remove transaction inputs from UTXO lists as spent:
        for(TransactionInput i : transaction.getInputs()) {
            if(i.UTXO == null) continue; //if Transaction can't be found skip it 
            coinConfig.getUTXOs().remove(i.UTXO.getId());
        }

        return true;
    }

    public float getInputsValue(Transaction transaction) {
        float total = 0;
        for(TransactionInput i : transaction.getInputs()) {
            if(i.UTXO == null) continue; //if Transaction can't be found skip it, This behavior may not be optimal.
            total += i.UTXO.getValue();
        }
        return total;
    }

    public void generateSignature(PrivateKey privateKey, Transaction transaction) {
        String data = StringUtil.getStringFromKey(transaction.getSender()) + 
                StringUtil.getStringFromKey(transaction.getRecipient()) +
                Float.toString(transaction.getValue())	;
        transaction.setSignature(StringUtil.applyECDSASig(privateKey,data));
    }

    public boolean verifiySignature(Transaction transaction) {
        String data = StringUtil.getStringFromKey(transaction.getSender()) + StringUtil.getStringFromKey(transaction.getRecipient()) + Float.toString(transaction.getValue())	;
        return StringUtil.verifyECDSASig(transaction.getSender(), data, transaction.getSignature());
    }

    public float getOutputsValue(Transaction transaction) {
        float total = 0;
        for(TransactionOutput o : transaction.getOutputs()) {
            total += o.getValue();
        }
        return total;
    }

    private String calulateHash(Transaction transaction) {
        //increase the sequence to avoid 2 identical transactions having the same hash
        transaction.setSequence(transaction.getSequence() + 1);
        return StringUtil.applySha256(
                StringUtil.getStringFromKey(transaction.getSender()) +
                        StringUtil.getStringFromKey(transaction.getRecipient()) +
                        Float.toString(transaction.getValue()) + transaction.getSequence()
        );
    }

}

