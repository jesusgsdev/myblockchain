package com.jesusgsdev.rest;

import lombok.*;

@Data
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class TransactionRestInput {

    private String senderWalletId;
    private String recipientWalletId;
    private Float amount;

}
