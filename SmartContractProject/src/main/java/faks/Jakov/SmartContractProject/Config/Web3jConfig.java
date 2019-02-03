package faks.Jakov.SmartContractProject.Config;

import java.io.IOException;

import javax.annotation.PreDestroy;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.http.HttpService;

@Configuration
public class Web3jConfig {
	@Autowired
	Logger logger;
	
	@Autowired
	Web3j web3j;
	
	@Bean
	public Web3j getWeb3j() throws IOException {
		// Napravi Web3j interface prema Infura klijentu (testnet Rinkeby)
        Web3j web3j = Web3j.build(new HttpService(
                "https://rinkeby.infura.io/v3/2a9d3976d2424e7795b5fe359323cb6d"));
        logger.info("Connected to Ethereum client version: "
                + web3j.web3ClientVersion().send().getWeb3ClientVersion());
        
        return web3j;
	}
	
	@PreDestroy
	public void destroyWeb3j() {
		web3j.shutdown();
		logger.info("Web3j is shut down.");
	}
}
