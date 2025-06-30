package dev.fabiosimones.jbank.controller.dto;

import java.util.List;

public record StatementDTO(WalletDTO walletDTO,
                           List<StatementItemDTO> statements,
                           PaginationDTO pagination) {
}
