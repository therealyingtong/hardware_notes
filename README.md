# hardware_notes
Offline-first cryptocurrency transactions. (reference: https://ethresear.ch/t/hardware-notes-a-physical-asset-backed-by-cryptocurrancy/6471) Allows parties with outdated states to transfer assets offline, with assurance that they can be withdrawn online within an agreed time window.

- To **download the app and install it on your Android phone**, please download the .apk file at https://github.com/therealyingtong/hardware_notes/blob/master/hardware_notes.apk.
  - By default, most phones do not allow app installations from unknown sources. Please change this in your phone settings / permissions (https://android.gadgethacks.com/how-to/android-101-sideload-apps-by-enabling-unknown-sources-install-unknown-apps-0161947/)
- Enable NFC on your phone and hold the hardware note close to your phone. When the phone connects with the NFC tag, it should prompt you to open the app.
- Once you're in the app, scan the hardware note again and press the `SCAN NOTE` button. After a slight delay (while we query the blockchain), you will see a page specifying details about the note.

![](https://i.imgur.com/KfB8X8j.png)

## User stories
### Card manufacturer
- **registers** a batch of hardware notes by specifying their public keys in the smart contract
- **deposits** value into hardware notes on the smart contract. Note that deposits can be made by third parties, in ETH or any ERC20 token.

### Buyer / seller
- hardware notes can be used as currency, e.g. exchange for goods and services.
- before accepting a hardware note, the recipient should **scan** it to verify that it has not been withdrawn. If the recipient is using an outdated state, they should verify at least that the `withdrawDelay` period has not passed.
- the user should periodically **sync** their blockchain state, since the frequency of syncs decides which notes they can accept. As an illustration: if they never sync their state, they should accept notes with infinite `withdrawDelay`; and if they are constantly syncing their state, they can accept notes with very small `withdrawDelay`.
- a card holder can **withdraw** the note's value to their own account by signing a transaction with the card. Given a valid signature, the smart contract will release assets to whichever recipient account is specified.

## TODOs (as of Feb 2020)
Please get in touch if you have further ideas or want to contribute.

### User app
- we do have working offline state storage that persists between syncs; however it uses SharedPreferences. there's probably a more scalable way to do it (e.g. SQLite)
- we should implement the `withdraw` function

### Manufacturer app
- we currently don't have a manufacturer app
- we need `register` and `deposit` functions

## Notes
- See the smart contract (with published source code) on Kovan testnet at: https://kovan.etherscan.io/address/0xa2ff8dAEf58467b2Ac3c93c955449EE1342F6F9E.
- As of Feb 2020 we are using Status Keycards with the Status Cash applet (https://status.im/keycard_api/sdk/cash.html). Please get in touch if you're interested in trying out some of these cards.
