package faks.Jakov.SmartContractProject.Controller;

import java.math.BigDecimal;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.WalletUtils;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.tx.Transfer;
import org.web3j.utils.Convert;

import faks.Jakov.SmartContractProject.Model.TransferRequest;
import faks.Jakov.SmartContractProject.Service.StorageService;

@RequestMapping("/transfer")
@Controller
public class TransferController {
    private final Web3j web3j;
    private final Logger logger;
    private final StorageService storageService;

    @Autowired
    public TransferController(Web3j web3j, Logger logger,
    		StorageService storageService) {
        this.web3j = web3j;
        this.logger = logger;
        this.storageService = storageService;
    }
	
	@GetMapping("")
	public String getTransferForm(Model model) {
		logger.info("Loading the transfer form");
		model.addAttribute("transferRequest", new TransferRequest());
		return "transfer";
	}
	
    @PostMapping("")
    public String execTransfer(@RequestParam("file") MultipartFile keystore,
    		@RequestParam("pass") String password,
    		@ModelAttribute TransferRequest transferRequest,
    		Model model) {
    	
    	// Check if the filename ends with .json
    	if (!keystore.getOriginalFilename().endsWith(".json")) {
			model.addAttribute("error", "The uploaded file is not a JSON wallet file.");
			return "transfer";
    	}
    	
		// Check if the address starts with 0x, otherwise it's added
		if (!transferRequest.getDestAddress().startsWith("0x")) {
			transferRequest.setDestAddress("0x" + transferRequest.getDestAddress());
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

            // Calculate the amount needed for transfer
            logger.info("Calculating the amount needed for transfer");
            BigDecimal etherAmount = new BigDecimal(0)
            		.add(Convert.toWei(transferRequest.getEther(), Convert.Unit.ETHER))
            		.add(Convert.toWei(transferRequest.getSzabo(), Convert.Unit.SZABO))
            		.add(Convert.toWei(transferRequest.getMwei(), Convert.Unit.MWEI))
            		.add(Convert.toWei(transferRequest.getWei(), Convert.Unit.WEI));

            // We then have to send the funds (in Wei)
            logger.info("Transfering the funds");
            TransactionReceipt transferReceipt = Transfer.sendFunds(
                    web3j, credentials, transferRequest.getDestAddress(),
                    etherAmount, Convert.Unit.WEI)
                    .send();
            
            // Put the Etherscan report into the model
            model.addAttribute("link", "https://rinkeby.etherscan.io/tx/" + 
            	transferReceipt.getTransactionHash());
            
            // Delete the keystore and return the view
            logger.info("Deleting the keystore");
            storageService.deleteAll();
            
            logger.info("Transaction complete");
        	return "transferResult";
    	}
    	catch (Exception e) {
    		logger.error(e.getMessage());
    		model.addAttribute("error", e.getMessage());
    		return "transfer";
    	}
    }
}
