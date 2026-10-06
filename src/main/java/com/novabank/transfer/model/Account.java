package com.novabank.transfer.model;

import java.math.BigDecimal;

/**
 * A customer bank account.
 */
public record Account(Long id,
                      String accountNumber,
                      String holderName,
                      BigDecimal balance,
                      String currency,
                      String pinHash) {
}
