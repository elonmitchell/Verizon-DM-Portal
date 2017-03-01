/**
 * 
 */
package motive.reports.reportconsole.util;

import javax.crypto.Cipher;

/**
 * @author arponnus
 *
 */
public interface LoginCipher {
	
	final int iterationCount = 16;
	
	byte[] salt = {

		(byte)0xB2, (byte)0x12, (byte)0xD5, (byte)0xB2,

		(byte)0x44, (byte)0x21, (byte)0xC3, (byte)0xC3
	};
	
	public void init();
	
	public String encrypt();
	
	public String decrypt();

}
