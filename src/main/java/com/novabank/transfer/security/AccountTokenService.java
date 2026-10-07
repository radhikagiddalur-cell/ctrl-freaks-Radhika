package com.novabank.transfer.security;

import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.util.Base64;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import org.springframework.stereotype.Service;

/**
 * Turns account numbers into opaque tokens that partner apps can store instead of the real number.
 */
@Service
public class AccountTokenService {

    private static final byte[] TOKEN_KEY = "NovaBankKey12345".getBytes(StandardCharsets.UTF_8);

    public String tokenize(String accountNumber) {
        try {
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(TOKEN_KEY, "AES"));
            byte[] encrypted = cipher.doFinal(accountNumber.getBytes(StandardCharsets.UTF_8));
            return Base64.getUrlEncoder().withoutPadding().encodeToString(encrypted);
        } catch (GeneralSecurityException e) {
            throw new IllegalStateException("Could not create token", e);
        }
    }
}
