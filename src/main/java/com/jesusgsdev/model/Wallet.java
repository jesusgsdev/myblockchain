package com.jesusgsdev.model;

import lombok.Data;

import java.security.*;
import java.security.spec.ECGenParameterSpec;
import java.util.HashMap;

@Data
public class Wallet {

    private PrivateKey privateKey;
    private PublicKey publicKey;

    private HashMap<String,TransactionOutput> UTXOs = new HashMap<String,TransactionOutput>();

    private String uuid;


    public Wallet(String uuid) {
        this.uuid = uuid;
        generateKeyPair();
    }

    public void generateKeyPair() {
        try {
            KeyPairGenerator keyGen = KeyPairGenerator.getInstance("ECDSA","BC");
            SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
            ECGenParameterSpec ecSpec = new ECGenParameterSpec("prime192v1");
            // Initialize the key generator and generate a KeyPair
            keyGen.initialize(ecSpec, random); //256
            KeyPair keyPair = keyGen.generateKeyPair();
            // Set the public and private keys from the keyPair
            privateKey = keyPair.getPrivate();
            publicKey = keyPair.getPublic();

        }catch(Exception e) {
            throw new RuntimeException(e);
        }
    }

}


