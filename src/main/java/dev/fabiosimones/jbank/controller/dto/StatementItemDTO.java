package dev.fabiosimones.jbank.controller.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record StatementItemDTO(String statementId,
                               String type,
                               String literal,
                               BigDecimal value,
                               LocalDateTime dateTime,
                               StatementOperation operation) {
}
