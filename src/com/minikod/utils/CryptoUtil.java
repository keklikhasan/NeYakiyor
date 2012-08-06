package com.minikod.utils;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @author hkeklik
 * 
 */
public class CryptoUtil {

	public static String md5(byte[] value) throws NoSuchAlgorithmException {
		MessageDigest digest;
		try {
			digest = MessageDigest.getInstance("MD5");
			digest.update(value, 0, value.length);
			BigInteger i = new BigInteger(1, digest.digest());
			return String.format("%1$032X", i);
		} finally {
			digest = null;
		}
	}
}
