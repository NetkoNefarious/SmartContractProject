package faks.Jakov.SmartContractProject.Controller;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.response.EthGetBalance;
import org.web3j.utils.Convert;

import faks.Jakov.SmartContractProject.Model.EthAddress;
import faks.Jakov.SmartContractProject.Model.Greeting;

@Controller
public class BalanceController {
	@Autowired
	private Web3j web3j;
	
	@Autowired
	private Logger logger;
	
	@GetMapping("/balance")
	public String getBalanceForm(Model model) {
		model.addAttribute("ethAddress", new EthAddress());
		return "balance";
	}
	
	@PostMapping("/balance")
	public String returnBalance(@ModelAttribute EthAddress address,
			Model model) {
		
		// Provjera da li adresa sadrži prefix 0x, inače se dodaje
		if (!address.getAddress().startsWith("0x")) {
			address.setAddress("0x" + address.getAddress());
		}
		try {
			// Dobivanje interface za pristupanje stanju računa
			logger.info("Fetching EthGetBalance class");
			EthGetBalance ethGetBalance = web3j
					.ethGetBalance(address.getAddress(), DefaultBlockParameterName.LATEST)
					.sendAsync().get();
			
			// Dobivanje stanja u valuti wei i pretvorba u ether
			logger.info("Fetching the wallet balance");
			String wei = ethGetBalance.getBalance().toString();
			address.setEther(Convert.fromWei(wei, Convert.Unit.ETHER).toPlainString());
			
			// Ubacivanje modela natrag u view
			logger.info("Done");
			model.addAttribute("ethAddress", address);			
			return "balanceResult";
		}

		catch (Exception e) {
			logger.info(e.getMessage());
			model.addAttribute("error", e.getMessage());
			return "balance";
		}
	}
	
	@GetMapping("/greeting")
    public String greetingForm(Model model) {
        model.addAttribute("greeting", new Greeting());
        return "greeting";
    }

    @PostMapping("/greeting")
    public String greetingSubmit(@ModelAttribute Greeting greeting) {
        return "greeting";
    }
}