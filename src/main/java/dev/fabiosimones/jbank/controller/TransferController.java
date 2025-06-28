package dev.fabiosimones.jbank.controller;

import dev.fabiosimones.jbank.controller.dto.TransferMoneyDTO;
import dev.fabiosimones.jbank.service.TransferService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/transfers")
public class TransferController {

    private final TransferService transferService;

    public TransferController(TransferService transferService) {
        this.transferService = transferService;
    }

    @PostMapping
    public ResponseEntity<Void> transfer(@RequestBody @Valid TransferMoneyDTO dto){
        transferService.transferMoney(dto);

        return ResponseEntity.ok().build();
    }
}
