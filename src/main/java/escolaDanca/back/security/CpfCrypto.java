package escolaDanca.back.security;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

@Converter
public class CpfCrypto implements AttributeConverter<String, String> {

    private static final String ALGORITHM = "AES";
    private static final byte[] SECRET_KEY_BYTES = new byte[]{
            0x30, 0x31, 0x32, 0x33, 0x34, 0x35, 0x36, 0x37,
            0x38, 0x39, 0x40, 0x41, 0x42, 0x43, 0x44, 0x45,
            0x46, 0x47, 0x48, 0x49, 0x50, 0x51, 0x52, 0x53,
            0x54, 0x55, 0x56, 0x57, 0x58, 0x59, 0x60, 0x61
    };
    private static final SecretKeySpec SECRET_KEY = new SecretKeySpec(SECRET_KEY_BYTES, ALGORITHM);

    @Override
    public String convertToDatabaseColumn(String cpf) {
        if (cpf == null) return null;
        try {
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, SECRET_KEY);
            return Base64.getEncoder().encodeToString(cipher.doFinal(cpf.getBytes()));
        } catch (Exception e) {
            throw new RuntimeException("Erro ao criptografar CPF", e);
        }
    }

    @Override
    public String convertToEntityAttribute(String dbData) {
        if (dbData == null) return null;
        try {
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, SECRET_KEY);
            return new String(cipher.doFinal(Base64.getDecoder().decode(dbData)));
        } catch (Exception e) {
            throw new RuntimeException("Erro ao descriptografar CPF", e);
        }
    }

    public static String encryptCpf(String cpf) throws Exception {
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, SECRET_KEY);
        byte[] encryptedBytes = cipher.doFinal(cpf.getBytes());
        return Base64.getEncoder().encodeToString(encryptedBytes);
    }

    public static String decryptCpf(String encryptedCpf) throws Exception {
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, SECRET_KEY);
        byte[] decryptedBytes = cipher.doFinal(Base64.getDecoder().decode(encryptedCpf));
        return new String(decryptedBytes);
    }
}


