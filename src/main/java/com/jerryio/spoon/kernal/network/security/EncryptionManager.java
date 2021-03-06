package com.jerryio.spoon.kernal.network.security;

import java.security.GeneralSecurityException;
import java.security.KeyPair;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;

public class EncryptionManager {
    
    private Cipher rsaEncryptCipher;
    private Cipher rsaDecryptCipher;
    private Cipher aesEncryptCipher;
    private Cipher aesDecryptCipher;
    private RSAPublicKey rsaPublicKey;
    private RSAPrivateKey rsaPrivateKey;
    private SecretKey aesSecretKey;
    private IvParameterSpec aesIV;

    public Cipher getRSAEncryptCipher() {
        return this.rsaEncryptCipher;
    }

    public void setRSAEncryptCipher(Cipher rsaEncryptCipher) {
        this.rsaEncryptCipher = rsaEncryptCipher;
    }

    public Cipher getRSADecryptCipher() {
        return this.rsaDecryptCipher;
    }

    public void setRSADecryptCipher(Cipher rsaDecryptCipher) {
        this.rsaDecryptCipher = rsaDecryptCipher;
    }

    public Cipher getAESEncryptCipher() {
        return this.aesEncryptCipher;
    }

    public void setAESEncryptCipher(Cipher aesEncryptCipher) {
        this.aesEncryptCipher = aesEncryptCipher;
    }

    public Cipher getAESDecryptCipher() {
        return this.aesDecryptCipher;
    }

    public void setAESDecryptCipher(Cipher aesDecryptCipher) {
        this.aesDecryptCipher = aesDecryptCipher;
    }

    public RSAPublicKey getRSAPublicKey() {
        return this.rsaPublicKey;
    }

    public RSAPrivateKey getRSAPrivateKey() {
        return this.rsaPrivateKey;
    }

    public SecretKey getAESSecretKey() {
        return this.aesSecretKey;
    }

    public IvParameterSpec getAESIV() {
        return this.aesIV;
    }

    public void makeRSACiphers(KeyPair keys) throws GeneralSecurityException {
        makeRSAPublicEncryptCipher((RSAPublicKey) keys.getPublic());
        makeRSAPrivateDecryptCipher((RSAPrivateKey) keys.getPrivate());
    }
    
    public void makeRSAPublicEncryptCipher(RSAPublicKey key) throws GeneralSecurityException {
        rsaPublicKey = key;
        rsaEncryptCipher = Cipher.getInstance("RSA/ECB/PKCS1Padding"); //NoSuchPaddingException
        rsaEncryptCipher.init(Cipher.ENCRYPT_MODE, getRSAPublicKey()); //InvalidKeyException
    }

    public void makeRSAPrivateDecryptCipher(RSAPrivateKey key) throws GeneralSecurityException {
        rsaPrivateKey = key;
        rsaDecryptCipher = Cipher.getInstance("RSA/ECB/PKCS1Padding"); //NoSuchPaddingException
        rsaDecryptCipher.init(Cipher.DECRYPT_MODE, getRSAPrivateKey()); //InvalidKeyException
    }

    public void makeAESCiphers(SecretKey key, IvParameterSpec iv) throws GeneralSecurityException {
        aesSecretKey = key;
        aesIV = iv;
        if (key == null || iv == null) {
            aesEncryptCipher = null;
            aesDecryptCipher = null;
            return;
        }
        aesEncryptCipher = Cipher.getInstance("AES/CFB8/NoPadding");
        aesEncryptCipher.init(Cipher.ENCRYPT_MODE, getAESSecretKey(), getAESIV());
        aesDecryptCipher = Cipher.getInstance("AES/CFB8/NoPadding");
        aesDecryptCipher.init(Cipher.DECRYPT_MODE, getAESSecretKey(), getAESIV());
    }

    public byte[] encode(byte[] data) throws GeneralSecurityException {
        if (getAESEncryptCipher() != null)
            return getAESEncryptCipher().doFinal(data);
        else if (getRSAEncryptCipher() != null)
            return getRSAEncryptCipher().doFinal(data);
        else
            return data;
    }

    public byte[] decode(byte[] data) throws GeneralSecurityException {
        if (getAESDecryptCipher() != null)
            return getAESDecryptCipher().doFinal(data);
        else if (getRSADecryptCipher() != null)
            return getRSADecryptCipher().doFinal(data);
        else
            return data;
    }

}
