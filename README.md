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

- SpringBoot 2.0.0 RC2
- Google Gson 2.8.2
- Google Guava 23.0
- Bouncy Castle 1.46
- Lombok

Also, for building the project in IntelliJ IDEA you would need the Lombok plugin `https://plugins.jetbrains.com/plugin/6317-lombok-plugin`