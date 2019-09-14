package com.assignment.util;

import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.stereotype.Component;

@Component
public class AESCipher {
	
	private static final String KEY = "restAssignment12";
	private static final Key aesKey =  new SecretKeySpec(KEY.getBytes(), "AES");
	
	private final Cipher encryptCipher;
	private final Cipher decryptCipher;
	
	public AESCipher() throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException {
		encryptCipher = Cipher.getInstance("AES");
		encryptCipher.init(Cipher.ENCRYPT_MODE,aesKey);
		
		decryptCipher = Cipher.getInstance("AES");
		decryptCipher.init(Cipher.DECRYPT_MODE,aesKey);
	}
    
	public String getEncryptedText(String text) throws IllegalBlockSizeException, BadPaddingException {
		byte[] encrypted = encryptCipher.doFinal(text.getBytes());
		return new String(encrypted);
	}
	
	public String getDecryptedText(String text) throws IllegalBlockSizeException, BadPaddingException {
		byte[] decrypted = decryptCipher.doFinal(text.getBytes());
		return new String(decrypted);
	}
}
