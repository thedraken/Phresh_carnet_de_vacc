package com.phresh;

import com.phresh.security.PasswordEncryptor;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(MockitoExtension.class)
public class PasswordEncryptorTest {

    private final String keyWord = "ThisIsATestStringForThePasswordEncoder123456789-?";

    private final List<String> encryptedPasswords = new ArrayList<>();

    @RepeatedTest(50)
    @Order(1)
    public void testEncode() {
        PasswordEncryptor passwordEncryptor = new PasswordEncryptor();
        String newValue = passwordEncryptor.encode(keyWord);
        Assertions.assertNotEquals(keyWord, newValue);
        encryptedPasswords.add(newValue);
    }

    @Test
    @Order(2)
    public void testMatches() {
        PasswordEncryptor passwordEncryptor = new PasswordEncryptor();
        encryptedPasswords.forEach(encryptedPassword -> Assertions.assertTrue(passwordEncryptor.matches(passwordEncryptor.encode(keyWord), encryptedPassword)));
    }
}
