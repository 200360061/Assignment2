package store;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

public class PasswordGenerator {

    public static String getSHA512Password(String passwordToEncrypt, byte[] salt) {
        String generatedPassword = null;

        try {
            StringBuilder stringBuilder = new StringBuilder();
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-512");
            messageDigest.update(salt);
            byte[] bytes = messageDigest.digest(passwordToEncrypt.getBytes());

            for (int i = 0; i < bytes.length; i++) {
                stringBuilder.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
            }
            generatedPassword = stringBuilder.toString();
        } catch (NoSuchAlgorithmException ex) {
            System.err.println(ex.getMessage());
        }

        return generatedPassword;
    }

    public static byte[] getSalt() throws NoSuchAlgorithmException {
        SecureRandom secureRandom = SecureRandom.getInstanceStrong();
        byte[] salt = new byte[16];
        secureRandom.nextBytes(salt);
        return salt;
    }
}
