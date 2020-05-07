package com.qyh.demo.base.util;

import com.qyh.demo.entity.SysUser;
import com.qyh.demo.enums.JedisIndex;
import com.github.pagehelper.util.StringUtil;
import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;
import org.apache.commons.lang.StringUtils;
import org.wltea.analyzer.core.IKSegmenter;
import org.wltea.analyzer.core.Lexeme;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;



public class Kit {

	public static CacheManager cacheManager = CacheManager.getInstance();
	/*
	 * 测试
	 */
	public static void main(String args[]) throws IOException, InterruptedException {
		/*int i = 0;
		Set<String> set = new HashSet<>();
		while (true){
			i++;
			String str = Kit.getIndentNumber("36543ab153594215b21eeba03fdeaa96",(int)(Math.random()*10000))+i;
			System.out.println(str);
			set.add(str);
			System.out.println(i);
			System.out.println(set.size());

			System.out.println("------");

		}*/
//		System.out.println(Kit.getMD5Str("devdev"));
//		while (true) {
//			i++;
//			System.out.println((int)(Math.random()*10000));
//			if (i==1000) {
//				break;
//			}
//		}


	}

	public static final String PROJECT_PATH = "http://120.78.172.166:8088/attorney";

	// 判断字符串是否只含有数字或字符
	public static boolean isNumAndStr(String input) {
		if (input!=null && input.matches("[0-9A-Za-z_]*")) {
			return true;
		} else {
			return false;
		}
	}

	// 判断字符串是否只含有数字
	public static boolean isNum(String input) {
		if (input != null && input.trim().length() != 0
				&& input.matches("[0-9]*")) {
			return true;
		} else {
			return false;
		}
	}


	// 判断字符串是否整数
	public static boolean isInt(String input) {
		if (input!=null && input.matches("^[+-]?[\\d]+$")) {
			return true;
		} else {
			return false;
		}
	}

	// 判断字符串是否小数
	public static boolean isFloat(String input) {
		if (input!=null && input.matches("^[+-]?[\\d]+\\.[\\d]+$")) {
			return true;
		} else {
			return false;
		}
	}


	// 得到对象的字符值
	public static String getObjStr(Object o) {
		return (o != null ? o.toString() : "");
	}

	// 得到对象的整形值，转化失败返回 0
	public static Integer getObjInteger(Object o) {
		Integer oi = new Integer(0);
		if (o instanceof Integer) {
			oi = (Integer) o;
		}
		return oi;
	}

	// 得到对象的长整形值，转化失败返回 0
	public static Long getObjLong(Object o) {
		Long ol = new Long(0);
		if (o instanceof Long) {
			ol = (Long) o;
		}
		return ol;
	}

	// 得到对象的浮点形式值，转化失败返回 0.0
	public static Double getObjDouble(Object o) {
		Double od = new Double(0.0);
		if (o instanceof Double) {
			od = (Double) o;
		}else if(o instanceof BigDecimal){
			od=((BigDecimal) o).doubleValue();
		}
		return od;
	}

	// 得到对象的日期值，转化失败返回null
	public static Date getObjDate(Object o) {
		Date date = null;
		if (o instanceof Date) {
			date = (Date) o;
		}
		return date;
	}

	/*
	 * 时间处理工具函数
	 */
	// 得到当前日期的开始时间
	public static Date getCurrentDateTimeStart() {
		Calendar cl = Calendar.getInstance();
		cl.set(cl.get(cl.YEAR), cl.get(cl.MONTH), cl.get(cl.DAY_OF_MONTH), 0,
				0, 0);
		return cl.getTime();
	}

	// 得到当前日期的结束时间
	public static Date getCurrentDateTimeEnd() {
		Calendar cl = Calendar.getInstance();
		cl.set(cl.get(cl.YEAR), cl.get(cl.MONTH), cl.get(cl.DAY_OF_MONTH), 23,
				59, 59);
		return cl.getTime();
	}

	// 得到当前周开始时间
	public static Date getCurrentWeekStartDate() {
		Calendar cl = Calendar.getInstance();
		int day = cl.get(Calendar.DAY_OF_WEEK);
		if (day == 1) {
			day = -6;
		} else {
			day = -1 * (day - 2);
		}
		cl.add(Calendar.DAY_OF_WEEK, day);
		Date firstDay = cl.getTime();
		return getDateTimeStart(firstDay);
	}

	// 得到当前周结束时间

	public static Date getCurrentWeekEndDate() {
		Calendar cl = Calendar.getInstance();
		int day = cl.get(Calendar.DAY_OF_WEEK);
		if (day == 1) {
			day = 0;
		} else {
			day = 8 - day;
		}
		cl.add(Calendar.DAY_OF_WEEK, day);
		Date endDay = cl.getTime();
		return getDateTimeEnd(endDay);
	}

	// 得到当前月开始时间

	public static Date getCurrentMonthStartDate() {
		Calendar cl = Calendar.getInstance();
		cl.set(Calendar.DAY_OF_MONTH, cl
				.getActualMinimum(Calendar.DAY_OF_MONTH));
		return getDateTimeStart(cl.getTime());
	}

	// 得到当前月结束时间

	public static Date getCurrentMonthEndDate() {
		Calendar cl = Calendar.getInstance();
		cl.set(Calendar.DAY_OF_MONTH, cl
				.getActualMaximum(Calendar.DAY_OF_MONTH));
		return getDateTimeEnd(cl.getTime());
	}

	// 得到指定日期的开始时间
	public static Date getDateTimeStart(Date date) {
		Calendar cl = Calendar.getInstance();
		cl.setTime(date);
		cl.set(cl.get(cl.YEAR), cl.get(cl.MONTH), cl.get(cl.DAY_OF_MONTH), 0,
				0, 0);
		return cl.getTime();
	}

	// 得到指定下一个月的零点
	public static Date getNextMonth(Date date) {
		Calendar cl = Calendar.getInstance();
		cl.setTime(date);
		cl.set(cl.get(cl.YEAR), cl.get(cl.MONTH) + 1 , cl.get(cl.DAY_OF_MONTH), 0,0, 0);
		return cl.getTime();
	}

	// 得到指定日期的结束时间
	public static Date getDateTimeEnd(Date date) {
		Calendar cl = Calendar.getInstance();
		cl.setTime(date);
		cl.set(cl.get(cl.YEAR), cl.get(cl.MONTH), cl.get(cl.DAY_OF_MONTH), 23,
				59, 59);
		return cl.getTime();
	}

	// 得到两个日期的间隔月
	public static int getBetweenMonth(Date s, Date e) {
		if (s.after(e)) {
			Date t = s;
			s = e;
			e = t;
		}
		Calendar start = Calendar.getInstance();
		start.setTime(s);
		Calendar end = Calendar.getInstance();
		end.setTime(e);
		Calendar temp = Calendar.getInstance();
		temp.setTime(e);
		temp.add(Calendar.DATE, 1);

		int y = end.get(Calendar.YEAR) - start.get(Calendar.YEAR);
		int m = end.get(Calendar.MONTH) - start.get(Calendar.MONTH);

		if ((start.get(Calendar.DATE) == 1) && (temp.get(Calendar.DATE) == 1)) {// 前后都不破月
			return y * 12 + m + 1;
		} else if ((start.get(Calendar.DATE) != 1)
				&& (temp.get(Calendar.DATE) == 1)) {// 前破月后不破月
			return y * 12 + m;
		} else if ((start.get(Calendar.DATE) == 1)
				&& (temp.get(Calendar.DATE) != 1)) {// 前不破月后破月
			return y * 12 + m;
		} else {// 前破月后破月
			return (y * 12 + m - 1) < 0 ? 0 : (y * 12 + m - 1);
		}
	}

	// 得到两个日期的间隔天
	public static int getBetweenDay(Date s, Date e) {
		return (new Long((e.getTime() - s.getTime()) / (1000 * 3600 * 24)))
				.intValue();
	}
	/**
	 * 得到两个日期的间秒
	 * @param oldDate 原来时间
	 * @param nowDate 现在时间
	 * @return
	 * @throws Exception
	 */
	public static int getBetweenSecond(String oldDate,String nowDate) throws Exception {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date old = sdf.parse(oldDate);
		Date now = sdf.parse(nowDate);

		return (new Long((now.getTime() - old.getTime()) / 1000)).intValue();
	}

	// 解析日期时间,解析失败返回null
	// @format 常用格式1.yyyy-MM-dd HH:mm:ss 2.yyyy-MM-dd
	public static Date parseDateTimeStr(String dateTimeStr, String format) {
		dateTimeStr=dateTimeStr.replace('.', '-');
		dateTimeStr=dateTimeStr.replace('/', '-');
		SimpleDateFormat sdf = new SimpleDateFormat();
		sdf.applyPattern(format);
		Date date = null;
		try {
			date = sdf.parse(dateTimeStr);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return date;
	}

	/**
	 * 格式化日期时间字符串
	 * @param date 日期
	 * @param @format 常用格式1.yyyy-MM-dd HH:mm:ss 2.yyyy-MM-dd
	 */
	public static String formatDateTime(Date date, String format) {
		if(date!=null){
			SimpleDateFormat sdf = new SimpleDateFormat();
			sdf.applyPattern(format);
			return sdf.format(date);
		}else{
			return "";
		}
	}

	// 得到随机数串
	// @length随机数的长度
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

	// 得到32位UUID
	public static String get32UUID() {
		UUID uuid = UUID.randomUUID();
		return uuid.toString().replace("-", "").substring(0, 31);
	}

	// 得到36位UUID
	public static String get36UUID() {
		UUID uuid = UUID.randomUUID();
		return uuid.toString();
	}

	// 得到MD5摘要串
	public static String getMD5Str(String s) {
		// 用作十六进制的数组.
		byte hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
				'a', 'b', 'c', 'd', 'e', 'f' };
		try {
			MessageDigest mdTemp = MessageDigest.getInstance("MD5"
					.toUpperCase());// 使用MD5加密
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
	 * @param s 待加密字符串
	 * @param charset 编码格式
	 * @return
	 */
	public static String getMD5Str(String s,String charset) {
		// 用作十六进制的数组.
		byte hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
				'A', 'B', 'C', 'D', 'E', 'F' };
		try {
			MessageDigest mdTemp = MessageDigest.getInstance("MD5"
					.toUpperCase());// 使用MD5加密
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

	//判断是否是UTF-8编码
	public static boolean isutf(String s) {
		try{
			if(s!=null && s.length()!=0){
				String tmp=new String(s.getBytes("utf-8"),"utf-8");
				return s.equals(tmp);
			}
		}catch(Exception ex){
			ex.printStackTrace();
			return false;
		}
		return true;
	}
	//判断是否是ISO-8859-1编码
	public static boolean isISO8859(String s) {
		try{
			if(s!=null && s.length()!=0){
				String tmp=new String(s.getBytes("ISO-8859-1"),"ISO-8859-1");
				return s.equals(tmp);
			}
		}catch(Exception ex){
			ex.printStackTrace();
			return false;
		}
		return true;
	}


	/**
	 * 根据生日获取年龄
	 * @param birthDay
	 * @return
	 * @throws Exception
	 */
	public static String getAge(Date birthDay) throws Exception {
		Calendar cal = Calendar.getInstance();

		if (cal.before(birthDay)) {
			throw new IllegalArgumentException(
					"The birthDay is before Now.It's unbelievable!");
		}

		int yearNow = cal.get(Calendar.YEAR);
		int monthNow = cal.get(Calendar.MONTH)+1;
		int dayOfMonthNow = cal.get(Calendar.DAY_OF_MONTH);

		cal.setTime(birthDay);
		int yearBirth = cal.get(Calendar.YEAR);
		int monthBirth = cal.get(Calendar.MONTH);
		int dayOfMonthBirth = cal.get(Calendar.DAY_OF_MONTH);

		int age = yearNow - yearBirth;

		if (monthNow <= monthBirth) {
			if (monthNow == monthBirth) {
				//monthNow==monthBirth
				if (dayOfMonthNow < dayOfMonthBirth) {
					age--;
				}
			} else {
				//monthNow>monthBirth
				age--;
			}
		}
		return age +"";
	}
	/**
	 * 得到指定时间后多少毫秒后的新时间
	 * @param time
	 * @param millisecond
	 * @return
	 */
	public static String getDateAfterWhatMS(Date time,long millisecond,String format){
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		return sdf.format(time.getTime()+millisecond);
	}
	/**
	 * 得到当前登录人
	 * @param request
	 * @return
	 */
	/*public static SysUser getLoginUser(HttpServletRequest request){
		return (SysUser) request.getSession().getAttribute("_loginUser");
	}*/

	/**
	 * 把一个id1,id2,id3格式的字符串改成('id1','id2','id3')
	 * @param ids
	 * @return
	 */
	public static String getWhereByIds(String ids){
		if(StringUtils.isNotEmpty(ids)){
			return getWhereByIds(ids.split(",")).trim();
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
			return "("+whereSb.toString().trim()+")";
		}
		return null;
	}
	/**
	 * 得到订单编号{[当前时间/用户ID后4位数字结果从头取8位数字]+[4位随机数]+[用户ID后4位数字]}共16位
	 * @param
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
				userId = Kit.getRadomNumber(1)+userId;
			}
		}
		int idNum = Integer.parseInt(userId);
		if(idNum < 1){
			userId = Kit.getRadomNumber(4);
		}
		String number = ((Long.parseLong(formatDateTime(new Date(), "yyMMddHHmmss"))/Integer.parseInt(userId))+"").substring(0,8);
		number = number+Kit.getRadomNumber(4-(num+"").length())+num+userId;
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
		return Kit.formatDateTime(new Date(), "yyMMdd")+Kit.getRadomNumber(4-(number+"").length())+number;
	}

	/**
	 * 得到注册码
	 * @param number
	 * @return
	 */
	public static String getRegistrationCode(Integer number){
		return SysConvertUtil.toSerialCode(number);
	}

	/**
	 * 去除指定的元素[字符串]
	 * @param source	准备被去除的数据
	 * @param filter	想要去的数据
	 */
	public static String wipeOffFilterString(String source,String filter){
		Object[] result = wipeOffFilter(source != null?source.split(","):new String[]{},
				filter != null?filter.split(","):new String[]{});
		return StringUtils.join(result,",");
	}

	/**
	 * 去除指定的元素[数组]
	 * @param source	准备被去除的数据
	 * @param filter	想要去的数据
	 */
	public static Object[] wipeOffFilter(Object[] source,Object[] filter){
		List<Object> sourceList = new ArrayList<Object>(Arrays.asList(source));
		List<Object> filterList = new ArrayList<Object>(Arrays.asList(filter));

		Iterator<Object> it =  sourceList.iterator();
		while(it.hasNext()){
			Object obj = it.next();
			if(filterList.contains(obj) || StringUtils.isEmpty(obj.toString().trim())){
				it.remove();
			}
		}

		return sourceList.toArray();
	}

	/**
	 * 返回JSON对象
	 * @param object	需要转换的对象
	 * @param excludes	需忽略字段
	 * @return
	 */
	public static Object returnJsonObj(Object object,String[] excludes){
		JSONObject jsonObj = null;

		JsonConfig jsonConfig = new JsonConfig();  //建立配置文件
		jsonConfig.setIgnoreDefaultExcludes(false);  //设置默认忽略
		if(excludes != null && excludes.length >= 1){
			jsonConfig.setExcludes(excludes);//将所需忽略字段加到数组中
		}
		jsonConfig.registerJsonValueProcessor(Date.class, new JsonDateValueProcessor("yyyy-MM-dd HH:mm:ss"));
		jsonConfig.registerJsonValueProcessor(java.sql.Timestamp.class, new JsonDateValueProcessor("yyyy-MM-dd HH:mm:ss"));

		jsonObj = JSONObject.fromObject(object,jsonConfig);

		System.err.println("返回格式："+jsonObj.toString());
		return jsonObj;
	}

	/**
	 * 生成6位随机数
	 * @return
	 */
	public static String getMsgCode(){
		int code = (int)((Math.random()*9+1)*100000);
		return String.valueOf(code);
	}

	/**
	 * 获取缓存中的元素
	 * hx 2017年9月6日 下午6:44:14
	 * @param cacheName
	 * @param elementName
	 * @return
	 */
	public static Element getElement(String cacheName,String elementName){
		Cache cache = null;
		if(cacheManager.getCache(cacheName)==null){
			cacheManager.addCache(cacheName);
		}
		cache = cacheManager.getCache(cacheName);
		/*List<String> keys = cache.getKeys();
		for(String s : keys) {
			System.out.println("elementValue=="+cache.get(s));
		}*/
		return cache.get(elementName);
	}

	/**
	 * 添加缓存
	 * hx 2017年9月6日 下午6:45:31
	 * @param cacheName
	 * @param elementName
	 * @param elementValue
	 */
	public static void setElement(String cacheName,String elementName,Object elementValue){
		Cache cache = null;
		if(cacheManager.getCache(cacheName)==null){
			cacheManager.addCache(cacheName);
		}
		cache = cacheManager.getCache(cacheName);
		cache.put(new Element(elementName,elementValue));
	}
	/**
	 * 添加缓存
	 * hx 2017年9月6日 下午6:45:31
	 * @param cacheName
	 * @param elementName
	 * @param elementValue
	 */
	public static void setElement(String cacheName,String elementName,Object elementValue,Integer time){
		Cache cache = null;
		if(cacheManager.getCache(cacheName)==null){
			cacheManager.addCache(cacheName);
		}
		cache = cacheManager.getCache(cacheName);
		Element element = new Element(elementName,elementValue);
		if(time != null) {
			element.setTimeToLive(time);
			element.setTimeToIdle(time);
		}
		cache.put(element);
	}

	/**
	 * 删除缓存元素
	 * hx 2018年3月29日 下午4:30:57
	 * @param objectKey 元素key
	 * @param cacheName 缓存名称
	 */
	public static void removeElement(String objectKey,String cacheName){
		Cache cache = cacheManager.getCache(cacheName);
		cache.remove(objectKey);
	}
	/**
	 *
	 *(特殊字符替换)
	 * @param
	 * @return String    返回类型
	 * @author xsw
	 * @2016-12-4下午03:10:03
	 */
	public static String htmlReplace(String str){
		str = str.replace("&ldquo;", "“");
		str = str.replace("&rdquo;", "”");
		str = str.replace("&nbsp;", " ");
		str = str.replace("&amp;", "&");
		str = str.replace("&#39;", "'");
		str = str.replace("&rsquo;", "’");
		str = str.replace("&mdash;", "—");
		str = str.replace("&ndash;", "–");
		return str;
	}

	/**
	 * 获取当前登录用户
	 * hx 2018年4月2日 上午11:56:41
	 * @param request
	 * @return
	 */
	public static SysUser getCurLoginUser(HttpServletRequest request){
		String token = request.getHeader("token");
		/*String requestURI = request.getRequestURI();
		Element e = null;
		if(requestURI.contains("auth")){//app端 访问接口
			e = getElement("tokenCache", token);
		}else{//web 端接口访问
			if(requestURI.contains("sys")) {
				e = getElement("sysTokenCache", token);

			}else{//app端 登录与不登录都可以访问的接口
				e = getElement("tokenCache", token);
			}
		}
		if(e==null){
			return null;
		}
		return (SysUser) e.getObjectValue();*/
		if(StringUtils.isEmpty(token)){
			return null;
		}

		Object obj = JedisUtil.getJedisUtil().getObject(token,JedisIndex.USER);

		if(obj==null){
			return null;
		}
		return (SysUser) obj;
	}

	/**
	 * 获取后台当前登录用户
	 * @param request
	 * @return
	 */
    /*public static SysUser getSysLoginUser(HttpServletRequest request){
    	String token = request.getHeader("token");
    	Element e = getElement("sysUserIdCache", token);
    	if(e==null){
    		return null;
    	}
    	return (SysUser) e.getObjectValue();
    }*/

	/**
	 * 获取请求参数字符串
	 * @author hx
	 * 2018年6月7日
	 * @param map
	 * @return
	 */
	public static String getParamStr(Map<String, String[]> map){
		StringBuilder sb = new StringBuilder();
		for (Map.Entry<String, String[]> e : map.entrySet()) {
			sb.append(e.getKey()).append("=");
			String[] value = e.getValue();
			if (value != null && value.length == 1) {
				sb.append(value[0]).append("\t");
			} else {
				sb.append(Arrays.toString(value)).append("\t");
			}
		}
		return sb.toString();
	}




	/**
	 * 从网络Url中下载文件
	 * @param urlStr
	 * @param fileName
	 * @param savePath
	 * @throws IOException
	 */
	public static void  downLoadFromUrl(String urlStr,String fileName,String savePath) throws IOException{
		URL url = new URL(urlStr);
		HttpURLConnection conn = (HttpURLConnection)url.openConnection();
		//设置超时间为3秒
		conn.setConnectTimeout(3*1000);
		//防止屏蔽程序抓取而返回403错误
		conn.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");

		//得到输入流
		InputStream inputStream = conn.getInputStream();
		//获取自己数组
		byte[] getData = readInputStream(inputStream);

		//文件保存位置
		File saveDir = new File(savePath);
		if(!saveDir.exists()){
			saveDir.mkdir();
		}
		File file = new File(saveDir+File.separator+fileName);
		FileOutputStream fos = new FileOutputStream(file);
		fos.write(getData);
		if(fos!=null){
			fos.close();
		}
		if(inputStream!=null){
			inputStream.close();
		}


		System.out.println("info:"+url+" download success");

	}



	/**
	 * 从输入流中获取字节数组
	 * @param inputStream
	 * @return
	 * @throws IOException
	 */
	public static  byte[] readInputStream(InputStream inputStream) throws IOException {
		byte[] buffer = new byte[1024];
		int len = 0;
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		while((len = inputStream.read(buffer)) != -1) {
			bos.write(buffer, 0, len);
		}
		bos.close();
		return bos.toByteArray();
	}

	/**
	 * 验证电话号码是否合法
	 * yh
	 * @param phone
	 * @return
	 */
	public static boolean isPhone(String phone) {
		String regex = "^((13[0-9])|(14[5,7,9])|(15([0-3]|[5-9]))|(166)|(17[0,1,3,5,6,7,8])|(18[0-9])|(19[8|9]))\\d{8}$";
		if (phone.length() != 11) {
			return false;
		} else {
			Pattern p = Pattern.compile(regex);
			Matcher m = p.matcher(phone);
			return m.matches();
		}
	}

	/**
	 * 目标日期加  整数天后的  日期
	 * @param num
	 * @param newDate
	 * @return 增加天数后的日期
	 * @throws Exception
	 * 2018年4月19日
	 * @author yh
	 */
	public static Date plusDay(int num,Date newDate) throws Exception{
		Calendar ca = Calendar.getInstance();
		ca.setTime(newDate);
		ca.add(Calendar.DATE, num);// num为增加的天数，可以改变的
		newDate = ca.getTime();
		return newDate;
	}

	/**
	 * 目标日期减去天数  后的日期
	 * @param num
	 * @param newDate
	 * @return
	 * @throws Exception
	 * 2018年8月20日
	 * @author yh
	 */
	public static Date subtractDay(int num,Date newDate) throws Exception{
		Calendar ca = Calendar.getInstance();
		ca.setTime(newDate);
		ca.set(Calendar.DAY_OF_YEAR, ca.get(Calendar.DAY_OF_YEAR) - num);
		newDate = ca.getTime();
		return newDate;
	}

	/**
	 *
	 * @param str
	 * @return "'%"+str+"%'"
	 * @author shugl
	 * @date 2018年8月17日
	 */
	public static String getLikeStr(String str){
		if (StringUtil.isEmpty(str)) {
			return null;
		}
		str = "%"+str+"%";
		return str;
	}

	/**
	 * @description IK分词器，分词数据
	 * @param    text 分词字符串
	 * @return   返回分词后 数据集合
	 * @date     2019年02月20日 11:23:10
	 * @author   cloud fly
	 */
	public static List<String> getAnalyzerData(String text) {
		List<String> resultList =  new ArrayList<>();
		//独立Lucene实现
		StringReader re = new StringReader(text);
		IKSegmenter ik = new IKSegmenter(re,true);//分词模式
		Lexeme lex = null;
		try {
			while((lex=ik.next()) != null){
				resultList.add(lex.getLexemeText());
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
		//System.out.println(resultList.toString());
		return resultList;
	}

	/**
	 * 获取当前时间UTC时时间戳
	 * @return  返回的就是UTC时间
	 * @throws Exception
	 * 2018年10月30日
	 * @author cloud fly
	 */
	public static String getUTCTimeStr() throws Exception {
		Calendar cal = Calendar.getInstance();
		return String.valueOf(cal.getTimeInMillis() / 1000);
	}
}
