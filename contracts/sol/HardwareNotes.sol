pragma solidity >= 0.5.0 < 0.6.0;

contract ERC20Interface {
    function transferFrom(address from, address to, uint256 value) public returns(bool) {}
	function transfer(address recipient, uint value) public returns (bool) {}
}

contract HardwareNotes {

	ERC20Interface public tokenContract;

	mapping (address => mapping(uint256 => address[])) public hardware; // manufacturer => batchId => array of note addresses in batch
	mapping (uint256 => uint256) ETHBalance; // map noteId to value

	struct note {
		address manufacturer;
		uint batchId;
		uint hardwareId;
		uint noteId;
		address token;
		uint amount;
		uint withdrawDelay;
		uint withdrawTimeout;
		uint withdrawStart;
		bool isInFlight;
	}

	note[] notes;

	event Deposit(
		address indexed _noteAddress,
		address manufacturer,
		uint batchId,
		uint hardwareId,
		uint noteId,
		address token,
		uint amount,
		uint withdrawDelay,
		uint withdrawTimeout
	);

	function registerBatch(uint256 batch, address[] memory pubKeys) public {

		uint i;
		for (i = 0; i < pubKeys.length; i++){
			hardware[msg.sender][batch].push(pubKeys[i]);
		}
	}

	function deposit(
		address manufacturer,
		uint batchId,
		uint hardwareId,
		address token,
		uint amount,
		uint withdrawDelay,
		uint withdrawTimeout)
	public payable {

		require(withdrawTimeout > withdrawDelay, "withdrawTimeout must be larger than withdrawDelay");

		uint noteId = notes.length;

		// ETH deposit
		if (token == address(0)){
			require(msg.value >= amount * 1 ether, "deposit insufficient");
			ETHBalance[noteId] = amount;
		// non-ETH deposit
		} else {
			tokenContract = ERC20Interface(token);
			require(
				tokenContract.transferFrom(msg.sender, address(this), amount),
				"token transfer not approved"
			);
		}

		note memory newNote = note(
			manufacturer, batchId, hardwareId, noteId,
			token, amount,
			withdrawDelay, withdrawTimeout, 0, false
		);
		notes.push(newNote);

		address noteAddress = hardware[manufacturer][batchId][hardwareId];

		emit Deposit(
			noteAddress,
			manufacturer, batchId, hardwareId, noteId, token, amount, withdrawDelay, withdrawTimeout
		);
	}

	function signalWithdraw(
		uint batchId,
		uint hardwareId,
		uint noteId,
		uint blockNum,
		bytes32 blockHash,
		uint8 v,
		bytes32 r,
		bytes32 s)
	public {
		checkSig(batchId, hardwareId, noteId, blockNum,blockHash, v, r, s);
		notes[noteId].isInFlight = true;
		notes[noteId].withdrawStart = block.timestamp;
	}

	function withdraw(
		uint batchId,
		uint hardwareId,
		uint noteId,
		uint blockNum,
		bytes32 blockHash,
		uint8 v,
		bytes32 r,
		bytes32 s,
		address payable recipient)
	public {

		note memory _note = notes[noteId];
		require(notes[noteId].isInFlight == true, "signalWithdraw has not been called");
		require(block.timestamp <= _note.withdrawStart + _note.withdrawTimeout, "withdraw has timed out since signalWithdraw");
		require(block.timestamp >= _note.withdrawStart + _note.withdrawDelay, "withdrawDelay has not ended");
		checkSig(batchId, hardwareId, noteId, blockNum,blockHash, v, r, s);

		// ETH deposit
		if (_note.token == address(0)){
			recipient.transfer(_note.amount * 1 ether);
			ETHBalance[noteId] = 0;
		// non-ETH deposit
		} else {
			tokenContract = ERC20Interface(_note.token);
			require(
				tokenContract.transfer(recipient, _note.amount),
				"token transfer not approved"
			);
		}

	}

	function checkSig(uint batchId, uint hardwareId, uint noteId, uint blockNum, bytes32 blockHash, uint8 v, bytes32 r, bytes32 s) public view {
		require(blockhash(blockNum) == blockHash, "invalid block (not in last 256 blocks)");

		note memory curNote = notes[noteId];
		address notePubKey = hardware[curNote.manufacturer][batchId][hardwareId];

		bytes32 messageDigest = keccak256(abi.encodePacked("\x19Ethereum Signed Message:\n32", blockHash));

		require(notePubKey == ecrecover(messageDigest, v, r, s), 'only trusted hardware can sign withdraw');


	}

	function getNote(uint _noteId) public view returns(
		address manufacturer,
		uint batchId,
		uint hardwareId,
		uint noteId,
		address token,
		uint amount,
		uint withdrawDelay,
		uint withdrawTimeout,
		uint withdrawStart,
		bool isInFlight
	) {
		note memory _note = notes[_noteId];
		return (_note.manufacturer, _note.batchId, _note.hardwareId, _note.noteId, _note.token,
		_note.amount, _note.withdrawDelay, _note.withdrawTimeout, _note.withdrawStart, _note.isInFlight);
	}

}