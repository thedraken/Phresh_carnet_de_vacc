import com.phresh.security.PasswordEncryptor;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;
import java.util.List;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(MockitoExtension.class)
public class PasswordEncryptorTest {

    private final String keyWord = "ThisIsATestStringForThePasswordEncoder123456789-?";

    private final List<String> encryptedPasswords = new ArrayList<>();

    @RepeatedTest(50)
    @Order(1)
    public void testEncode() throws NoSuchAlgorithmException, InvalidKeySpecException, InvalidAlgorithmParameterException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, InvalidKeyException {
        PasswordEncryptor passwordEncryptor = new PasswordEncryptor();
        String newValue = passwordEncryptor.encode(keyWord);
        Assertions.assertNotEquals(keyWord, newValue);
        encryptedPasswords.add(newValue);
    }

    @Test
    @Order(2)
    public void testMatches() throws NoSuchAlgorithmException, InvalidKeySpecException, InvalidAlgorithmParameterException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, InvalidKeyException {
        PasswordEncryptor passwordEncryptor = new PasswordEncryptor();
        for (String encryptedPassword : encryptedPasswords) {
            Assertions.assertTrue(passwordEncryptor.matches(passwordEncryptor.encode(keyWord), encryptedPassword));
        }
    }
}
