# hardware_notes
Offline-first cryptocurrency transactions

Install all dependencies and build the source code:

```bash
npm i &&
npm run bootstrap &&
npm run build
```

In a separate terminal, navigate to `contracts` and launch Ganache:

```bash
cd contracts
npm run ganache
```

Staying inside the `contracts` directory, run the demo script. You need a 
[`solc`](https://github.com/ethereum/solidity) 0.5.X binary somewhere in your
filesystem.

```bash
node build/run.js -s /path/to/solc -o ./abi -i ./sol/
```
