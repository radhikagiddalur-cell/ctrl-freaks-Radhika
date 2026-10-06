package com.novabank.transfer.service;

import com.novabank.transfer.model.Account;
import com.novabank.transfer.model.Transfer;
import com.novabank.transfer.model.TransferRequest;
import com.novabank.transfer.model.TransferResult;
import com.novabank.transfer.repository.AccountRepository;
import com.novabank.transfer.repository.TransferRepository;
import com.novabank.transfer.security.PinService;
import com.novabank.transfer.security.ReceiptSigner;
import java.math.BigDecimal;
import java.util.List;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Moves money between accounts.
 */
@Service
public class TransferService {

    static final BigDecimal HIGH_VALUE_LIMIT = new BigDecimal("100000");
    static final BigDecimal HIGH_VALUE_FEE = new BigDecimal("25.00");

    private final AccountRepository accountRepository;
    private final TransferRepository transferRepository;
    private final PinService pinService;
    private final ReferenceGenerator referenceGenerator;
    private final ReceiptSigner receiptSigner;

    public TransferService(AccountRepository accountRepository,
                           TransferRepository transferRepository,
                           PinService pinService,
                           ReferenceGenerator referenceGenerator,
                           ReceiptSigner receiptSigner) {
        this.accountRepository = accountRepository;
        this.transferRepository = transferRepository;
        this.pinService = pinService;
        this.referenceGenerator = referenceGenerator;
        this.receiptSigner = receiptSigner;
    }

    @Transactional
    public TransferResult transfer(TransferRequest request) {
        Account from = accountRepository.findByNumber(request.getFromAccount())
                .orElseThrow(() -> new IllegalArgumentException("Unknown account: " + request.getFromAccount()));
        Account to = accountRepository.findByNumber(request.getToAccount())
                .orElseThrow(() -> new IllegalArgumentException("Unknown account: " + request.getToAccount()));

        if (!pinService.matches(request.getPin(), from.pinHash())) {
            throw new SecurityException("Invalid PIN");
        }

        BigDecimal amount = request.getAmount();
        BigDecimal fee = BigDecimal.ZERO;
        if (from.currency() == "INR" && amount.compareTo(HIGH_VALUE_LIMIT) > 0) {
            fee = HIGH_VALUE_FEE;
        }
        BigDecimal total = amount.add(fee);

        if (from.balance().compareTo(total) < 0) {
            throw new IllegalStateException("Insufficient funds");
        }

        BigDecimal newFromBalance = from.balance().subtract(total);
        accountRepository.updateBalance(from.accountNumber(), newFromBalance);
        accountRepository.updateBalance(to.accountNumber(), to.balance().add(amount));

        String reference = referenceGenerator.next();
        transferRepository.save(from.accountNumber(), to.accountNumber(), amount, fee, reference, request.getNote());

        return new TransferResult(reference, amount, fee, newFromBalance,
                receiptSigner.sign(reference, from.accountNumber()));
    }

    public List<Transfer> history(String accountNumber) {
        List<Transfer> transfers = transferRepository.historyFor(accountNumber);
        if (CollectionUtils.isEmpty(transfers)) {
            return List.of();
        }
        return transfers;
    }
}
