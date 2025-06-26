package dev.fabiosimones.jbank.service;

import dev.fabiosimones.jbank.controller.dto.CreateWalletDTO;
import dev.fabiosimones.jbank.controller.dto.DepositMoneyDTO;
import dev.fabiosimones.jbank.entities.Deposit;
import dev.fabiosimones.jbank.entities.Wallet;
import dev.fabiosimones.jbank.exception.DeleteWalletException;
import dev.fabiosimones.jbank.exception.WalletDataAlreadyExistsException;
import dev.fabiosimones.jbank.exception.WalletNotFoundException;
import dev.fabiosimones.jbank.repository.DepositRepository;
import dev.fabiosimones.jbank.repository.WalletRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class WalletService {
    private final WalletRepository walletRepository;
    private final DepositRepository depositRepository;

    public WalletService(WalletRepository walletRepository, DepositRepository depositRepository) {
        this.walletRepository = walletRepository;
        this.depositRepository = depositRepository;
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

    public boolean deleteWallet(UUID walletId) {
        var wallet = walletRepository.findById(walletId);

        if(wallet.isPresent()){

            if(wallet.get().getBalance().compareTo(BigDecimal.ZERO) != 0){
                throw new DeleteWalletException("The balance is not ZERO. The current amount is $" + wallet.get().getBalance());
            }
            walletRepository.deleteById(walletId);
        }

        return wallet.isPresent();
    }

    @Transactional
    public void depositMoney(UUID walletId, DepositMoneyDTO dto, String ipAddress) {

        var wallet = walletRepository.findById(walletId)
                .orElseThrow(() -> new WalletNotFoundException("There is no wallet with this id."));

        var deposit = new Deposit();
        deposit.setWallet(wallet);
        deposit.setDepositValue(dto.value());
        deposit.setDepositDateTime(LocalDateTime.now());
        deposit.setIpAddress(ipAddress);

        depositRepository.save(deposit);

        wallet.setBalance(wallet.getBalance().add(dto.value()));

        walletRepository.save(wallet);

    }
}
