package com.phresh.security;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder;

@Configuration
@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
public class PasswordEncryptor implements PasswordEncoder {

    private final Pbkdf2PasswordEncoder passwordEncoder;

    protected PasswordEncryptor() {
        passwordEncoder = new Pbkdf2PasswordEncoder(new String(new char[]{'v', 't', 'x', '1', 'p', 'm', 'o', 'r', 't', 'P', 'h', 'r', 'e', 's', 'h', '2', '0', '2', '5', '!', '-'}),
                32, 2048, Pbkdf2PasswordEncoder.SecretKeyFactoryAlgorithm.PBKDF2WithHmacSHA512);
    }

    @Override
    public String encode(CharSequence rawPassword) {
        return passwordEncoder.encode(rawPassword);
    }

    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }
}
