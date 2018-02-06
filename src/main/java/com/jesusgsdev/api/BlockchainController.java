package com.jesusgsdev.api;

import com.jesusgsdev.config.CoinCore;
import com.jesusgsdev.model.Block;
import com.jesusgsdev.util.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/")
public class BlockchainController {

	@Autowired
	private TestService testService;

	@Autowired
	private CoinCore coinConfig;

	@GetMapping("/test")
	public void test() {
		testService.mainTest();
	}

	@GetMapping("/chain")
	public List<Block> getBlockChain() {
		return coinConfig.getBlockchain();
	}


}
