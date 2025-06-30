package dev.fabiosimones.jbank.service;

import dev.fabiosimones.jbank.controller.dto.*;
import dev.fabiosimones.jbank.entities.Deposit;
import dev.fabiosimones.jbank.entities.Wallet;
import dev.fabiosimones.jbank.exception.DeleteWalletException;
import dev.fabiosimones.jbank.exception.StatementException;
import dev.fabiosimones.jbank.exception.WalletDataAlreadyExistsException;
import dev.fabiosimones.jbank.exception.WalletNotFoundException;
import dev.fabiosimones.jbank.repository.DepositRepository;
import dev.fabiosimones.jbank.repository.WalletRepository;
import dev.fabiosimones.jbank.repository.dto.StatementView;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
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

    public StatementDTO getStatements(UUID walletId, Integer page, Integer pageSize) {
        var wallet = walletRepository.findById(walletId)
                .orElseThrow(() -> new WalletNotFoundException("There is no wallet with this id."));

        var pageRequest = PageRequest.of(page, pageSize, Sort.Direction.DESC, "statement_date_time");

        var statements = walletRepository.findStatements(walletId.toString(), pageRequest)
                .map(view -> mapToDTO(walletId, view));


        return new StatementDTO(
                new WalletDTO(wallet.getWalletId(), wallet.getCpf(), wallet.getName(), wallet.getEmail(), wallet.getBalance()),
                statements.getContent(),
                new PaginationDTO(statements.getNumber(), statements.getSize(), statements.getTotalElements(), statements.getTotalPages())
        );
    }

    private StatementItemDTO mapToDTO(UUID walletId, StatementView view) {
        if(view.getType().equalsIgnoreCase("deposit")){
            return mapToDeposit(view);
        }

        if(view.getType().equalsIgnoreCase("transfer")
            && view.getWalletSender().equalsIgnoreCase(walletId.toString())){
            return mapWhenTransferSent(walletId, view);
        }

        if(view.getType().equalsIgnoreCase("transfer")
                && view.getWalletReceiver().equalsIgnoreCase(walletId.toString())){
            return mapWhenTransferReceived(walletId, view);
        }

        throw new StatementException("Invalid type" + view.getType());
    }

    private StatementItemDTO mapWhenTransferReceived(UUID walletId, StatementView view) {
        return new StatementItemDTO(
                view.getStatementId(),
                view.getType(),
                "Money received to " + view.getWalletReceiver(),
                view.getStatementValue(),
                view.getStatementDateTime(),
                StatementOperation.CREDIT
        );
    }

    private StatementItemDTO mapWhenTransferSent(UUID walletId, StatementView view) {
        return new StatementItemDTO(
                view.getStatementId(),
                view.getType(),
                "Money sent to " + view.getWalletReceiver(),
                view.getStatementValue(),
                view.getStatementDateTime(),
                StatementOperation.DEBIT
        );
    }

    private StatementItemDTO mapToDeposit(StatementView view) {
        return new StatementItemDTO(
                view.getStatementId(),
                view.getType(),
                "money deposit",
                view.getStatementValue(),
                view.getStatementDateTime(),
                StatementOperation.CREDIT
        );
    }
}
