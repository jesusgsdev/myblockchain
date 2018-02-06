package com.jesusgsdev.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.UUID;

@RestController
@RequestMapping("/")
public class BlockchainController {

	@Autowired
	private ObjectMapper mapper;

	public static final String NODE_ID = UUID.randomUUID().toString().replace("-", "");
	public static final String NODE_ACCOUNT_ADDRESS = "0";
	public static final BigDecimal MINING_CASH_AWARD = BigDecimal.ONE;

}
