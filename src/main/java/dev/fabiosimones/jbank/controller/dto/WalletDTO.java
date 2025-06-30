package dev.fabiosimones.jbank.controller.dto;

import java.math.BigDecimal;
import java.util.UUID;

public record WalletDTO (UUID walletID,
                         String cpf,
                         String name,
                         String email,
                         BigDecimal balance){
}
