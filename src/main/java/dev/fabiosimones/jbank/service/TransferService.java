package dev.fabiosimones.jbank.service;


import dev.fabiosimones.jbank.controller.dto.TransferMoneyDTO;
import dev.fabiosimones.jbank.entities.Transfer;
import dev.fabiosimones.jbank.entities.Wallet;
import dev.fabiosimones.jbank.exception.TransferException;
import dev.fabiosimones.jbank.exception.WalletNotFoundException;
import dev.fabiosimones.jbank.repository.TransferRepository;
import dev.fabiosimones.jbank.repository.WalletRepository;
import jakarta.validation.Valid;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class TransferService {

    private final TransferRepository transferRepository;
    private final WalletRepository walletRepository;

    public TransferService(TransferRepository transferRepository, WalletRepository walletRepository) {
        this.transferRepository = transferRepository;
        this.walletRepository = walletRepository;
    }

    @Transactional
    public void transferMoney(@Valid TransferMoneyDTO dto) {
        var sender = walletRepository.findById(dto.sender()).
                orElseThrow(() -> new WalletNotFoundException("Sender does not exist"));

        var receiver = walletRepository.findById(dto.receiver()).
                orElseThrow(() -> new WalletNotFoundException("Receiver does not exist"));

        if(sender.getBalance().compareTo(dto.value()) == -1){
            throw new TransferException("Insufficient balance. You current balance is $" + sender.getBalance());
        }

        persistTransfer(dto, receiver, sender);
        updateWallets(dto, sender, receiver);
    }

    private void updateWallets(TransferMoneyDTO dto, Wallet sender, Wallet receiver) {
        sender.setBalance(sender.getBalance().subtract(dto.value()));
        receiver.setBalance(receiver.getBalance().add(dto.value()));

        walletRepository.save(sender);
        walletRepository.save(receiver);
    }

    private void persistTransfer(TransferMoneyDTO dto, Wallet receiver, Wallet sender) {
        var transfer = new Transfer();
        transfer.setReceiver(receiver);
        transfer.setSender(sender);
        transfer.setTransferValue(dto.value());
        transfer.setTransferDateTime(LocalDateTime.now());

        transferRepository.save(transfer);
    }
}
