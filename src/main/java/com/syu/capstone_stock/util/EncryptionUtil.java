package com.syu.capstone_stock.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

@PropertySource("classpath:application.properties")
@Component
public class EncryptionUtil {
    private static String AES_SECRET_KEY;
    private static final String AES_ALGORITHM = "AES";
    private static final String SHA256_ALGORITHM = "SHA-256";

    @Value("${KEY}")
    private void setKey(String value){
        AES_SECRET_KEY = value;
    }

    /**
     * aes 암호화 알고리즘으로 인코딩
     * @param data 인코딩할 문자열
     * @return 인코딩된 문자열
     */
    public static String aesEncode(String data) {
        try {
            SecretKeySpec secretKeySpec = new SecretKeySpec(AES_SECRET_KEY.getBytes(), AES_ALGORITHM);
            Cipher cipher = Cipher.getInstance(AES_ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);
            byte[] encryptedData = cipher.doFinal(data.getBytes());

            return Base64.getEncoder().encodeToString(encryptedData);
        } catch (Exception e) {
            System.out.println("aes encoding fail");
            return null;
        }
    }

    /**
     * aes 암호화 알고리즘으로 디코딩
     * @param data 디코딩할 문자열
     * @return 디코딩된 문자열
     */
    public static String aesDecode(String data) {
        try {
            SecretKeySpec secretKeySpec = new SecretKeySpec(AES_SECRET_KEY.getBytes(), AES_ALGORITHM);
            Cipher cipher = Cipher.getInstance(AES_ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, secretKeySpec);
            byte[] decryptedData = cipher.doFinal(Base64.getDecoder().decode(data));

            return new String(decryptedData);
        } catch (Exception e) {
            System.out.println("aes decoding fail");
            return null;
        }
    }
    /**
     * sha256 암호화 알고리즘으로 인코딩
     * @param data 인코딩할 문자열
     * @return 인코딩된 문자열
     */
    public static String sha256Encode(String data) {
        try {
            MessageDigest digest = MessageDigest.getInstance(SHA256_ALGORITHM);
            byte[] hash = digest.digest(data.getBytes());

            return Base64.getEncoder().encodeToString(hash);
        } catch (NoSuchAlgorithmException e) {
            System.out.println("sha256 encoding fail");
            return null;
        }
    }
}
