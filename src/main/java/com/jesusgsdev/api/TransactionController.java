package com.jesusgsdev.api;

import com.jesusgsdev.model.Block;
import com.jesusgsdev.model.Transaction;
import com.jesusgsdev.model.Wallet;
import com.jesusgsdev.rest.TransactionRestInput;
import com.jesusgsdev.service.BlockService;
import com.jesusgsdev.service.BlockchainService;
import com.jesusgsdev.service.WalletService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/transactions")
public class TransactionController {

	private static final Logger LOGGER = LoggerFactory.getLogger(TransactionController.class);

	@Autowired
	private WalletService walletService;

	@Autowired
	private BlockService blockService;

	@Autowired
	private BlockchainService blockchainService;

	@PostMapping()
	public void submitTransaction(@RequestBody TransactionRestInput transactionRestInput) {
		Wallet senderWallet = walletService.getWalletById(transactionRestInput.getSenderWalletId());
		Wallet recipientWallet = walletService.getWalletById(transactionRestInput.getRecipientWalletId());
		Float amount = transactionRestInput.getAmount();

		Block block = blockService.prepareNewBlock();
		LOGGER.info("Origin Wallet's balance is: " + walletService.getBalance(senderWallet));
		LOGGER.info("Origin Wallet is Attempting to send funds ("+ amount +") to Destination Wallet...");

		Transaction walletASendingToWalletB =  walletService.sendFunds(senderWallet, recipientWallet.getPublicKey(), amount);
		blockService.addTransaction(block, walletASendingToWalletB);
		blockService.addBlock(block);

		LOGGER.info("Origin Wallet's balance will be: " + walletService.getBalance(senderWallet));
		LOGGER.info("Destination Wallet's balance will be: " + walletService.getBalance(recipientWallet));
	}

}