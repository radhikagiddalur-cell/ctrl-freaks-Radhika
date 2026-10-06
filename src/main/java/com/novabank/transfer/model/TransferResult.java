package com.novabank.transfer.model;

import java.math.BigDecimal;

/**
 * What the customer sees after a successful transfer.
 */
public record TransferResult(String reference,
                             BigDecimal amount,
                             BigDecimal fee,
                             BigDecimal newBalance,
                             String receiptSignature) {
}
