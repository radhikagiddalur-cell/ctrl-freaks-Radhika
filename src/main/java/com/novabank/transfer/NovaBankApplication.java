package com.novabank.transfer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * NovaBank Transfer Portal (fictional bank).
 *
 * <p>Hackathon application: it intentionally contains security flaws. Do not deploy it outside a lab.</p>
 */
@SpringBootApplication
public class NovaBankApplication {

    public static void main(String[] args) {
        SpringApplication.run(NovaBankApplication.class, args);
    }
}
