pragma solidity >= 0.5.0 < 0.6.0;

import {ERC20} from "./ERC20/ERC20.sol";
import {ERC20Detailed} from "./ERC20/ERC20Detailed.sol";

contract Token is ERC20, ERC20Detailed{

	constructor(string memory name, string memory symbol, uint8 decimal) ERC20Detailed(name, symbol, decimal) public{
        _mint(msg.sender, 1000000000000000000000);
	}

}