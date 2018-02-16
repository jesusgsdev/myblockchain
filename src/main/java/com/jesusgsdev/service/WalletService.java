package com.jesusgsdev.service;

import com.jesusgsdev.config.CoinCore;
import com.jesusgsdev.model.Transaction;
import com.jesusgsdev.model.TransactionInput;
import com.jesusgsdev.model.TransactionOutput;
import com.jesusgsdev.model.Wallet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.PublicKey;
import java.util.ArrayList;
import java.util.Map;

@Service
public class WalletService {

    private static final Logger LOGGER = LoggerFactory.getLogger(WalletService.class);

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private CoinCore coinCore;

    public float getBalance(Wallet wallet) {
        float total = 0;
        for (Map.Entry<String, TransactionOutput> item: coinCore.getUTXOs().entrySet()){
            TransactionOutput UTXO = item.getValue();
            if(UTXO.isMine(wallet.getPublicKey())) { //if output belongs to me ( if coins belong to me )
                coinCore.getUTXOs().put(UTXO.getId(),UTXO); //add it to our list of unspent transactions.
                total += UTXO.getValue() ;
            }
        }
        return total;
    }

    public Transaction sendFunds(final Wallet wallet, PublicKey recipient, float value ) {
        if(getBalance(wallet) < value) {
            LOGGER.warn("Not Enough funds to send transaction. Transaction Discarded");
            return null;
        }
        ArrayList<TransactionInput> inputs = new ArrayList<>();

        float total = 0;
        for (Map.Entry<String, TransactionOutput> item: coinCore.getUTXOs().entrySet()){
            TransactionOutput UTXO = item.getValue();
            total += UTXO.getValue();
            inputs.add(new TransactionInput(UTXO.getId()));
            if(total > value) break;
        }

        Transaction newTransaction = new Transaction(wallet.getPublicKey(), recipient , value, inputs);
        transactionService.generateSignature(wallet.getPrivateKey(), newTransaction);

        inputs.forEach(input -> wallet.getUTXOs().remove(input.transactionOutputId));

        return newTransaction;
    }

}
