package com.jesusgsdev.model;

import com.jesusgsdev.util.StringUtil;
import lombok.Data;
import lombok.ToString;

import java.util.ArrayList;
import java.util.Date;

@Data
public class Block {

	private String hash;
	private String previousHash;
	private String merkleRoot;
	private ArrayList<Transaction> transactions = new ArrayList<Transaction>(); //our data will be a simple message.
	private long timeStamp; //as number of milliseconds since 1/1/1970.
	private int nonce;

	//Block Constructor.
	public Block(String previousHash) {
		this.previousHash = previousHash;
		this.timeStamp = new Date().getTime();

		this.hash = calculateHash(); //Making sure we do this after we set the other values.
	}

	//Calculate new hash based on blocks contents
	public String calculateHash() {
		String calculatedhash = StringUtil.applySha256(
				previousHash +
						Long.toString(timeStamp) +
						Integer.toString(nonce) +
						merkleRoot
		);
		return calculatedhash;
	}

}
