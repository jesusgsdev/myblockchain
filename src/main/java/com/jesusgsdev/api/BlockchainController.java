package com.jesusgsdev.api;

import com.jesusgsdev.config.CoinCore;
import com.jesusgsdev.rest.BlockRestOutput;
import com.jesusgsdev.service.BlockService;
import com.jesusgsdev.service.BlockchainService;
import com.jesusgsdev.util.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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

    @Autowired
    private CoinCore coinCore;

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

    @PutMapping("/difficulty/{level}")
    public String updateDificulty(@PathVariable Integer level) {
        coinCore.setDifficulty(level);
        return "Difficulty updated to " + level;
    }

}
