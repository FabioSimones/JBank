package dev.fabiosimones.jbank.controller.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record DepositMoneyDTO(@NotNull @DecimalMin("10") BigDecimal value) {
}
