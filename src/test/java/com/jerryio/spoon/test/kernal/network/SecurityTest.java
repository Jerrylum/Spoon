package com.jerryio.spoon.test.kernal.network;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.fail;

import java.security.GeneralSecurityException;

import com.jerryio.spoon.kernal.network.security.AESUtils;
import com.jerryio.spoon.kernal.network.security.EncryptionManager;
import com.jerryio.spoon.kernal.network.security.RSAUtils;

import org.junit.Test;

public class SecurityTest {
    @Test
    public void testUtilsConstructor() {
        new AESUtils();
        new RSAUtils();
    }

    @Test
    public void testEncryptionManager() throws GeneralSecurityException {
        EncryptionManager manager = new EncryptionManager();

        manager.makeRSACiphers(RSAUtils.createKeys(2048));

        try {
            manager.makeRSACiphers(null);
            fail("should failed");
        } catch (Exception e) {
            // ok
        }

        manager.makeAESCiphers(AESUtils.createKey(128), AESUtils.createIv(16));
        manager.makeAESCiphers(null, null);
        
        manager.setRSAEncryptCipher(null);
        manager.setRSADecryptCipher(null);
        manager.setAESEncryptCipher(null);
        manager.setAESDecryptCipher(null);

        assertNull(manager.getRSAEncryptCipher());
        assertNull(manager.getRSADecryptCipher());
        assertNull(manager.getAESEncryptCipher());
        assertNull(manager.getAESEncryptCipher());
    }
}
