package com.jesusgsdev.service;

import com.jesusgsdev.config.CoinCore;
import com.jesusgsdev.model.Block;
import com.jesusgsdev.model.Transaction;
import com.jesusgsdev.model.TransactionOutput;
import com.jesusgsdev.model.Wallet;
import com.jesusgsdev.rest.BlockRestOutput;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BlockchainService {

    private static final Logger LOGGER = LoggerFactory.getLogger(BlockchainService.class);

    @Autowired
    private CoinCore coinCore;

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private BlockService blockService;

    @Autowired
    private WalletService walletService;

    //Add transactions to this block
    public String initialize() {
        if(coinCore.getBlockchain().size() != 0){
            return "The blockchain has been initialized";
        }

        //Create wallets:
        String initialWalletId = walletService.createWallet();
        Wallet walletA = coinCore.getWallets().get(initialWalletId);
        Wallet coinbase = coinCore.getWallets().get(walletService.createWallet());

        //create genesis transaction, which sends 100 NoobCoin to walletA:
        Transaction genesisTransaction = new Transaction(coinbase.getPublicKey(), walletA.getPublicKey(), 100f, null);
        transactionService.generateSignature(coinbase.getPrivateKey(), genesisTransaction);//manually sign the genesis transaction
        //manually set the transaction id
        genesisTransaction.setTransactionId("0");
        //manually add the Transactions Output
        TransactionOutput genesisTXO = new TransactionOutput(genesisTransaction.getRecipient(), genesisTransaction.getValue(), genesisTransaction.getTransactionId());
        genesisTransaction.getOutputs().add(genesisTXO);
        //its important to store our first transaction in the UTXOs list.
        coinCore.getUTXOs().put(genesisTransaction.getOutputs().get(0).getId(), genesisTransaction.getOutputs().get(0));

        LOGGER.info("Creating and Mining Genesis block... ");
        Block genesis = new Block("0");
        blockService.addTransaction(genesis, genesisTransaction);
        blockService.addBlock(genesis);
        blockService.mineBlock();

        return initialWalletId;
    }

    public List<BlockRestOutput> getBlockChain() {
        return coinCore.getBlockchain().stream().map(BlockRestOutput::new).collect(Collectors.toList());
    }

    public Boolean isChainValid() {
        Block currentBlock;
        Block previousBlock;
        String hashTarget = new String(new char[coinCore.getDifficulty()]).replace('\0', '0');

        //loop through blockchain to check hashes:
        for(int i=1; i < coinCore.getBlockchain().size(); i++) {
            currentBlock = coinCore.getBlockchain().get(i);
            previousBlock = coinCore.getBlockchain().get(i-1);
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
            if(!currentBlock.getHash().substring( 0, coinCore.getDifficulty()).equals(hashTarget)) {
                LOGGER.warn("This block hasn't been mined");
                return false;
            }

        }
        return true;
    }

}
