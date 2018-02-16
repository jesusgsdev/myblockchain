package com.jesusgsdev.rest;

import com.jesusgsdev.model.Block;
import lombok.*;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

@Data
@ToString
public class BlockRestOutput {

    private String hash;
    private String previousHash;
    private String merkleRoot;
    private List<TransactionRestOutput> transactions;
    private long timeStamp;
    private int nonce;

    public BlockRestOutput(Block block){
        this.hash = block.getHash();
        this.previousHash = block.getPreviousHash();
        this.merkleRoot = block.getMerkleRoot();
        this.timeStamp = block.getTimeStamp();
        this.nonce = block.getNonce();
        this.transactions = block.getTransactions().stream().map(TransactionRestOutput::new).collect(Collectors.toList());
    }

}
