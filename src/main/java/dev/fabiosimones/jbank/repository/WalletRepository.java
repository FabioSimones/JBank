package dev.fabiosimones.jbank.repository;

import dev.fabiosimones.jbank.entities.Wallet;
import dev.fabiosimones.jbank.repository.dto.StatementView;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.UUID;

public interface WalletRepository extends JpaRepository<Wallet, UUID> {

    String SQL_STATEMENT= """
            Select
                BIN_TO_UUID(transfer_id) as statemet_id,
                "transfer" as type,
                transfer_value as statement_value,
                BIN_TO_UUID(wallet_receiver_id) as wallet_receiver,
                BIN_TO_UUID(wallet_sender_id) as wallet_sender,
                transfer_date_time as statement_date_time
            from
                tb_transfers
            where wallet_receiver_id = UUID_TO_BIN(?1) OR wallet_sender_id = UUID_TO_BIN(?1)
            union all
            select
                BIN_TO_UUID(deposit_id) as statement_id,
                "deposit" as type,
                deposit_value as statement_value,
                BIN_TO_UUID(wallet_id)as wallet_receiver,
                "" as wallet_sender,
                deposit_date_time as statement_date_time
            from tb_deposit
            where
                wallet_id = UUID_TO_BIN(?1)
            """;

    String SQL_COUNT_STATEMENT = """
            Select count(*) from
            (
                """ + SQL_STATEMENT + """
            ) as total
            """;

    Optional<Wallet> findByCpfOrEmail(String cpf, String email);

    @Query(value = SQL_STATEMENT,
            countQuery = SQL_COUNT_STATEMENT, 
            nativeQuery = true)
    Page<StatementView> findStatements(String walletId, PageRequest pageRequest);
}
