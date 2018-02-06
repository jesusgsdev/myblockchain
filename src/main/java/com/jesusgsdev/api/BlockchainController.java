package com.jesusgsdev.api;

import com.jesusgsdev.config.CoinCore;
import com.jesusgsdev.model.Block;
import com.jesusgsdev.model.Transaction;
import com.jesusgsdev.model.TransactionOutput;
import com.jesusgsdev.model.Wallet;
import com.jesusgsdev.rest.BlockRestOutput;
import com.jesusgsdev.service.BlockService;
import com.jesusgsdev.service.TransactionService;
import com.jesusgsdev.util.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/")
public class BlockchainController {

	@Autowired
	private TestService testService;

	@Autowired
	private CoinCore coinConfig;

	@Autowired
	private TransactionService transactionService;

	@Autowired
	private BlockService blockService;

	@GetMapping("/test")
	public void test() {
		testService.mainTest();
	}

	@GetMapping("/start")
	public String initialize(){
		if(coinConfig.getBlockchain().size() != 0){
			return "The blockchain has been initialized";
		}

		//Create wallets:
		String initialWalletId = coinConfig.createWallet();
		Wallet walletA = coinConfig.getWallets().get(initialWalletId);
		Wallet coinbase = coinConfig.getWallets().get(coinConfig.createWallet());

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
		coinConfig.addBlock(genesis);

		return initialWalletId;
	}

	@GetMapping("/chain")
	public List<BlockRestOutput> getBlockChain() {
		return coinConfig.getBlockchain().stream().map(BlockRestOutput::new).collect(Collectors.toList());
	}


}
