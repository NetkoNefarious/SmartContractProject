package faks.Jakov.SmartContractProject.Controller;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.WalletUtils;
import org.web3j.protocol.Web3j;
import org.web3j.tx.gas.ContractGasProvider;
import org.web3j.tx.gas.DefaultGasProvider;

import faks.Jakov.SmartContractProject.Service.StorageService;
import faks.Jakov.SmartContractProject.Contracts.*;

@RequestMapping("/contract")
@Controller
public class ContractController {
    private final Web3j web3j;
    private final Logger logger;
    private final StorageService storageService;

    @Autowired
    public ContractController(Web3j web3j, Logger logger,
    		StorageService storageService) {
        this.web3j = web3j;
        this.logger = logger;
        this.storageService = storageService;
    }
    
	@GetMapping("")
	public String getContractForm() {
		logger.info("Loading the contract form");
		return "contract";
	}
	
	@PostMapping("")
	public String deployContract(@RequestParam("file") MultipartFile keystore,
    		@RequestParam("pass") String password, 
    		@RequestParam("msg") String message,
    		Model model) {
		
    	// Check if the filename ends with .json
    	if (!keystore.getOriginalFilename().endsWith(".json")) {
			model.addAttribute("error", "The uploaded file is not a JSON wallet file.");
			return "contract";
    	}

    	try {
    		// We need to store uploaded file
    		logger.info("Storing the keystore");
    		storageService.store(keystore);
    		
    		// Then load our Ethereum wallet file
    		logger.info("Loading credentials");
            Credentials credentials =
                    WalletUtils.loadCredentials(password,
                    		storageService.loadAsResource(
                    				keystore.getOriginalFilename()).getFile());
            
            // Now lets deploy a smart contract
            logger.info("Deploying smart contract");
            ContractGasProvider contractGasProvider = new DefaultGasProvider();
            Greeter contract = Greeter.deploy(
                    web3j,
                    credentials,
                    contractGasProvider,
                    message
                    ).send();

            // Let's get the contract address and contract message
            String contractAddress = contract.getContractAddress();
            model.addAttribute("link", "https://rinkeby.etherscan.io/address/"
            		+ contract.getContractAddress());
            logger.info("Smart contract deployed to address " + contractAddress);

            model.addAttribute("msg", contract.greet().send());
            
            // Delete the keystore and return the view
            logger.info("Deleting the keystore");
            storageService.deleteAll();
            
            logger.info("Contract deployed");
        	return "contractResult";
    	}
    	catch (Exception e) {
    		logger.error(e.getMessage());
    		model.addAttribute("error", e.getMessage());
    		return "contract";
    	}
	}
}
