package com.jesusgsdev.api;

import com.jesusgsdev.config.CoinCore;
import com.jesusgsdev.model.Wallet;
import com.jesusgsdev.service.WalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/wallet")
public class WalletController {

	@Autowired
	private CoinCore coinConfig;

	@Autowired
	private WalletService walletService;

	@GetMapping
	public String createWallet() {
		return coinConfig.createWallet();
	}

	@GetMapping("/{id}")
	public Float getBalance(@PathVariable String id) {
		Wallet wallet = coinConfig.getWallets().get(id);
		return walletService.getBalance(wallet);
	}


}
