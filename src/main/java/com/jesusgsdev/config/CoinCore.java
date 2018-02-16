package com.jesusgsdev.config;

import com.jesusgsdev.model.Block;
import com.jesusgsdev.model.TransactionOutput;
import com.jesusgsdev.model.Wallet;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;

@Getter
@Service
public class CoinCore {

    private static final Logger LOGGER = LoggerFactory.getLogger(CoinCore.class);

    private ArrayList<Block> blockchain = new ArrayList<>();
    private ArrayList<Block> nonMinedBlocks = new ArrayList<>();
    private HashMap<String,TransactionOutput> UTXOs = new HashMap<>();
    private HashMap<String, Wallet> wallets = new HashMap<>();

    @Value("${application.coin.difficulty}")
    private Integer difficulty;

    @Value("${application.coin.minimum_transaction}")
    private Float minimumTransaction;

    public CoinCore() { }

}
