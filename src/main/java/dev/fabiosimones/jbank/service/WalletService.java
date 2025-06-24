package dev.fabiosimones.jbank.service;

import dev.fabiosimones.jbank.controller.dto.CreateWalletDTO;
import dev.fabiosimones.jbank.entities.Wallet;
import dev.fabiosimones.jbank.exception.WalletDataAlreadyExistsException;
import dev.fabiosimones.jbank.repository.WalletRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class WalletService {
    private final WalletRepository walletRepository;

    public WalletService(WalletRepository walletRepository) {
        this.walletRepository = walletRepository;
    }

    public Wallet createWaller(CreateWalletDTO dto) {
        var walletDb = walletRepository.findByCpfOrEmail(dto.cpf(), dto.email());

        if(walletDb.isPresent()){
            throw new WalletDataAlreadyExistsException("CPF or Email already exists.");
        }

        var wallet = new Wallet();
        wallet.setBalance(BigDecimal.ZERO);
        wallet.setName(dto.name());
        wallet.setCpf(dto.cpf());
        wallet.setEmail(dto.email());

        return walletRepository.save(wallet);
    }
}
