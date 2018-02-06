package com.jesusgsdev.api;

import com.jesusgsdev.config.CoinCore;
import com.jesusgsdev.model.Block;
import com.jesusgsdev.model.Transaction;
import com.jesusgsdev.model.TransactionOutput;
import com.jesusgsdev.model.Wallet;
import com.jesusgsdev.rest.TransactionRestInput;
import com.jesusgsdev.service.BlockService;
import com.jesusgsdev.service.TransactionService;
import com.jesusgsdev.service.WalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/transactions")
public class TransactionController {

	@Autowired
	private CoinCore coinConfig;

	@Autowired
	private WalletService walletService;

	@Autowired
	private BlockService blockService;

	@PostMapping()
	public void submitTransaction(@RequestBody TransactionRestInput transactionRestInput) {
		Wallet senderWallet = coinConfig.getWallets().get(transactionRestInput.getSenderWalletId());
		Wallet recipientWallet = coinConfig.getWallets().get(transactionRestInput.getRecipientWalletId());
		Float amount = transactionRestInput.getAmount();

		Block block = new Block(coinConfig.getBlockchain().get(coinConfig.getBlockchain().size() - 1).getHash());
		System.out.println("\nWalletA's balance is: " + walletService.getBalance(senderWallet));
		System.out.println("\nWalletA is Attempting to send funds ("+ amount +") to WalletB...");
		Transaction walletASendingToWalletB =  walletService.sendFunds(senderWallet, recipientWallet.getPublicKey(), amount);
		blockService.addTransaction(block, walletASendingToWalletB);
		coinConfig.addBlock(block);
		System.out.println("\nWalletA's balance is: " + walletService.getBalance(senderWallet));
		System.out.println("WalletB's balance is: " + walletService.getBalance(recipientWallet));
	}

}
