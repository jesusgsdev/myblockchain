package com.jesusgsdev.api;

import com.jesusgsdev.rest.BlockRestOutput;
import com.jesusgsdev.service.BlockService;
import com.jesusgsdev.service.BlockchainService;
import com.jesusgsdev.util.TestService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/")
public class BlockchainController {

    @Autowired
    private TestService testService;

    @Autowired
    private BlockService blockService;

    @Autowired
    private BlockchainService blockchainService;

    @GetMapping("/test")
    public void test() {
        testService.mainTest();
    }

    @PostMapping("/start")
    public String initialize(){
        return blockchainService.initialize();
    }

    @GetMapping("/chain")
    public List<BlockRestOutput> getBlockChain() {
        return blockchainService.getBlockChain();
    }

    @GetMapping("/chain/validate")
    public String isBlockchainValid() {
        return blockchainService.isChainValid() ? "The current blockchain is valid" : "The blockchain is not longer valid";
    }

    @PostMapping("/mine")
    public String mineBlock() {
        return blockService.mineBlock();
    }

}
