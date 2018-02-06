package com.jesusgsdev.service;

import com.jesusgsdev.config.CoinConfig;
import com.jesusgsdev.model.Transaction;
import com.jesusgsdev.model.TransactionInput;
import com.jesusgsdev.model.TransactionOutput;
import com.jesusgsdev.model.Wallet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.PublicKey;
import java.util.ArrayList;
import java.util.Map;

@Service
public class WalletService {

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private CoinConfig coinConfig;

    public float getBalance(Wallet wallet) {
        float total = 0;
        for (Map.Entry<String, TransactionOutput> item: coinConfig.getUTXOs().entrySet()){
            TransactionOutput UTXO = item.getValue();
            if(UTXO.isMine(wallet.getPublicKey())) { //if output belongs to me ( if coins belong to me )
                coinConfig.getUTXOs().put(UTXO.getId(),UTXO); //add it to our list of unspent transactions.
                total += UTXO.getValue() ;
            }
        }
        return total;
    }

    public Transaction sendFunds(Wallet wallet, PublicKey _recipient, float value ) {
        if(getBalance(wallet) < value) {
            System.out.println("#Not Enough funds to send transaction. Transaction Discarded.");
            return null;
        }
        ArrayList<TransactionInput> inputs = new ArrayList<>();

        float total = 0;
        for (Map.Entry<String, TransactionOutput> item: coinConfig.getUTXOs().entrySet()){
            TransactionOutput UTXO = item.getValue();
            total += UTXO.getValue();
            inputs.add(new TransactionInput(UTXO.getId()));
            if(total > value) break;
        }

        Transaction newTransaction = new Transaction(wallet.getPublicKey(), _recipient , value, inputs);
        transactionService.generateSignature(wallet.getPrivateKey(), newTransaction);

        for(TransactionInput input: inputs){
            wallet.getUTXOs().remove(input.transactionOutputId);
        }

        return newTransaction;
    }

}
