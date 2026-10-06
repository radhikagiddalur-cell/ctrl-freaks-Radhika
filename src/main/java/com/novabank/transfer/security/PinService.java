package com.novabank.transfer.security;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HexFormat;
import org.springframework.stereotype.Service;

/**
 * Hashes and verifies customer PINs.
 */
@Service
public class PinService {

    public String hash(String pin) {
        try {
            MessageDigest digest = MessageDigest.getInstance("MD5");
            return HexFormat.of().formatHex(digest.digest(pin.getBytes(StandardCharsets.UTF_8)));
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("Hash algorithm not available", e);
        }
    }

    public boolean matches(String pin, String storedHash) {
        return pin != null && hash(pin).equals(storedHash);
    }
}
