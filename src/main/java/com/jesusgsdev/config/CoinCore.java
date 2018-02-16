package com.jesusgsdev.config;

import com.jesusgsdev.model.Block;
import com.jesusgsdev.model.TransactionOutput;
import com.jesusgsdev.model.Wallet;
import com.jesusgsdev.service.BlockService;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

@Getter
@Service
public class CoinCore {

    private static final Logger LOGGER = LoggerFactory.getLogger(CoinCore.class);

    private ArrayList<Block> blockchain = new ArrayList<>();
    private HashMap<String,TransactionOutput> UTXOs = new HashMap<>();
    private HashMap<String, Wallet> wallets = new HashMap<>();

    @Value("${application.coin.difficulty}")
    private Integer difficulty;

    @Value("${application.coin.minimum_transaction}")
    private Float minimumTransaction;

    @Autowired
    private BlockService blockService;

    public CoinCore() { }

    public String createWallet(){
        Wallet wallet = new Wallet();
        String uuid = UUID.randomUUID().toString();
        wallets.put(uuid, wallet);
        return uuid;
    }

    public Boolean isChainValid() {
        Block currentBlock;
        Block previousBlock;
        String hashTarget = new String(new char[difficulty]).replace('\0', '0');

        //loop through blockchain to check hashes:
        for(int i=1; i < blockchain.size(); i++) {
            currentBlock = blockchain.get(i);
            previousBlock = blockchain.get(i-1);
            //compare registered hash and calculated hash:
            if(!currentBlock.getHash().equals(currentBlock.calculateHash()) ){
                LOGGER.warn("Current Hashes not equal");
                return false;
            }
            //compare previous hash and registered previous hash
            if(!previousBlock.getHash().equals(currentBlock.getPreviousHash()) ) {
                LOGGER.warn("Previous Hashes not equal");
                return false;
            }
            //check if hash is solved
            if(!currentBlock.getHash().substring( 0, difficulty).equals(hashTarget)) {
                LOGGER.warn("This block hasn't been mined");
                return false;
            }

        }
        return true;
    }

    public void addBlock(Block newBlock) {
        blockService.mineBlock(newBlock, difficulty);
        blockchain.add(newBlock);
    }
    
}
