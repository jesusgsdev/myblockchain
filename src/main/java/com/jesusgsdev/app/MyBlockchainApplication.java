package com.jesusgsdev.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.security.Security;

@SpringBootApplication(scanBasePackageClasses = { MyBlockchainApplication.class })
public class MyBlockchainApplication {

	public static void main(String[] args) {
		SpringApplication.run(MyBlockchainApplication.class, args);

		//add our blocks to the blockchain ArrayList:
		Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider()); //Setup Bouncey castle as a Security Provider
	}

}