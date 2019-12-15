pragma solidity ^0.5.0;

contract HardwareNotes {

	mapping (address => mapping(uint256 => address[])) public hardwareBatches;

	struct note {
	// TODO does noteId specify position in notes array, or hardwareBatches[manufacturer][batchId] array?
	// TODO timeout on withdrawStart
		address manufacturer;
		uint batchId;
		uint noteId;
		address token;
		uint amount;
		uint withdrawDelay;
		uint withdrawStart;
		bool isInFlight;
	}

	note[] notes;

	function registerBatch(uint256 batch, address[] memory pubKeys) public {

		uint i;
		for (i = 0; i < pubKeys.length; i++){
			hardwareBatches[msg.sender][batch].push(pubKeys[i]);
		}
	}

	function deposit(address manufacturer, uint batchId, uint noteId, address token, uint amount, uint withdrawDelay) public {
		// TODO actually transfer value from TokenContract to this contract
		note memory newNote = note(
			manufacturer, batchId, noteId,
			token, amount,
			withdrawDelay, 0, false
		);
		notes.push(newNote);
	}

	function signalWithdraw(uint batchId, uint noteId, string memory message, uint8 v, bytes32 r, bytes32 s) public {
		checkSig(batchId, noteId, message, v, r, s);
		notes[noteId].isInFlight = true;
		notes[noteId].withdrawStart = block.timestamp;
	}

	function withdraw(uint batchId, uint noteId, string memory message, uint8 v, bytes32 r, bytes32 s, address recipient) public {
		// TODO check that signalWithdraw has not timed out
		checkSig(batchId, noteId, message, v, r, s);
		require(notes[noteId].isInFlight == true);
		require(block.timestamp >= notes[noteId].withdrawStart + notes[noteId].withdrawDelay);
		// TODO actually transfer value from this contract to recipient address

	}

	function checkSig(uint batchId, uint noteId, string memory message, uint8 v, bytes32 r, bytes32 s) public view {
		// TODO require(message in last_256_blockhashes)
		// TODO we should check that the message is signing the expected function (either withdraw() or signalWithdraw())
		note memory curNote = notes[noteId];
		address notePubKey = hardwareBatches[curNote.manufacturer][batchId][noteId];

        // The message header; we will fill in the length next
        string memory header = "\x19Ethereum Signed Message:\n000000";

        uint256 lengthOffset;
        uint256 length;
        assembly {
            // The first word of a string is its length
            length := mload(message)
            // The beginning of the base-10 message length in the prefix
            lengthOffset := add(header, 57)
        }

        // Maximum length we support
        require(length <= 999999);

        // The length of the message's length in base-10
        uint256 lengthLength = 0;

        // The divisor to get the next left-most message length digit
        uint256 divisor = 100000;

        // Move one digit of the message length to the right at a time
        while (divisor != 0) {

            // The place value at the divisor
            uint256 digit = length / divisor;
            if (digit == 0) {
                // Skip leading zeros
                if (lengthLength == 0) {
                    divisor /= 10;
                    continue;
                }
            }

            // Found a non-zero digit or non-leading zero digit
            lengthLength++;

            // Remove this digit from the message length's current value
            length -= digit * divisor;

            // Shift our base-10 divisor over
            divisor /= 10;

            // Convert the digit to its ASCII representation (man ascii)
            digit += 0x30;
            // Move to the next character and write the digit
            lengthOffset++;

            assembly {
                mstore8(lengthOffset, digit)
            }
        }

        // The null string requires exactly 1 zero (unskip 1 leading 0)
        if (lengthLength == 0) {
            lengthLength = 1 + 0x19 + 1;
        } else {
            lengthLength += 1 + 0x19;
        }

        // Truncate the tailing zeros from the header
        assembly {
            mstore(header, lengthLength)
        }

        // Perform the elliptic curve recover operation
        bytes32 check = keccak256(abi.encodePacked(header, message));


		require(notePubKey == ecrecover(check, v, r, s), 'only trusted hardware can sign withdraw');

	}

}