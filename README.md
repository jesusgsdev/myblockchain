# My Blockchain

The current implementation of the Blockchain in Java allows:

- Initialize the current blockchain giving back a wallet with coins
- Create wallets
- Get wallet's balance
- Send money between wallets
- Mine a block after submitting a transaction
- Validate the current blockchain

## Helper

I added a postman collection to make easier to perform the different operations on the
endpoints created.

### Third party libraries used

- SpringBoot 2.0.0 RELEASE
- Google Gson 2.8.2
- Google Guava 23.0
- Bouncy Castle 1.46
- Lombok

Also, for building the project in IntelliJ IDEA you would need the Lombok plugin `https://plugins.jetbrains.com/plugin/6317-lombok-plugin`

## How to use it

1. On the Postman Collection, use the `Initialize` call. This will give you back
the UUID of the main walled and will be saved on the environment variable called 
{{SENDER_WALLET_ID}}.
2. Use the action `Create Wallet` to create a new wallet to start transferring coins
from one to another. The UUID will be saved on the environment variable called 
{{RECIPIENT_WALLET_ID}}.
3. Go to `Submit Transaction` and you will see the body already built using the previous
two variables plus an amount of 30 coins. Send the POST to submit a transaction.
4. Go to `Mine Block` so you will mine the new block with the previous transaction in it
and then the block will be added to the blockchain.
5. Go to `Get blockchain` to see the current blockchain with the genesis block with a transaction
and our new block with our transaction.

- You can always verify that the blockchain is valid using `Validate Blockchain`.
- You can always get the balance of any wallet using `Get balance` and replacing
the UUID in the URL by the one you want to check the balance.
- You can adjust the difficulty using `Set Difficulty` replacing the current 4
given as example by any integer bigger or equal to 0.