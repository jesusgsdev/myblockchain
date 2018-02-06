package com.jesusgsdev.app;

import com.jesusgsdev.model.Block;
import com.jesusgsdev.model.Transaction;
import com.jesusgsdev.model.TransactionOutput;
import com.jesusgsdev.model.Wallet;
import com.jesusgsdev.util.CoinUtil;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.security.Security;

@SpringBootApplication(scanBasePackageClasses = { MyBlockchainApplication.class })
public class MyBlockchainApplication {

	public static void main(String[] args) {
		SpringApplication.run(MyBlockchainApplication.class, args);


		//add our blocks to the blockchain ArrayList:
		Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider()); //Setup Bouncey castle as a Security Provider

		//Create wallets:
		CoinUtil.walletA = new Wallet();
		CoinUtil.walletB = new Wallet();
		Wallet coinbase = new Wallet();

		//create genesis transaction, which sends 100 NoobCoin to walletA:
		CoinUtil.genesisTransaction = new Transaction(coinbase.getPublicKey(), CoinUtil.walletA.getPublicKey(), 100f, null);
		CoinUtil.genesisTransaction.generateSignature(coinbase.getPrivateKey());	 //manually sign the genesis transaction
		CoinUtil.genesisTransaction.setTransactionId("0"); //manually set the transaction id
		CoinUtil.genesisTransaction.getOutputs().add(new TransactionOutput(CoinUtil.genesisTransaction.getReciepient(), CoinUtil.genesisTransaction.getValue(), CoinUtil.genesisTransaction.getTransactionId())); //manually add the Transactions Output
		CoinUtil.UTXOs.put(CoinUtil.genesisTransaction.getOutputs().get(0).getId(), CoinUtil.genesisTransaction.getOutputs().get(0)); //its important to store our first transaction in the UTXOs list.

		System.out.println("Creating and Mining Genesis block... ");
		Block genesis = new Block("0");
		genesis.addTransaction(CoinUtil.genesisTransaction);
		CoinUtil.addBlock(genesis);

		//testing
		Block block1 = new Block(genesis.getHash());
		System.out.println("\nWalletA's balance is: " + CoinUtil.walletA.getBalance());
		System.out.println("\nWalletA is Attempting to send funds (40) to WalletB...");
		block1.addTransaction(CoinUtil.walletA.sendFunds(CoinUtil.walletB.getPublicKey(), 40f));
		CoinUtil.addBlock(block1);
		System.out.println("\nWalletA's balance is: " + CoinUtil.walletA.getBalance());
		System.out.println("WalletB's balance is: " + CoinUtil.walletB.getBalance());

		Block block2 = new Block(block1.getHash());
		System.out.println("\nWalletA Attempting to send more funds (1000) than it has...");
		block2.addTransaction(CoinUtil.walletA.sendFunds(CoinUtil.walletB.getPublicKey(), 1000f));
		CoinUtil.addBlock(block2);
		System.out.println("\nWalletA's balance is: " + CoinUtil.walletA.getBalance());
		System.out.println("WalletB's balance is: " + CoinUtil.walletB.getBalance());

		Block block3 = new Block(block2.getHash());
		System.out.println("\nWalletB is Attempting to send funds (20) to WalletA...");
		block3.addTransaction(CoinUtil.walletB.sendFunds( CoinUtil.walletA.getPublicKey(), 20));
		System.out.println("\nWalletA's balance is: " + CoinUtil.walletA.getBalance());
		System.out.println("WalletB's balance is: " + CoinUtil.walletB.getBalance());

		CoinUtil.isChainValid();
	}



}