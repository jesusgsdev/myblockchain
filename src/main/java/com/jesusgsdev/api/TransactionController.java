package com.jesusgsdev.api;

import com.jesusgsdev.config.CoinCore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/transactions")
public class TransactionController {

	@Autowired
	private CoinCore coinConfig;



}
