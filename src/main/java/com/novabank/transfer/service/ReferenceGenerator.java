package com.novabank.transfer.service;

import java.util.Random;
import org.springframework.stereotype.Component;

/**
 * Creates transfer reference numbers such as NB042917.
 */
@Component
public class ReferenceGenerator {

    private final Random random = new Random();

    public String next() {
        return String.format("NB%06d", random.nextInt(1_000_000));
    }
}
