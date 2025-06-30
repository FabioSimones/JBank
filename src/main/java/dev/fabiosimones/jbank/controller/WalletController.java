package dev.fabiosimones.jbank.controller;

import dev.fabiosimones.jbank.controller.dto.CreateWalletDTO;
import dev.fabiosimones.jbank.controller.dto.DepositMoneyDTO;
import dev.fabiosimones.jbank.controller.dto.StatementDTO;
import dev.fabiosimones.jbank.service.WalletService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.UUID;

@RestController
@RequestMapping(path = "/wallets")
public class WalletController {
    private final WalletService walletService;

    public WalletController(WalletService walletService) {
        this.walletService = walletService;
    }

    @PostMapping
    public ResponseEntity<Void> createWallet(@RequestBody @Valid CreateWalletDTO dto){

        var wallet = walletService.createWaller(dto);

        return ResponseEntity.created(URI.create("/wallets/" + wallet.getWalletId().toString())).build();
    }

    @DeleteMapping(path = "/{walletId}")
    public ResponseEntity<Void> deleteWallet(@PathVariable("walletId")UUID walletId){
        var deleted = walletService.deleteWallet(walletId);

        return deleted ?
                ResponseEntity.noContent().build() :
                ResponseEntity.notFound().build();
    }

    @PostMapping(path = "/{walletId}/deposits")
    public ResponseEntity<Void> depositMoney(@PathVariable("walletId")UUID walletId,
                                             @RequestBody @Valid DepositMoneyDTO dto,
                                             HttpServletRequest servletRequest){

        walletService.depositMoney(walletId,
                dto,
                servletRequest.getAttribute("x-user-ip").toString());

        return ResponseEntity.ok().build();
    }

    @GetMapping(path = "/{walletId}/statements")
    public ResponseEntity<StatementDTO> getStatements(@PathVariable("walletId") UUID walletId,
                                                     @RequestParam(name = "page", defaultValue = "0") Integer page,
                                                     @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize){

        var statement = walletService.getStatements(walletId, page, pageSize);

        return ResponseEntity.ok(statement);
    }
}
