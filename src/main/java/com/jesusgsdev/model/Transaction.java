package com.jesusgsdev.model;

import lombok.Data;

import java.security.PublicKey;
import java.util.ArrayList;

@Data
public class Transaction {
	
	private String transactionId; //Contains a hash of transaction*
	private PublicKey sender; //Senders address/public key.
	private PublicKey reciepient; //Recipients address/public key.
	private float value; //Contains the amount we wish to send to the recipient.
	private byte[] signature; //This is to prevent anybody else from spending funds in our wallet.

	private ArrayList<TransactionInput> inputs = new ArrayList<>();
	private ArrayList<TransactionOutput> outputs = new ArrayList<>();
	
	private int sequence = 0; //A rough count of how many transactions have been generated
	
	// Constructor: 
	public Transaction(PublicKey from, PublicKey to, float value,  ArrayList<TransactionInput> inputs) {
		this.sender = from;
		this.reciepient = to;
		this.value = value;
		this.inputs = inputs;
	}

}
