package com.jesusgsdev.service;

import com.jesusgsdev.model.Block;
import com.jesusgsdev.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class MinerService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MinerService.class);

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
