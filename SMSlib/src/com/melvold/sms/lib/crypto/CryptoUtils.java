package com.melvold.sms.lib.crypto;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import de.rtner.misc.BinTools;
import de.rtner.security.auth.spi.PBKDF2Engine;
import de.rtner.security.auth.spi.PBKDF2Formatter;
import de.rtner.security.auth.spi.PBKDF2HexFormatter;
import de.rtner.security.auth.spi.PBKDF2Parameters;

public class CryptoUtils {

	private static final String SALT_PRNG = "SHA1PRNG";
	private static final String MD5 = "md5";
	private static final String HMAC = "HmacSHA256";
	private static final String ENCODING = "UTF-8";

	public static String md5(String input){
		String res = "";
		try {
			MessageDigest algorithm = MessageDigest.getInstance(MD5);
			algorithm.reset();
			algorithm.update(input.getBytes());
			byte[] md5 = algorithm.digest();
			String tmp = "";
			for (int i = 0; i < md5.length; i++) {
				tmp = (Integer.toHexString(0xFF & md5[i]));
				if (tmp.length() == 1) {
					res += "0" + tmp;
				} else {
					res += tmp;
				}
			}
		} catch (NoSuchAlgorithmException ex) {}
		return res;
	}

	public static String generatePBKDF2Key(byte[] salt, String password){
		PBKDF2Parameters p = new PBKDF2Parameters(HMAC, ENCODING, salt, 4096);

		PBKDF2Engine e = new PBKDF2Engine(p);
		p.setDerivedKey(e.deriveKey(password, 16));
		PBKDF2Formatter formatter = new PBKDF2HexFormatter();
		String key = formatter.toString(p);

		return key.split(":")[2];
	}

	public static String generateNewSalt(){
		SecureRandom sr;
		try {
			sr = SecureRandom.getInstance(SALT_PRNG);
			byte[] salt = new byte[16];
			sr.nextBytes(salt);
			return BinTools.bin2hex(salt);
		} catch (NoSuchAlgorithmException e) {
			return null;
		}
	}

}
