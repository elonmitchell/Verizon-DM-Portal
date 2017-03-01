/**
 * 
 */
package motive.reports.reportconsole.util;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.AlgorithmParameterSpec;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Date;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;

import org.apache.log4j.Logger;

import com.sun.mail.util.BASE64DecoderStream;
import com.sun.mail.util.BASE64EncoderStream;

/**
 * @author arponnus
 *
 */
public class LoginCipherImpl implements LoginCipher {
	
	private Cipher loginCipher = null;
	
	private String secureMessage = "";
	
	private int cipherMode = Cipher.DECRYPT_MODE;
	
	private static Logger log = Logger.getLogger(LoginCipherImpl.class);
	
	private final String  PASS_PHRASE = "pass_phrase";
	
	
	/**
	 * @param cipherMode
	 * @param secureMessage
	 */
	public LoginCipherImpl(int cipherMode, String secureMessage) {
		this.cipherMode = cipherMode;
		this.secureMessage = secureMessage;
		init();
	}
	
	private Cipher getCipherByType(int cipherMode) {
		String passPhrase = null;
		
		ReportSystemPropertyAdapter prop = ReportSystemPropertyAdapter.getInstance();
		
		passPhrase = prop.getPropValue(PASS_PHRASE);
		
		if(passPhrase != null){

			try {
				
				// provide password, salt, iteration count for generating PBEKey of fixed-key-size PBE ciphers
				KeySpec keySpec = new PBEKeySpec(passPhrase.toCharArray(), salt, iterationCount);

				// construct a parameter set for password-based encryption as defined in the PKCS #5 standard
				AlgorithmParameterSpec paramSpec = new PBEParameterSpec(salt, iterationCount);
				
				// create a secret (symmetric) key using PBE with MD5 and DES 
				SecretKey key = SecretKeyFactory.getInstance("PBEWithMD5AndDES").generateSecret(keySpec);

				loginCipher = Cipher.getInstance(key.getAlgorithm());

				// initialize the ciphers with the given key
				loginCipher.init(cipherMode,key, paramSpec);

			} catch (InvalidKeySpecException e) {
				e.printStackTrace();
				log.error(e.getCause());
	            log.error(e);
			} catch (NoSuchAlgorithmException e) {
				e.printStackTrace();
				log.error(e.getCause());
	            log.error(e);
			} catch (NoSuchPaddingException e) {
				e.printStackTrace();
				log.error(e.getCause());
	            log.error(e);
			} catch (InvalidKeyException e) {
				e.printStackTrace();
				log.error(e.getCause());
	            log.error(e);
			} catch (InvalidAlgorithmParameterException e) {
				e.printStackTrace();
				log.error(e.getCause());
	            log.error(e);
	        }

		}else {
			passPhrase = "";
			log.error("PassPrash not configured in Environment properties. Please configure pass_phrase");
		}
				
		
		return loginCipher;
	}

	/* (non-Javadoc)
	 * @see motive.reports.reportconsole.util.LoginCipher#decrypt(java.lang.String)
	 */
	@Override
	public String decrypt() {

		try {

			// decode with base64 to get bytes

			byte[] dec = BASE64DecoderStream.decode(secureMessage.getBytes());

			byte[] utf8 = loginCipher.doFinal(dec);

			// create new string based on the specified charset

			return new String(utf8, "UTF8");

		}

		catch (Exception e) {

			e.printStackTrace();

		}

		return null;

	}

	/* (non-Javadoc)
	 * @see motive.reports.reportconsole.util.LoginCipher#encrypt(java.lang.String)
	 */
	@Override
	public String encrypt() {

		try {

			// encode the string into a sequence of bytes using the named charset
			byte[] utf8 = secureMessage.getBytes("UTF8");

			byte[] enc = loginCipher.doFinal(utf8);

			// encode to base64
			enc = BASE64EncoderStream.encode(enc);

			return new String(enc);

		}

		catch (Exception e) {

			e.printStackTrace();

		}

		return null;

	}

	@Override
	public void init() {
		getCipherByType(cipherMode);
		
	}
	
	public static void main(String[] args) {
		
		LoginCipherImpl enCipher = new LoginCipherImpl(Cipher.ENCRYPT_MODE,"dmportal|" + new Date().toLocaleString());
		String finalenCipher = enCipher.encrypt();
		LoginCipherImpl deCipher = new LoginCipherImpl(Cipher.DECRYPT_MODE, finalenCipher);
		String finalString = deCipher.decrypt();
		System.out.println(finalString);
	}
}
