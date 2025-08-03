package com.phresh.security;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder;

@Configuration
@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
public class PasswordEncryptor {

    private final Pbkdf2PasswordEncoder passwordEncoder;

    protected PasswordEncryptor() {
        passwordEncoder = new Pbkdf2PasswordEncoder(new String(new char[]{'v', 't', 'x', '1', 'p', 'm', 'o', 'r', 't', 'P', 'h', 'r', 'e', 's', 'h', '2', '0', '2', '5', '!', '-'}),
                32, 2048, Pbkdf2PasswordEncoder.SecretKeyFactoryAlgorithm.PBKDF2WithHmacSHA512);
    }

    public String encryptPassword(String password) {
        return passwordEncoder.encode(password);
    }

    public boolean passwordMatches(String expectedPassword, String encryptedPassword) {
        return passwordEncoder.matches(expectedPassword, encryptedPassword);
    }
}
