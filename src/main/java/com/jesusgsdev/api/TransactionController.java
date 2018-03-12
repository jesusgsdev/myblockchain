package com.jesusgsdev.api;

import com.jesusgsdev.model.Block;
import com.jesusgsdev.model.Transaction;
import com.jesusgsdev.model.Wallet;
import com.jesusgsdev.rest.TransactionRestInput;
import com.jesusgsdev.service.BlockService;
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

    @PostMapping()
    public void submitTransaction(@RequestBody TransactionRestInput transactionRestInput) {
        Wallet senderWallet = walletService.getWalletById(transactionRestInput.getSenderWalletId());
        Wallet recipientWallet = walletService.getWalletById(transactionRestInput.getRecipientWalletId());
        Float amount = transactionRestInput.getAmount();

        Float senderWalletBalance = walletService.getBalance(senderWallet);
        Float recipientWalletBalance = walletService.getBalance(recipientWallet);
        LOGGER.info("Origin Wallet's balance is: " + senderWalletBalance);
        LOGGER.info("Recipient Wallet's balance is: " + recipientWalletBalance);
        LOGGER.info("Origin Wallet is Attempting to send funds ("+ amount +") to Destination Wallet...");

        Block block = blockService.prepareNewBlock();
        Transaction transaction = walletService.sendFunds(senderWallet, recipientWallet.getPublicKey(), amount);
        blockService.addTransaction(block, transaction);
        blockService.addBlock(block);

        LOGGER.info("Origin Wallet's balance will be: " + (senderWalletBalance - amount));
        LOGGER.info("Destination Wallet's balance will be: " + (recipientWalletBalance + amount));
    }

}