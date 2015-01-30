package utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5 {
	private static final char HEX_DIGITS[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };

	public static String toHexString(byte[] b) {
		StringBuilder sb = new StringBuilder(b.length * 2);
		for (int i = 0; i < b.length; i++) {
			sb.append(HEX_DIGITS[(b[i] & 0xf0) >>> 4]);
			sb.append(HEX_DIGITS[b[i] & 0x0f]);
		}
		return sb.toString();
	}

	public static String calculateMD5(String filename) {
		InputStream fis;
		byte[] buffer = new byte[1024];
		int numRead = 0;
		MessageDigest md5;
		try {
			fis = new FileInputStream(filename);
			md5 = MessageDigest.getInstance("MD5");
			while ((numRead = fis.read(buffer)) > 0) {
				md5.update(buffer, 0, numRead);
			}
			fis.close();
			return toHexString(md5.digest());
		} catch (Exception e) {
			System.out.println("error");
			return null;
		}
	}

	public static boolean checkMD5(String paramString, String localFile) {
		boolean flag = false;
		boolean bool = (null == paramString || "".equals(paramString));
		if (!bool && (localFile != null)) {
			File local = new File(localFile);
			bool = local.exists();
			if (!bool) {
				return flag;
			}
		}
		String localFileMD5 = null;
		try {
			localFileMD5 = calculateMD5(localFile);
			if (localFileMD5 != null) {
				flag = paramString.equalsIgnoreCase(localFileMD5);
			}

		} catch (RuntimeException e) {
			e.printStackTrace();
		}
		return flag;
	}

	/**
	 * 
	 * 
	 * 
	 * @param password
	 * 
	 *            需要加密的字符串
	 * 
	 * @return 加密后的字符串
	 * 
	 * @throws NoSuchAlgorithmException
	 */

	public static String md5Digest(String password) {

		String temp = "";

		MessageDigest alg;
		try {
			alg = MessageDigest.getInstance("MD5");
			alg.update(password.getBytes());

			byte[] digest = alg.digest();
			temp = byte2hex(digest);
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return temp;

	}

	public static String getMd5ByFile(File file) {
		String value = null;
		FileInputStream in = null ;
		try {
			in = new FileInputStream(file);
			MappedByteBuffer byteBuffer = in.getChannel().map(FileChannel.MapMode.READ_ONLY, 0, file.length());
			MessageDigest md5 = MessageDigest.getInstance("MD5");
			md5.update(byteBuffer);
			BigInteger bi = new BigInteger(1, md5.digest());
			value = bi.toString(16);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (null != in) {
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return value;
	}
	public static String getMd5ByFile(String filePath) {
		return getMd5ByFile(new File(filePath)) ;
	}

	/*
	 * 
	 * 二进制转换为十六进制字符串
	 * 
	 * 
	 * 
	 * @param b 二进制数组 @return 十六进制字符串
	 */

	private static String byte2hex(byte[] bytes) {

		String hs = "";

		String stmp = "";

		for (int i = 0; i < bytes.length; i++) {

			stmp = (java.lang.Integer.toHexString(bytes[i] & 0XFF));

			if (stmp.length() == 1)

				hs = hs + "0" + stmp;

			else

				hs = hs + stmp;

		}

		return hs.toUpperCase();

	}
}
