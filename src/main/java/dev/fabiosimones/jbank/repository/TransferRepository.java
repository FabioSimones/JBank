package dev.fabiosimones.jbank.repository;

import dev.fabiosimones.jbank.entities.Transfer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface TransferRepository extends JpaRepository<Transfer, UUID> {
}
