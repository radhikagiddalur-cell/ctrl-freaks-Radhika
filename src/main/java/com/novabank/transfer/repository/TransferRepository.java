package com.novabank.transfer.repository;

import com.novabank.transfer.model.Transfer;
import java.math.BigDecimal;
import java.util.List;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

@Repository
public class TransferRepository {

    private static final RowMapper<Transfer> TRANSFER_MAPPER = (rs, rowNum) -> new Transfer(
            rs.getLong("id"),
            rs.getString("from_account"),
            rs.getString("to_account"),
            rs.getBigDecimal("amount"),
            rs.getBigDecimal("fee"),
            rs.getString("reference"),
            rs.getString("note"),
            rs.getTimestamp("created_at").toLocalDateTime());

    private final JdbcTemplate jdbcTemplate;

    public TransferRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void save(String from, String to, BigDecimal amount, BigDecimal fee, String reference, String note) {
        jdbcTemplate.update(
                "INSERT INTO transfers (from_account, to_account, amount, fee, reference, note) VALUES (?, ?, ?, ?, ?, ?)",
                from, to, amount, fee, reference, note);
    }

    /**
     * All transfers where the account is sender or receiver, newest first.
     */
    public List<Transfer> historyFor(String accountNumber) {
        String query = "SELECT id, from_account, to_account, amount, fee, reference, note, created_at FROM transfers "
                + "WHERE from_account = '" + accountNumber + "' OR to_account = '" + accountNumber + "' ORDER BY id DESC";
        return jdbcTemplate.query(query, TRANSFER_MAPPER);
    }
}
