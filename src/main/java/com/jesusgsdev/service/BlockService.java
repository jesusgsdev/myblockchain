package com.jesusgsdev.service;

import com.jesusgsdev.config.CoinCore;
import com.jesusgsdev.model.Block;
import com.jesusgsdev.model.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static java.util.Objects.isNull;

@Service
public class BlockService {

    private static final Logger LOGGER = LoggerFactory.getLogger(BlockService.class);

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private CoinCore coinCore;

    @Autowired
    private MinerService minerService;

    //Add transactions to this block
    public Boolean addTransaction(Block block, Transaction transaction) {
        //process transaction and check if valid, unless block is genesis block then ignore.
        if(isNull(transaction)) return false;
        if(!block.getPreviousHash().equals("0")) {
            if(transactionService.processTransaction(transaction) != true) {
                LOGGER.warn("Transaction failed to process. Discarded.");
                return false;
            }
        }

        block.getTransactions().add(transaction);
        LOGGER.info("Transaction Successfully added to Block");
        return true;
    }

    public String mineBlock() {
        if(coinCore.getNonMinedBlocks().isEmpty()){
            LOGGER.info("There is not blocks to mine");
            return "There is not blocks to mine";
        }

        Block blockToMine = coinCore.getNonMinedBlocks().get(0);
        minerService.mineBlock(blockToMine, coinCore.getDifficulty());
        coinCore.getBlockchain().add(blockToMine);
        coinCore.getNonMinedBlocks().remove(blockToMine);

        return "Block mined with hash: " + blockToMine.getHash();
    }

    public Block prepareNewBlock(){
        return new Block(coinCore.getBlockchain().get(coinCore.getBlockchain().size() - 1).getHash());
    }

    public void addBlock(Block newBlock) {
        if(!newBlock.getTransactions().isEmpty()) {
            coinCore.getNonMinedBlocks().add(newBlock);
        }
    }

}
