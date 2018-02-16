package com.jesusgsdev.util;

import com.jesusgsdev.config.CoinCore;
import com.jesusgsdev.model.Block;
import com.jesusgsdev.model.Transaction;
import com.jesusgsdev.model.TransactionOutput;
import com.jesusgsdev.model.Wallet;
import com.jesusgsdev.service.BlockService;
import com.jesusgsdev.service.BlockchainService;
import com.jesusgsdev.service.TransactionService;
import com.jesusgsdev.service.WalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TestService {

    @Autowired
    private CoinCore coinConfig;

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private WalletService walletService;

    @Autowired
    private BlockService blockService;

    @Autowired
    private BlockchainService blockchainService;

    public void mainTest(){
        //Create wallets:
        Wallet walletA = coinConfig.getWallets().get(walletService.createWallet());
        Wallet walletB = coinConfig.getWallets().get(walletService.createWallet());
        Wallet coinbase = coinConfig.getWallets().get(walletService.createWallet());

        //create genesis transaction, which sends 100 NoobCoin to walletA:
        Transaction genesisTransaction = new Transaction(coinbase.getPublicKey(), walletA.getPublicKey(), 100f, null);
        transactionService.generateSignature(coinbase.getPrivateKey(), genesisTransaction);//manually sign the genesis transaction
        //manually set the transaction id
        genesisTransaction.setTransactionId("0");
        //manually add the Transactions Output
        TransactionOutput genesisTXO = new TransactionOutput(genesisTransaction.getRecipient(), genesisTransaction.getValue(), genesisTransaction.getTransactionId());
        genesisTransaction.getOutputs().add(genesisTXO);
        //its important to store our first transaction in the UTXOs list.
        coinConfig.getUTXOs().put(genesisTransaction.getOutputs().get(0).getId(), genesisTransaction.getOutputs().get(0));

        System.out.println("Creating and Mining Genesis block... ");
        Block genesis = new Block("0");
        blockService.addTransaction(genesis, genesisTransaction);
        blockService.addBlock(genesis);

        //testing
        Block block1 = new Block(genesis.getHash());
        System.out.println("\nWalletA's balance is: " + walletService.getBalance(walletA));
        System.out.println("\nWalletA is Attempting to send funds (40) to WalletB...");
        Transaction walletASend40ToWalletB =  walletService.sendFunds(walletA, walletB.getPublicKey(), 40f);
        blockService.addTransaction(block1, walletASend40ToWalletB);
        blockService.addBlock(block1);
        System.out.println("\nWalletA's balance is: " + walletService.getBalance(walletA));
        System.out.println("WalletB's balance is: " + walletService.getBalance(walletB));

        Block block2 = new Block(block1.getHash());
        System.out.println("\nWalletA Attempting to send more funds (1000) than it has...");
        blockService.addTransaction(block2, walletService.sendFunds(walletA, walletB.getPublicKey(), 1000f));
        blockService.addBlock(block2);
        System.out.println("\nWalletA's balance is: " + walletService.getBalance(walletA));
        System.out.println("WalletB's balance is: " + walletService.getBalance(walletB));

        Block block3 = new Block(block2.getHash());
        System.out.println("\nWalletB is Attempting to send funds (20) to WalletA...");
        blockService.addTransaction(block3, walletService.sendFunds(walletB, walletA.getPublicKey(), 20f));
        System.out.println("\nWalletA's balance is: " + walletService.getBalance(walletA));
        System.out.println("WalletB's balance is: " + walletService.getBalance(walletB));

        blockchainService.isChainValid();
    }
}
