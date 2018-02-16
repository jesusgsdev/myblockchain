package com.jesusgsdev.service;

import com.jesusgsdev.model.Block;
import com.jesusgsdev.model.Transaction;
import com.jesusgsdev.util.StringUtil;
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

    //Add transactions to this block
    public boolean addTransaction(Block block, Transaction transaction) {
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

    //Increases nonce value until hash target is reached.
    public void mineBlock(Block block, int difficulty) {
        block.setMerkleRoot(StringUtil.getMerkleRoot(block.getTransactions()));
        String target = StringUtil.getDificultyString(difficulty); //Create a string with difficulty * "0"
        while(!block.getHash().substring( 0, difficulty).equals(target)) {
            block.setNonce(block.getNonce() + 1);
            block.setHash(block.calculateHash());
        }
        LOGGER.info("Block Mined!!! : " + block.getHash());
    }

}
