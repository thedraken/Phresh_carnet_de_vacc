package com.phresh.security;

import org.jetbrains.annotations.NotNull;
import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class PasswordEncryptor {

    public String encryptPassword(String password) {
        return getPbkdf2PasswordEncoder().encode(password);
    }

    private @NotNull Pbkdf2PasswordEncoder getPbkdf2PasswordEncoder() {
        return new Pbkdf2PasswordEncoder(new String(new char[]{'v', 't', 'x', '1', 'p', 'm', 'o', 'r', 't', 'P', 'h', 'r', 'e', 's', 'h', '2', '0', '2', '5', '!', '-'}),
                32, 2048, Pbkdf2PasswordEncoder.SecretKeyFactoryAlgorithm.PBKDF2WithHmacSHA512);
    }
}
