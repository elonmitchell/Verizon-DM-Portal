package motive.reports.reportconsole.util;

import java.net.URLEncoder;
import java.security.spec.KeySpec;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;

import org.apache.commons.codec.binary.Base64;

public class URLEncodeCipher {
			
	private static final String UNICODE_FORMAT = "UTF8";
	public static final String ENCRYPTION_KEY = "VZWM0T1V32014DVZW4LUDMR3P0";
	public static final String DESEDE_ENCRYPTION_SCHEME = "DESede";
	private KeySpec ks;
	private SecretKeyFactory skf;
	private Cipher cipher;
	byte[] arrayBytes;
	private String myEncryptionScheme;
	SecretKey key;

	
	public URLEncodeCipher(String myEncryptionKey) throws Exception {
		myEncryptionScheme = DESEDE_ENCRYPTION_SCHEME;
		arrayBytes = myEncryptionKey.getBytes(UNICODE_FORMAT);
		ks = new DESedeKeySpec(arrayBytes);
		skf = SecretKeyFactory.getInstance(myEncryptionScheme);
		cipher = Cipher.getInstance(myEncryptionScheme);
		key = skf.generateSecret(ks);
	}

	public String encrypt(String unencryptedString) throws Exception {
		String encryptedString = null;
		try {
			cipher.init(Cipher.ENCRYPT_MODE, key);
			byte[] plainText = unencryptedString.getBytes(UNICODE_FORMAT);
			byte[] encryptedText = cipher.doFinal(plainText);
			encryptedString = new String(Base64.encodeBase64(encryptedText));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return encode(encryptedString);
	}
	    
	private String encode(String decodedString) throws Exception {
		return URLEncoder.encode(decodedString, "UTF-8");
	}
	    
	public static void main(String args[]) throws Exception {
		//Date d = new Date();
		//GregorianCalendar gc = new GregorianCalendar();
		//gc.setTime(d);
		//gc.set(GregorianCalendar.SECOND, 0);
		//gc.set(GregorianCalendar.MILLISECOND, 0);
		//Date d2 = gc.getTime();
		//System.out.println("Completed time: " + d2);
		//Long time = d2.getTime();
		//System.out.println("Completed time: " + time);
		URLEncodeCipher cipher = new URLEncodeCipher(ENCRYPTION_KEY);
		String target = "dmportal";
		System.out.println("String To Encrypt: " + target);
		String encryptedEncoded = cipher.encrypt(target);
		System.out.println("Encrypted String:" + encryptedEncoded);
	}
	
}
