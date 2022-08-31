package com.qyh.demo.base.util;

import java.math.BigInteger;
import java.util.Random;
/**
 * 进制转换工具
 * @author qiuyuehao
 *
 */
public class SysConvertUtil {
	
	public static void main(String[] args) {
		/*System.out.println(new Date().getTime());
		String s = toAnyConversion(new BigInteger(Long.parseLong(Kit.getDateNumber())+""), new BigInteger("35"));
		System.out.println(s);*/
//		System.out.println(toDecimal("A00000", 35));
		
	}
	
	//十进制转换中把字符转换为数
	private static int changeDec(char ch)
	    {
	        int num = 0;
	        if(ch >= 'A' && ch <= 'Z')
	            num = ch - 'A' + 10;
	        else if(ch >= 'a' && ch <= 'z')
	            num = ch - 'a' + 36;
	        else
	            num = ch - '0';
	        return num;
	    }

	/**
	 * 任意进制转换为10进制
	 * @param input	需要转化的数
	 * @param base	转化数的进制
	 * @return
	 */
	public static String toDecimal(String number, int base){
        BigInteger Bigtemp = BigInteger.ZERO, temp = BigInteger.ONE;
        int len = number.length();
        for(int i = len - 1; i >= 0; i--)
        {
            if(i != len - 1)
                temp = temp.multiply(BigInteger.valueOf(base));
            int num = changeDec(number.charAt(i));
            Bigtemp = Bigtemp.add(temp.multiply(BigInteger.valueOf(num)));
        }
        return Bigtemp.toString();
    }

	//数字转换为字符
	private static char changToNum(BigInteger temp){
	    int n = temp.intValue();
	
	    if(n >= 10 && n <= 35)
	        return (char) (n - 10 + 'A');
	
	    else if(n >= 36 && n <= 61)
	        return (char)(n - 36 + 'a');
	
	    else
	        return (char)(n + '0');
	}

	/**
	 * 十进制转换为任意进制
	 * @param Bigtemp	需要转化的数
	 * @param base		目标进制
	 * @return
	 */
	public static String toAnyConversion(BigInteger Bigtemp, BigInteger base){
	    String ans = "";
	    while(Bigtemp.compareTo(BigInteger.ZERO) != 0)
	    {
	        BigInteger temp = Bigtemp.mod(base);
	        Bigtemp = Bigtemp.divide(base);
	        char ch = changToNum(temp);
	        ans = ch + ans;
	    }
	    return ans;
	}
	
	
	/** 自定义进制(0,1没有加入,容易与o,l混淆) */
    private static final char[] r=new char[]{'A', 'W', 'E', '8', 'Q', 'S', '2', 'D', 'Z', 'X', '9', 'C', '7', 
    	'P', '5', 'I', 'K', '3', 'M', 'J', 'U', 'F', 'R', '4', 'V', 'Y', 'L', 'T', 'N', '6', 'B', 'G', 'H'};
    /** 进制长度 */
    private static final int binLen=r.length;
    /** 序列最小长度 */
    private static final int s=6;
    
    /**
     * 根据ID生成六位随机码
     * @param id ID
     * @return 随机码
     */
    public static String toSerialCode(long idNum) {
    	  char[] buf=new char[32];
          int charPos=32;
          
          while((idNum / binLen) > 0) {
              int ind=(int)(idNum % binLen);
              buf[--charPos]=r[ind];
              idNum /= binLen;
          }
          buf[--charPos]=r[(int)(idNum % binLen)];
          String str=new String(buf, charPos, (32 - charPos));
          // 不够长度的自动随机补全
          if(str.length() < s) {
              StringBuilder sb=new StringBuilder();
              Random rnd=new Random();
              for(int i=0; i < s - str.length(); i++) {
              sb.append(r[rnd.nextInt(binLen)]);
              }
              str+=sb.toString();
          }
          return str;
    }
}
