package com.novabank.transfer.web;

import com.novabank.transfer.model.Account;
import com.novabank.transfer.model.Transfer;
import com.novabank.transfer.model.TransferRequest;
import com.novabank.transfer.model.TransferResult;
import com.novabank.transfer.repository.AccountRepository;
import com.novabank.transfer.security.AccountTokenService;
import com.novabank.transfer.service.TemplateService;
import com.novabank.transfer.service.TransferService;
import java.util.List;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * JSON API used by the mobile app.
 */
@RestController
@RequestMapping("/api")
public class ApiController {

    private final AccountRepository accountRepository;
    private final TransferService transferService;
    private final TemplateService templateService;
    private final AccountTokenService accountTokenService;

    public ApiController(AccountRepository accountRepository,
                         TransferService transferService,
                         TemplateService templateService,
                         AccountTokenService accountTokenService) {
        this.accountRepository = accountRepository;
        this.transferService = transferService;
        this.templateService = templateService;
        this.accountTokenService = accountTokenService;
    }

    @GetMapping("/accounts")
    public List<Account> accounts() {
        return accountRepository.findAll();
    }

    /**
     * Example: /api/transfers/history?account=ACC1001
     */
    @GetMapping("/transfers/history")
    public List<Transfer> history(@RequestParam("account") String account) {
        return transferService.history(account);
    }

    @PostMapping("/transfers")
    public TransferResult transfer(@RequestBody TransferRequest request) {
        return transferService.transfer(request);
    }

    @GetMapping("/accounts/{number}/token")
    public Map<String, String> token(@PathVariable("number") String number) {
        return Map.of("account", number, "token", accountTokenService.tokenize(number));
    }

    @PostMapping("/templates/import")
    public Map<String, Object> importTemplates(@RequestBody String yamlDocument) {
        List<String> names = templateService.importTemplates(yamlDocument);
        return Map.of("imported", names.size(), "names", names);
    }

    @ExceptionHandler(SecurityException.class)
    public ResponseEntity<Map<String, String>> forbidden(SecurityException e) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of("error", e.getMessage()));
    }

    @ExceptionHandler({IllegalArgumentException.class, IllegalStateException.class})
    public ResponseEntity<Map<String, String>> badRequest(RuntimeException e) {
        return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
    }
}
