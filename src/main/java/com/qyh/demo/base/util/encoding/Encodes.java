/**
 * Copyright (c) 2005-2012 springside.org.cn
 */
package com.qyh.demo.base.util.encoding;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

import com.qyh.demo.base.util.Exceptions;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.lang3.StringEscapeUtils;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

/**
 * 封装各种格式的编码解码工具类.
 * 1.Commons-Codec的 hex/base64 编码
 * 2.自制的base62 编码
 * 3.Commons-Lang的xml/html escape
 * 4.JDK提供的URLEncoder
 * @author calvin
 * @version 2013-01-15
 */
@SuppressWarnings("restriction")
public class Encodes {

	private static final String DEFAULT_URL_ENCODING = "UTF-8";
	private static final char[] BASE62 = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz".toCharArray();

	/**
	 * Hex编码.
	 */
	public static String encodeHex(byte[] input) {
		return new String(Hex.encodeHex(input));
	}

	/**
	 * Hex解码.
	 */
	public static byte[] decodeHex(String input) {
		try {
			return Hex.decodeHex(input.toCharArray());
		} catch (DecoderException e) {
			throw Exceptions.unchecked(e);
		}
	}

	/**
	 * Base64编码.
	 */
	public static String encodeBase64(byte[] input) {
		return new String(Base64.encodeBase64(input));
	}
	
	/**
	 * Base64编码.
	 */
	public static String encodeBase64(String input) {
		try {
			return new String(Base64.encodeBase64(input.getBytes(DEFAULT_URL_ENCODING)));
		} catch (UnsupportedEncodingException e) {
			return "";
		}
	}

//	/**
//	 * Base64编码, URL安全(将Base64中的URL非法字符'+'和'/'转为'-'和'_', 见RFC3548).
//	 */
//	public static String encodeUrlSafeBase64(byte[] input) {
//		return Base64.encodeBase64URLSafe(input);
//	}

	/**
	 * Base64解码.
	 */
	public static byte[] decodeBase64(String input) {
		return Base64.decodeBase64(input.getBytes());
	}
	
	/**
	 * Base64解码.
	 */
	public static String decodeBase64String(String input) {
		try {
			return new String(Base64.decodeBase64(input.getBytes()), DEFAULT_URL_ENCODING);
		} catch (UnsupportedEncodingException e) {
			return "";
		}
	}

	/**
	 * Base62编码。
	 */
	public static String encodeBase62(byte[] input) {
		char[] chars = new char[input.length];
		for (int i = 0; i < input.length; i++) {
			chars[i] = BASE62[((input[i] & 0xFF) % BASE62.length)];
		}
		return new String(chars);
	}

	/**
	 * Html 转码.
	 */
	public static String escapeHtml(String html) {
		return StringEscapeUtils.escapeHtml4(html);
	}

	/**
	 * Html 解码.
	 */
	public static String unescapeHtml(String htmlEscaped) {
		return StringEscapeUtils.unescapeHtml4(htmlEscaped);
	}

	/**
	 * Xml 转码.
	 */
	public static String escapeXml(String xml) {
		return StringEscapeUtils.escapeXml10(xml);
	}

	/**
	 * Xml 解码.
	 */
	public static String unescapeXml(String xmlEscaped) {
		return StringEscapeUtils.unescapeXml(xmlEscaped);
	}

	/**
	 * URL 编码, Encode默认为UTF-8. 
	 */
	public static String urlEncode(String part) {
		try {
			return URLEncoder.encode(part, DEFAULT_URL_ENCODING);
		} catch (UnsupportedEncodingException e) {
			throw Exceptions.unchecked(e);
		}
	}

	/**
	 * URL 解码, Encode默认为UTF-8. 
	 */
	public static String urlDecode(String part) {

		try {
			return URLDecoder.decode(part, DEFAULT_URL_ENCODING);
		} catch (UnsupportedEncodingException e) {
			throw Exceptions.unchecked(e);
		}
	}
	
	/**
	 * 加密--不直接使用
	 * 
	 * @param datasource
	 *            byte[]
	 * @param password
	 *            String
	 * @return byte[]
	 */
	public static byte[] encrypt(byte[] datasource, String password) {
		try {
			SecureRandom random = new SecureRandom();
			DESKeySpec desKey = new DESKeySpec(password.getBytes());
			// 创建一个密匙工厂，然后用它把DESKeySpec转换成
			SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
			SecretKey securekey = keyFactory.generateSecret(desKey);
			// Cipher对象实际完成加密操作
			Cipher cipher = Cipher.getInstance("DES");
			// 用密匙初始化Cipher对象
			cipher.init(Cipher.ENCRYPT_MODE, securekey, random);
			// 现在，获取数据并加密
			// 正式执行加密操作
			return cipher.doFinal(datasource);
		} catch (Throwable e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 解密--不直接使用
	 * 
	 * @param src
	 *            byte[]
	 * @param password
	 *            String
	 * @return byte[]
	 * @throws Exception
	 */
	public static byte[] decrypt(byte[] src, String password) {
		try {
			// DES算法要求有一个可信任的随机数源
			SecureRandom random = new SecureRandom();
			// 创建一个DESKeySpec对象
			DESKeySpec desKey = new DESKeySpec(password.getBytes());
			// 创建一个密匙工厂
			SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
			// 将DESKeySpec对象转换成SecretKey对象
			SecretKey securekey = keyFactory.generateSecret(desKey);
			// Cipher对象实际完成解密操作
			Cipher cipher = Cipher.getInstance("DES");
			// 用密匙初始化Cipher对象
			cipher.init(Cipher.DECRYPT_MODE, securekey, random);
			// 真正开始解密操作
			return cipher.doFinal(src);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * Description 根据键值进行加密
	 * 
	 * @param data
	 * @param key
	 *            加密键byte数组--在cofig中有定义DES_PASSWORD
	 * @return
	 * @throws Exception
	 */
	public static String encrypt(String data, String key) {
		byte[] bt = encrypt(data.getBytes(), key);
		String strs = new BASE64Encoder().encode(bt);
		return strs;
	}

	/**
	 * Description 根据键值进行解密
	 * 
	 * @param data
	 * @param key
	 *            加密键byte数组
	 * @return
	 * @throws IOException
	 * @throws Exception
	 */
	public static String decrypt(String data, String key) {
		try {
			if (data == null)
				return null;
			BASE64Decoder decoder = new BASE64Decoder();
			byte[] buf = decoder.decodeBuffer(data);
			byte[] bt = decrypt(buf, key);
			return new String(bt);
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}

	/**
	 * 得到MD5摘要串
	 * 
	 * @param s
	 * @return
	 */
	public static String getMD5Str(String s) {
		// 用作十六进制的数组.
		byte hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };
		try {
			MessageDigest mdTemp = MessageDigest.getInstance("MD5".toUpperCase());// 使用MD5加密
			byte[] strTemp = s.getBytes();// 把传入的字符串转换成字节数组
			mdTemp.update(strTemp);//
			byte[] md = mdTemp.digest();
			int j = md.length;
			byte str[] = new byte[j * 2];
			int k = 0;
			for (int i = 0; i < j; i++) {
				byte byte0 = md[i];
				str[k++] = hexDigits[byte0 >>> 4 & 0xf];
				str[k++] = hexDigits[byte0 & 0xf];
			}
			return new String(str);// 返回加密后的字符串.
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}

	/**
	 * 得到MD5摘要串
	 * 
	 * @param s
	 *            待加密字符串
	 * @param charset
	 *            编码格式
	 * @return
	 */
	public static String getMD5Str(String s, String charset) {
		// 用作十六进制的数组.
		byte hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };
		try {
			MessageDigest mdTemp = MessageDigest.getInstance("MD5".toUpperCase());// 使用MD5加密
			byte[] strTemp = s.getBytes(charset);// 把传入的字符串转换成字节数组
			mdTemp.update(strTemp);//
			byte[] md = mdTemp.digest();
			int j = md.length;
			byte str[] = new byte[j * 2];
			int k = 0;
			for (int i = 0; i < j; i++) {
				byte byte0 = md[i];
				str[k++] = hexDigits[byte0 >>> 4 & 0xf];
				str[k++] = hexDigits[byte0 & 0xf];
			}
			return new String(str);// 返回加密后的字符串.
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}

	/**
	 * 判断是否是UTF-8编码
	 * 
	 * @param s
	 * @return
	 */
	public static boolean isutf(String s) {
		try {
			if (s != null && s.length() != 0) {
				String tmp = new String(s.getBytes("utf-8"), "utf-8");
				return s.equals(tmp);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			return false;
		}
		return true;
	}

	/**
	 * 判断是否是ISO-8859-1编码
	 * 
	 * @param s
	 * @return
	 */
	public static boolean isISO8859(String s) {
		try {
			if (s != null && s.length() != 0) {
				String tmp = new String(s.getBytes("ISO-8859-1"), "ISO-8859-1");
				return s.equals(tmp);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			return false;
		}
		return true;
	}

	public static void main(String[] args) throws Exception {
		System.out.println(Encodes.encodeHex("admin".getBytes()));
		/*
		 * String str = "13551883239"; //密码，长度要是8的倍数 byte[] result =
		 * encrypt(str.getBytes(),AppConfig.DES_PASSWORD); result =
		 * encrypt(result,AppConfig.DES_PASSWORD); System.out.println("加密后："+new
		 * String(result)); System.out.println(result.length); String s = new
		 * String(result, "GB2312"); //直接将如上内容解密 a+="@isDes";
		 * a=a.split("@isDes")[0]; try { byte[] decryResult = decrypt(result,
		 * AppConfig.DES_PASSWORD); System.out.println("解密后："+new
		 * String(decryResult)); } catch (Exception e1) { e1.printStackTrace();
		 * }
		 */
		// String data = "123 456";
		// System.err.println(encrypt(data, AppConfig.DES_PASSWORD));
		// System.err.println(decrypt(encrypt(data, AppConfig.DES_PASSWORD),
		// AppConfig.DES_PASSWORD));
	}
}
