package com.jesusgsdev;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.security.Security;

@SpringBootApplication
public class MyBlockchainApplication {

	public static void main(String[] args) {
		SpringApplication.run(MyBlockchainApplication.class, args);

		//Setup Bouncey castle as a Security Provider
		Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
	}

}