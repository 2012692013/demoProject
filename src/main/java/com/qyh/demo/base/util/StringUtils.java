/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.qyh.demo.base.util;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringEscapeUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.LocaleResolver;

import com.qyh.demo.base.util.encoding.Encodes;
import com.google.common.collect.Lists;

/**
 * 字符串工具类, 继承org.apache.commons.lang3.StringUtils类
 * 
 * @author ThinkGem
 * @version 2013-05-22
 */
public class StringUtils extends org.apache.commons.lang3.StringUtils {

	private static final char SEPARATOR = '_';
	private static final String CHARSET_NAME = "UTF-8";

	/**
	 * 得到随机数串
	 * 
	 * @param length
	 *            随机数的长度
	 * @return
	 */
	public static String getRadomNumber(int length) {
		Double r = Math.random() * Math.pow(10, length);
		String ra = String.valueOf(r.longValue());
		String radom = ra;
		if (ra.length() < length) {
			for (int i = 0; i < length - ra.length(); i++) {
				radom = "0" + radom;
			}
		}
		return radom;
	}

	/**
	 * 生成6位随机数
	 * 
	 * @return
	 */
	public static String randomSix() {
		int code = (int) ((Math.random() * 9 + 1) * 100000);
		return String.valueOf(code);
	}

	/**
	 * 判断字符串是否只含有数字或字符
	 * 
	 * @param input
	 * @return
	 */
	public static boolean isNumAndStr(String input) {
		if (input != null && input.matches("[0-9A-Za-z_]*")) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 判断字符串是否只含有数字
	 * 
	 * @param input
	 * @return
	 */
	public static boolean isNum(String input) {
		if (input != null && input.trim().length() != 0 && input.matches("[0-9]*")) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 判断字符串是否整数
	 * 
	 * @param input
	 * @return
	 */
	public static boolean isInt(String input) {
		if (input != null && input.matches("^[+-]?[\\d]+$")) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 判断字符串是否小数
	 * 
	 * @param input
	 * @return
	 */
	public static boolean isFloat(String input) {
		if (input != null && input.matches("^[+-]?[\\d]+\\.[\\d]+$")) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 转换为字节数组
	 * 
	 * @param str
	 * @return
	 */
	public static byte[] getBytes(String str) {
		if (str != null) {
			try {
				return str.getBytes(CHARSET_NAME);
			} catch (UnsupportedEncodingException e) {
				return null;
			}
		} else {
			return null;
		}
	}

	/**
	 * 转换为Boolean类型 'true', 'on', 'y', 't', 'yes' or '1' (case insensitive) will
	 * return true. Otherwise, false is returned.
	 */
	public static Boolean toBoolean(final Object val) {
		if (val == null) {
			return false;
		}
		return BooleanUtils.toBoolean(val.toString()) || "1".equals(val.toString());
	}

	/**
	 * 转换为字节数组
	 * @return
	 */
	public static String toString(byte[] bytes) {
		try {
			return new String(bytes, CHARSET_NAME);
		} catch (UnsupportedEncodingException e) {
			return EMPTY;
		}
	}

	/**
	 * 如果对象为空，则使用defaultVal值 see: ObjectUtils.toString(obj, defaultVal)
	 * 
	 * @param obj
	 * @param defaultVal
	 * @return
	 */
	public static String toString(final Object obj, final String defaultVal) {
		return obj == null ? defaultVal : obj.toString();
	}

	/**
	 * 是否包含字符串
	 * 
	 * @param str
	 *            验证字符串
	 * @param strs
	 *            字符串组
	 * @return 包含返回true
	 */
	public static boolean inString(String str, String... strs) {
		if (str != null) {
			for (String s : strs) {
				if (str.equals(trim(s))) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * 替换掉HTML标签方法
	 */
	public static String replaceHtml(String html) {
		if (isBlank(html)) {
			return "";
		}
		String regEx = "<.+?>";
		Pattern p = Pattern.compile(regEx);
		Matcher m = p.matcher(html);
		String s = m.replaceAll("");
		return s;
	}

	/**
	 * 替换为手机识别的HTML，去掉样式及属性，保留回车。
	 * 
	 * @param html
	 * @return
	 */
	public static String replaceMobileHtml(String html) {
		if (html == null) {
			return "";
		}
		return html.replaceAll("<([a-z]+?)\\s+?.*?>", "<$1>");
	}

	/**
	 * 替换为手机识别的HTML，去掉样式及属性，保留回车。
	 * 
	 * @param txt
	 * @return
	 */
	public static String toHtml(String txt) {
		if (txt == null) {
			return "";
		}
		return replace(replace(Encodes.escapeHtml(txt), "\n", "<br/>"), "\t", "&nbsp; &nbsp; ");
	}

	/**
	 * 缩略字符串（不区分中英文字符）
	 * 
	 * @param str
	 *            目标字符串
	 * @param length
	 *            截取长度
	 * @return
	 */
	public static String abbr(String str, int length) {
		if (str == null) {
			return "";
		}
		try {
			StringBuilder sb = new StringBuilder();
			int currentLength = 0;
			for (char c : replaceHtml(StringEscapeUtils.unescapeHtml4(str)).toCharArray()) {
				currentLength += String.valueOf(c).getBytes("GBK").length;
				if (currentLength <= length - 3) {
					sb.append(c);
				} else {
					sb.append("...");
					break;
				}
			}
			return sb.toString();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return "";
	}

	public static String abbr2(String param, int length) {
		if (param == null) {
			return "";
		}
		StringBuffer result = new StringBuffer();
		int n = 0;
		char temp;
		boolean isCode = false; // 是不是HTML代码
		boolean isHTML = false; // 是不是HTML特殊字符,如&nbsp;
		for (int i = 0; i < param.length(); i++) {
			temp = param.charAt(i);
			if (temp == '<') {
				isCode = true;
			} else if (temp == '&') {
				isHTML = true;
			} else if (temp == '>' && isCode) {
				n = n - 1;
				isCode = false;
			} else if (temp == ';' && isHTML) {
				isHTML = false;
			}
			try {
				if (!isCode && !isHTML) {
					n += String.valueOf(temp).getBytes("GBK").length;
				}
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}

			if (n <= length - 3) {
				result.append(temp);
			} else {
				result.append("...");
				break;
			}
		}
		// 取出截取字符串中的HTML标记
		String temp_result = result.toString().replaceAll("(>)[^<>]*(<?)", "$1$2");
		// 去掉不需要结素标记的HTML标记
		temp_result = temp_result.replaceAll(
				"</?(AREA|BASE|BASEFONT|BODY|BR|COL|COLGROUP|DD|DT|FRAME|HEAD|HR|HTML|IMG|INPUT|ISINDEX|LI|LINK|META|OPTION|P|PARAM|TBODY|TD|TFOOT|TH|THEAD|TR|area|base|basefont|body|br|col|colgroup|dd|dt|frame|head|hr|html|img|input|isindex|li|link|meta|option|p|param|tbody|td|tfoot|th|thead|tr)[^<>]*/?>",
				"");
		// 去掉成对的HTML标记
		temp_result = temp_result.replaceAll("<([a-zA-Z]+)[^<>]*>(.*?)</\\1>", "$2");
		// 用正则表达式取出标记
		Pattern p = Pattern.compile("<([a-zA-Z]+)[^<>]*>");
		Matcher m = p.matcher(temp_result);
		List<String> endHTML = Lists.newArrayList();
		while (m.find()) {
			endHTML.add(m.group(1));
		}
		// 补全不成对的HTML标记
		for (int i = endHTML.size() - 1; i >= 0; i--) {
			result.append("</");
			result.append(endHTML.get(i));
			result.append(">");
		}
		return result.toString();
	}

	/**
	 * 转换为Double类型
	 */
	public static Double toDouble(Object val) {
		if (val == null) {
			return 0D;
		}
		try {
			return Double.valueOf(trim(val.toString()));
		} catch (Exception e) {
			return 0D;
		}
	}

	/**
	 * 转换为Float类型
	 */
	public static Float toFloat(Object val) {
		return toDouble(val).floatValue();
	}

	/**
	 * 转换为Long类型
	 */
	public static Long toLong(Object val) {
		return toDouble(val).longValue();
	}

	/**
	 * 转换为Integer类型
	 */
	public static Integer toInteger(Object val) {
		return toLong(val).intValue();
	}

	/**
	 * 获得i18n字符串
	 */
	public static String getMessage(String code, Object[] args) {
		LocaleResolver localLocaleResolver = (LocaleResolver) SpringContextHolder.getBean(LocaleResolver.class);
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
				.getRequest();
		Locale localLocale = localLocaleResolver.resolveLocale(request);
		return SpringContextHolder.getApplicationContext().getMessage(code, args, localLocale);
	}

	/**
	 * 获得用户远程地址
	 */
	public static String getRemoteAddr(HttpServletRequest request) {
		String remoteAddr = request.getHeader("X-Real-IP");
		if (isNotBlank(remoteAddr)) {
			remoteAddr = request.getHeader("X-Forwarded-For");
		} else if (isNotBlank(remoteAddr)) {
			remoteAddr = request.getHeader("Proxy-Client-IP");
		} else if (isNotBlank(remoteAddr)) {
			remoteAddr = request.getHeader("WL-Proxy-Client-IP");
		}
		return remoteAddr != null ? remoteAddr : request.getRemoteAddr();
	}

	/**
	 * 驼峰命名法工具
	 * 
	 * @return toCamelCase("hello_world") == "helloWorld"
	 *         toCapitalizeCamelCase("hello_world") == "HelloWorld"
	 *         toUnderScoreCase("helloWorld") = "hello_world"
	 */
	public static String toCamelCase(String s) {
		if (s == null) {
			return null;
		}

		s = s.toLowerCase();

		StringBuilder sb = new StringBuilder(s.length());
		boolean upperCase = false;
		for (int i = 0; i < s.length(); i++) {
			char c = s.charAt(i);

			if (c == SEPARATOR) {
				upperCase = true;
			} else if (upperCase) {
				sb.append(Character.toUpperCase(c));
				upperCase = false;
			} else {
				sb.append(c);
			}
		}

		return sb.toString();
	}

	/**
	 * 驼峰命名法工具
	 * 
	 * @return toCamelCase("hello_world") == "helloWorld"
	 *         toCapitalizeCamelCase("hello_world") == "HelloWorld"
	 *         toUnderScoreCase("helloWorld") = "hello_world"
	 */
	public static String toCapitalizeCamelCase(String s) {
		if (s == null) {
			return null;
		}
		s = toCamelCase(s);
		return s.substring(0, 1).toUpperCase() + s.substring(1);
	}

	/**
	 * 驼峰命名法工具
	 * 
	 * @return toCamelCase("hello_world") == "helloWorld"
	 *         toCapitalizeCamelCase("hello_world") == "HelloWorld"
	 *         toUnderScoreCase("helloWorld") = "hello_world"
	 */
	public static String toUnderScoreCase(String s) {
		if (s == null) {
			return null;
		}

		StringBuilder sb = new StringBuilder();
		boolean upperCase = false;
		for (int i = 0; i < s.length(); i++) {
			char c = s.charAt(i);

			boolean nextUpperCase = true;

			if (i < (s.length() - 1)) {
				nextUpperCase = Character.isUpperCase(s.charAt(i + 1));
			}

			if ((i > 0) && Character.isUpperCase(c)) {
				if (!upperCase || !nextUpperCase) {
					sb.append(SEPARATOR);
				}
				upperCase = true;
			} else {
				upperCase = false;
			}

			sb.append(Character.toLowerCase(c));
		}

		return sb.toString();
	}

	/**
	 * 转换为JS获取对象值，生成三目运算返回结果
	 * 
	 * @param objectString
	 *            对象串 例如：row.user.id
	 *            返回：!row?'':!row.user?'':!row.user.id?'':row.user.id
	 */
	public static String jsGetVal(String objectString) {
		StringBuilder result = new StringBuilder();
		StringBuilder val = new StringBuilder();
		String[] vals = split(objectString, ".");
		for (int i = 0; i < vals.length; i++) {
			val.append("." + vals[i]);
			result.append("!" + (val.substring(1)) + "?'':");
		}
		result.append(val.substring(1));
		return result.toString();
	}
	/**
	 * 把一个id1,id2,id3格式的字符串改成('id1','id2','id3')
	 * @param ids
	 * @return
	 */
	public static String getWhereByIds(String ids){
		if(StringUtils.isNotEmpty(ids)){
			return getWhereByIds(ids.split(","));
		}
		return null;
	}
	public static String getWhereByIds(String[] idArr){
		if(idArr != null && idArr.length >= 1){
			StringBuffer whereSb = new StringBuffer();
			for(int i=0;i<idArr.length;i++){
				if(whereSb.length() >= 1){
					whereSb.append(",");
				}
				whereSb.append("'"+idArr[i]+"'");
			}
			return "("+whereSb.toString()+")";
		}
		return null;
	}
	/**
	 * 得到订单编号{[当前时间/用户ID后4位数字结果从头取8位数字]+[4位随机数]+[用户ID后4位数字]}共16位
	 * @return
	 */
	public static synchronized String getIndentNumber(String userId,int num){
		String str = userId;
		str=str.trim();
		userId="";
		if(str != null && !"".equals(str)){
			for(int i=0;i<str.length();i++){
				if(str.charAt(i)>=48 && str.charAt(i)<=57){
					userId+=str.charAt(i);
				}
			}
		}
		if(userId.length()>=4){
			userId = userId.substring((userId.length() - 4));
		}else{
			for(int i=0;i<(4 - userId.length());i++){
				userId = getRadomNumber(1)+userId;
			}
		}
		int idNum = Integer.parseInt(userId);
		if(idNum < 1){
			userId = getRadomNumber(4);
		}
		String number = ((Long.parseLong(DateUtils.formatDateTime(new Date(), "yyMMddHHmmss"))/Integer.parseInt(userId))+"").substring(0,8);
		number = number+getRadomNumber(4-(num+"").length())+num+userId;
		return number;
	}
	
	/**
	 * 同步得到由时间生成的编号
	 * @return
	 */
	private static int number = 0;
	public static synchronized String getDateNumber(){
		if(number==9999){
			number = 0;
		}
		number++;
		return DateUtils.formatDateTime(new Date(), "yyMMdd")+getRadomNumber(4-(number+"").length())+number;
	}
	
	/**
	 * 将字符串中省，市，区，县以,号分割
	 * yh
	 * 2017年9月27日
	 * @param addr 四川省成都市武侯区仪陇县
	 * @return 四川省,成都市,武侯区,仪陇县
	 */
	public static  String[] getFormatAddr(String addr){
		int indexOfProvince = addr.indexOf("省");
		int indexOfCity = addr.indexOf("市");
		int indexOfDistrict = addr.indexOf("区");
		int indexOfCounty = addr.indexOf("县");
		String province = "";
		if(indexOfProvince != -1) {
			province = addr.substring(0, indexOfProvince+1);
		}
		String city = "";
		if(indexOfCity != -1) {
			city = addr.substring(indexOfProvince+1, indexOfCity+1);
		}
		String district = "";
		if(indexOfDistrict != -1) {
			district = addr.substring(indexOfCity+1, indexOfDistrict+1);
		}
		String county = "";
		if(indexOfCounty != -1) {
			county = addr.substring(indexOfDistrict+1, indexOfCounty+1);
		}
	
		String[] addrs={province,city,district,county};
		return addrs;
	}

	/**
	 * 判断字符串的编码类型
	 * @param str
	 * @return
	 */
	public static String getEncoding(String str) {
		String encode = "GB2312";
		try {
			if (str.equals(new String(str.getBytes(encode), encode))) {
				String s = encode;
				return s;
			}
		} catch (Exception exception) {
		}
		encode = "ISO-8859-1";
		try {
			if (str.equals(new String(str.getBytes(encode), encode))) {
				String s1 = encode;
				return s1;
			}
		} catch (Exception exception1) {
		}
		encode = "UTF-8";
		try {
			if (str.equals(new String(str.getBytes(encode), encode))) {
				String s2 = encode;
				return s2;
			}
		} catch (Exception exception2) {
		}
		encode = "GBK";
		try {
			if (str.equals(new String(str.getBytes(encode), encode))) {
				String s3 = encode;
				return s3;
			}
		} catch (Exception exception3) {
		}
		return "";
	}

	/**
	 * Z字符串编码转换
	 * @param encode
	 * @param par
	 * @return
	 */
	public static  String encoding(String encode,String par) {
		try {
			par = new String(par.getBytes(StringUtils.getEncoding(par)) , encode);
		}catch (Exception e){
			e.printStackTrace();
			return  par;
		}
		return par;
	}
}
