package com.phresh.security;


import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Base64;

public class PasswordEncryptor {

    private final SecretKey passwordEncoder;
    private final byte[] salt;

    public PasswordEncryptor() throws NoSuchAlgorithmException, InvalidKeySpecException {
        SecureRandom random = new SecureRandom();
        salt = new byte[16];
        random.nextBytes(salt);
        KeySpec spec = new PBEKeySpec(new char[]{'v', 't', 'x', '1', 'p', 'm', 'o', 'r', 't', 'P', 'h', 'r', 'e', 's', 'h', '2', '0', '2', '5', '!', '-'}, salt, 65536, 128);
        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        passwordEncoder = factory.generateSecret(spec);
    }

    private Cipher prepareCipher(int cipherMode) throws NoSuchPaddingException, NoSuchAlgorithmException,
            InvalidAlgorithmParameterException, InvalidKeyException {
        IvParameterSpec ivSpec = new IvParameterSpec(salt);
        Cipher cipher = Cipher.getInstance("PKCS5Padding");
        cipher.init(cipherMode, passwordEncoder, ivSpec);
        return cipher;
    }

    public String encode(String rawPassword) throws InvalidAlgorithmParameterException, NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeySpecException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        Cipher cipher = prepareCipher(Cipher.ENCRYPT_MODE);
        return Base64.getEncoder().encodeToString(cipher.doFinal(rawPassword.getBytes(StandardCharsets.UTF_8)));
    }

    public boolean matches(String rawPassword, String encodedPassword) throws InvalidAlgorithmParameterException, NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        Cipher cipher = prepareCipher(Cipher.DECRYPT_MODE);
        String decodedPassword = new String(cipher.doFinal(Base64.getDecoder().decode(encodedPassword.getBytes(StandardCharsets.UTF_8))));
        return rawPassword.compareTo(decodedPassword) == 0;
    }
}
