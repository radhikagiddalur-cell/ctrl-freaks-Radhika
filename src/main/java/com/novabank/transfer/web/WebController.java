package com.novabank.transfer.web;

import com.novabank.transfer.model.TransferRequest;
import com.novabank.transfer.model.TransferResult;
import com.novabank.transfer.repository.AccountRepository;
import com.novabank.transfer.service.TransferService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * HTML pages.
 */
@Controller
public class WebController {

    private final AccountRepository accountRepository;
    private final TransferService transferService;

    public WebController(AccountRepository accountRepository, TransferService transferService) {
        this.accountRepository = accountRepository;
        this.transferService = transferService;
    }

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("accounts", accountRepository.findAll());
        model.addAttribute("transfer", new TransferRequest());
        return "index";
    }

    @PostMapping("/transfer")
    public String transfer(@ModelAttribute("transfer") TransferRequest request, Model model) {
        try {
            TransferResult result = transferService.transfer(request);
            model.addAttribute("result", result);
            model.addAttribute("history", transferService.history(request.getFromAccount()));
            return "result";
        } catch (RuntimeException e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("accounts", accountRepository.findAll());
            return "index";
        }
    }
}
