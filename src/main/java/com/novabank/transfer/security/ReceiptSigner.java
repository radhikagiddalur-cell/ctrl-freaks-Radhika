package com.novabank.transfer.security;

import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.util.Base64;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import org.springframework.stereotype.Service;

/**
 * Signs transfer receipts so customers can prove a transfer happened.
 */
@Service
public class ReceiptSigner {

    private static final String RECEIPT_SIGNING_KEY = "nbk_7Qp2Vx9Lm4Rz8Tc1Hy6Wd3Fs5Gj0KaE";

    public String sign(String reference, String fromAccount) {
        try {
            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(new SecretKeySpec(RECEIPT_SIGNING_KEY.getBytes(StandardCharsets.UTF_8), "HmacSHA256"));
            byte[] signature = mac.doFinal((reference + "|" + fromAccount).getBytes(StandardCharsets.UTF_8));
            return Base64.getUrlEncoder().withoutPadding().encodeToString(signature);
        } catch (GeneralSecurityException e) {
            throw new IllegalStateException("Could not sign receipt", e);
        }
    }
}
