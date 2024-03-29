package com.qyh.demo.base.third.netease;

import java.security.MessageDigest;

/**
* @Description:    计算  网易云 接口请求令牌
* @CreateDate:     2019年02月19日 17:54:27
* @author qiuyuehao
*/
public class CheckSumBuilder {
	
	/**
	 * 计算并获取CheckSum
	 * @param appSecret  
	 * @param nonce  随机数
	 * @param curTime 当前UTC时间戳
	 * @return
	 * 2018年10月30日
	 * @author qiuyuehao
	 */
    public static String getCheckSum(String appSecret, String nonce, String curTime) {
        return encode("sha1", appSecret + nonce + curTime);
    }

    /**
     * 计算并获取md5值
     * @param requestBody
     * @return
     * 2018年10月30日
     * @author qiuyuehao
     */
    public static String getMD5(String requestBody) {
        return encode("md5", requestBody);
    }

    private static String encode(String algorithm, String value) {
        if (value == null) {
            return null;
        }
        try {
            MessageDigest messageDigest
                    = MessageDigest.getInstance(algorithm);
            messageDigest.update(value.getBytes());
            return getFormattedText(messageDigest.digest());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    private static String getFormattedText(byte[] bytes) {
        int len = bytes.length;
        StringBuilder buf = new StringBuilder(len * 2);
        for (int j = 0; j < len; j++) {
            buf.append(HEX_DIGITS[(bytes[j] >> 4) & 0x0f]);
            buf.append(HEX_DIGITS[bytes[j] & 0x0f]);
        }
        return buf.toString();
    }
    private static final char[] HEX_DIGITS = { '0', '1', '2', '3', '4', '5',
            '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };
}
