package com.jerryio.spoon.kernal.network.security;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;

public class AESUtils {
    
    public static final String AES_ALGORITHM = "AES";

    public static SecretKey createKey(int keySize) {
        KeyGenerator generator;
        try {
            generator = KeyGenerator.getInstance(AES_ALGORITHM);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e.getMessage());
        }

        generator.init(keySize);
        return generator.generateKey();
    }

    public static IvParameterSpec createIv(int size) {
        try {
            byte[] iv_byte = new byte[size];
            SecureRandom.getInstanceStrong().nextBytes(iv_byte);
            return new IvParameterSpec(iv_byte);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}
