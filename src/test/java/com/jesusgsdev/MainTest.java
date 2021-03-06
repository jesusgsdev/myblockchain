package com.jesusgsdev;

import com.jesusgsdev.config.CoinCore;
import com.jesusgsdev.model.Block;
import com.jesusgsdev.model.Transaction;
import com.jesusgsdev.model.TransactionOutput;
import com.jesusgsdev.model.Wallet;
import com.jesusgsdev.service.BlockService;
import com.jesusgsdev.service.BlockchainService;
import com.jesusgsdev.service.TransactionService;
import com.jesusgsdev.service.WalletService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.UUID;

@WebAppConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
public class MainTest {

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

    @Test
    public void mainTest(){
        //Create wallets:
        Wallet walletA = new Wallet(UUID.randomUUID().toString());
        Wallet walletB = new Wallet(UUID.randomUUID().toString());
        Wallet coinbase = new Wallet(UUID.randomUUID().toString());

        //create genesis transaction, which sends 100 NoobCoin to walletA:
        Transaction genesisTransaction = new Transaction(coinbase.getPublicKey(), walletA.getPublicKey(), 100f, null);
        transactionService.generateSignature(coinbase.getPrivateKey(), genesisTransaction);//manually sign the genesis transaction
        genesisTransaction.setTransactionId("0"); //manually set the transaction id
        genesisTransaction.getOutputs().add(new TransactionOutput(genesisTransaction.getRecipient(), genesisTransaction.getValue(), genesisTransaction.getTransactionId())); //manually add the Transactions Output
        coinConfig.getUTXOs().put(genesisTransaction.getOutputs().get(0).getId(), genesisTransaction.getOutputs().get(0)); //its important to store our first transaction in the UTXOs list.

        System.out.println("Creating and Mining Genesis block... ");
        Block genesis = new Block("0");
        blockService.addTransaction(genesis, genesisTransaction);
        blockService.addBlock(genesis);

        //testing
        Block block1 = new Block(genesis.getHash());
        System.out.println("\nWalletA's balance is: " + walletService.getBalance(walletA));
        System.out.println("\nWalletA is Attempting to send funds (40) to WalletB...");
        blockService.addTransaction(block1, walletService.sendFunds(walletA, walletB.getPublicKey(), 40f));
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
