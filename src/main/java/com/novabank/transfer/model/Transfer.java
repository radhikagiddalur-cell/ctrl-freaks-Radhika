package com.novabank.transfer.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * A completed money transfer.
 */
public record Transfer(Long id,
                       String fromAccount,
                       String toAccount,
                       BigDecimal amount,
                       BigDecimal fee,
                       String reference,
                       String note,
                       LocalDateTime createdAt) {
}
