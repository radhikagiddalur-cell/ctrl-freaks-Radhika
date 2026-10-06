package com.novabank.transfer.repository;

import com.novabank.transfer.model.Account;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

@Repository
public class AccountRepository {

    private static final RowMapper<Account> ACCOUNT_MAPPER = (rs, rowNum) -> new Account(
            rs.getLong("id"),
            rs.getString("account_number"),
            rs.getString("holder_name"),
            rs.getBigDecimal("balance"),
            rs.getString("currency"),
            rs.getString("pin_hash"));

    private static final String COLUMNS = "id, account_number, holder_name, balance, currency, pin_hash";

    private final JdbcTemplate jdbcTemplate;

    public AccountRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Account> findAll() {
        return jdbcTemplate.query("SELECT " + COLUMNS + " FROM accounts ORDER BY account_number", ACCOUNT_MAPPER);
    }

    public Optional<Account> findByNumber(String accountNumber) {
        return jdbcTemplate.query("SELECT " + COLUMNS + " FROM accounts WHERE account_number = ?",
                        ACCOUNT_MAPPER, accountNumber)
                .stream()
                .findFirst();
    }

    public void updateBalance(String accountNumber, BigDecimal newBalance) {
        jdbcTemplate.update("UPDATE accounts SET balance = ? WHERE account_number = ?", newBalance, accountNumber);
    }
}
