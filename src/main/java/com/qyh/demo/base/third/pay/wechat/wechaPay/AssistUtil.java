package com.qyh.demo.base.third.pay.wechat.wechaPay;

import com.qyh.demo.base.util.Kit;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.util.EntityUtils;
import org.dom4j.*;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import java.io.*;
import java.net.URL;
import java.security.KeyStore;
import java.util.*;

/**
 * 辅助工具
 * @author qiuyuehao
 *
 */
public class AssistUtil {
	/** 
     * 除去数组中的空值和签名参数
     * @param sArray 签名参数组
     * @return 去掉空值与签名参数后的新签名参数组
     */
    public static Map<String, String> paraFilter(Map<String, String> sArray) {

        Map<String, String> result = new HashMap<String, String>();

        if (sArray == null || sArray.size() <= 0) {
            return result;
        }

        for (String key : sArray.keySet()) {
            String value = sArray.get(key);
            if (value == null || value.equals("") || key.equalsIgnoreCase("sign")) {
                continue;
            }
            result.put(key, value);
        }

        return result;
    }
   
    /** 
     * 把数组所有元素排序，并按照“参数=参数值”的模式用“&”字符拼接成字符串
     * @param params 需要排序并参与字符拼接的参数组
     * @return 拼接后字符串
     */
    public static String createLinkString(Map<String, String> params) {

        List<String> keys = new ArrayList<String>(params.keySet());
        Collections.sort(keys);

        String prestr = "";

        for (int i = 0; i < keys.size(); i++) {
            String key = keys.get(i);
            String value = params.get(key);

            if (i == keys.size() - 1) {//拼接时，不包括最后一个&字符
                prestr = prestr + key + "=" + value;
            } else {
                prestr = prestr + key + "=" + value + "&";
            }
        }

        return prestr;
    }
    
    /**
     * 生成签名结果
     * @param sPara 要签名的数组
     * @return 签名结果字符串
     */
	public static String buildRequestMysign(Map<String, String> sPara,String key) {
    	String signTemp = createLinkString(paraFilter(sPara)); //把数组所有元素，按照“参数=参数值”的模式用“&”字符拼接成字符串
    	signTemp += "&key="+key;
        return Kit.getMD5Str(signTemp, "UTF-8").toUpperCase();
    }
	
	/**
	 * 把map转化成XML
	 * @param params
	 * @return
	 */
	public static String conversionMapToXml(Map<String, String> params){
		StringBuffer result = new StringBuffer(); 
		result.append("<xml>");
		for(Map.Entry<String, String> entry : params.entrySet()){
			result.append("<"+entry.getKey()+">");
			result.append(entry.getValue());
			result.append("</"+entry.getKey()+">");
		}
		result.append("</xml>");
		return result.toString();
	}
	
	 /**
	  * 通用：获取签名
	  * @param coded	编码
	  * @param parames	待签名参数
	  * @return
	  */
	 public static String getSignCommon(String coded,Map<String,String> parames,String key){  
		SortedMap<String,String> sort=new TreeMap<String,String>(parames);  
		
		StringBuffer sb = new StringBuffer();  
		Set es = sort.entrySet();//所有参与传参的参数按照accsii排序（升序）  
		Iterator it = es.iterator();  
		while(it.hasNext()) {  
		    Map.Entry entry = (Map.Entry)it.next();  
		    String k = (String)entry.getKey();  
		    Object v = entry.getValue();  
		    if(null != v && !"".equals(v)   
		            && !"sign".equals(k) && !"key".equals(k)) {  
		        sb.append(k + "=" + v + "&");  
		    }  
		}  
        sb.append("key=" + key);  
        return Kit.getMD5Str(sb.toString(), "UTF-8").toUpperCase();  
    }  
	 
	 /**
	  * XML转化成Map<String, Object>
	  * @param xmlStr
	  * @return
	 * @throws Exception 
	  */
	 public static Map xmlToMap(String xmlStr) throws Exception {
		 Document doc = DocumentHelper.parseText(xmlStr);  
		 Element rootElement = doc.getRootElement();  
		 Map<String, Object> map = new HashMap<String, Object>();
		 
		 ele2map(map,rootElement);
		  return map;
	 }
 /*** 
  * 核心方法，里面有递归调用 
  *  
  * @param map 
  * @param ele 
  */  
	 static void ele2map(Map map, Element ele) {  
		 // 获得当前节点的子节点  
		 List<Element> elements = ele.elements();  
		 if (elements.size() == 0) {  
			 // 没有子节点说明当前节点是叶子节点，直接取值即可  
			 map.put(ele.getName(), ele.getText());  
		 } else if (elements.size() == 1) {  
			 // 只有一个子节点说明不用考虑list的情况，直接继续递归即可  
			 Map<String, Object> tempMap = new HashMap<String, Object>();  
			 ele2map(tempMap, elements.get(0));  
			 map.put(ele.getName(), tempMap);  
		 } else {  
			 // 多个子节点的话就得考虑list的情况了，比如多个子节点有节点名称相同的  
			 // 构造一个map用来去重  
			 Map<String, Object> tempMap = new HashMap<String, Object>();  
			 for (Element element : elements) {  
				 tempMap.put(element.getName(), null);  
			 }  
			 Set<String> keySet = tempMap.keySet();  
			 for (String string : keySet) {  
				 Namespace namespace = elements.get(0).getNamespace();  
				 List<Element> elements2 = ele.elements(new QName(string,namespace));  
				 // 如果同名的数目大于1则表示要构建list  
				 if (elements2.size() > 1) {  
					 List<Map> list = new ArrayList<Map>();  
					 for (Element element : elements2) {  
						 Map<String, Object> tempMap1 = new HashMap<String, Object>();  
						 ele2map(tempMap1, element);  
						 list.add(tempMap1);  
					 }  
					 map.put(string, list);  
				 } else {  
					 // 同名的数量不大于1则直接递归去  
					 Map<String, Object> tempMap1 = new HashMap<String, Object>();  
					 ele2map(tempMap1, elements2.get(0));  
					 map.put(string, tempMap1.get(string));  
				 }  
			 }  
		 }  
	 } 
	 /**
	  * 向微信发出“统一下单”请求，并返回响应信息
	  * @param requestUrl
	  * @param requestMethod
	  * @param outputStr
	  * @return
	  */
	 public static Map<String, String> httpsRequestToXML(String requestUrl, String requestMethod, String outputStr) {
		  Map<String, String> result = new HashMap<>();
		  try {
		  StringBuffer buffer = AssistUtil.httpsRequest(requestUrl, requestMethod, outputStr);
		  result = AssistUtil.parseXml(buffer.toString());
		  } catch (Exception ce) {
			  System.out.println("访问微信服务异常：");
			  ce.printStackTrace();
		  }
		  return result;
	 }
	/**
	 * 发送请求并返回响应信息
	 * @param requestUrl
	 * @param requestMethod
	 * @param output
	 * @return
	 * @throws Exception
	 */
	public static StringBuffer httpsRequest(String requestUrl, String requestMethod, String output) throws Exception{
		  URL url = new URL(requestUrl);
		  HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
		  connection.setDoOutput(true);
		  connection.setDoInput(true);
		  connection.setUseCaches(false);
		  connection.setRequestMethod(requestMethod);
		  if (null != output) {
			  OutputStream outputStream = connection.getOutputStream();
			  outputStream.write(output.getBytes("UTF-8"));
			  outputStream.close();
		  }
		  // 从输入流读取返回内容
		  InputStream inputStream = connection.getInputStream();
		  InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "utf-8");
		  BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
		  String str = null;
		  StringBuffer buffer = new StringBuffer();
		  while ((str = bufferedReader.readLine()) != null) {
			  buffer.append(str);
		  }
		  bufferedReader.close();
		  inputStreamReader.close();
		  inputStream.close();
		  inputStream = null;
		  connection.disconnect();
		  return buffer;
	} 
	/**
	 * 把xml内容装入Map中并返回
	 * @param xml
	 * @return
	 * @throws Exception
	 */
	public static Map<String, String> parseXml(String xml) throws Exception {
		  Map<String, String> map = new HashMap<String, String>();
		  Document document = DocumentHelper.parseText(xml);
		  Element root = document.getRootElement();
		  List<Element> elementList = root.elements();
		  for (Element e : elementList){
			  map.put(e.getName(), e.getText());
		  }
		  return map;
	 }
	 
    /**
     * 微信退款请求
     * @param url		退款地址
     * @param data		退款参数
     * @param mch_id	商户ID
     * @param path 		项目地址
     * @return
     * @throws Exception
     */
    public static String doRefund(String url,String data,String mch_id,String path) throws Exception {  
        /** 
         * 注意PKCS12证书 是从微信商户平台-》账户设置-》 API安全 中下载的 
         */  
          
        KeyStore keyStore  = KeyStore.getInstance("PKCS12");  
        FileInputStream instream = new FileInputStream(new File(path+"/apiclient_cert.p12"));//P12文件目录  
        try {  
            /** 
             * 此处要改 
             * */  
            keyStore.load(instream, mch_id.toCharArray());//这里写密码..默认是你的MCHID  
        } finally {  
            instream.close();  
        }  
  
        // Trust own CA and all self-signed certs  
        /** 
         * 此处要改 
         * */  
        SSLContext sslcontext = SSLContexts.custom()
                .loadKeyMaterial(keyStore, mch_id.toCharArray())//这里也是写密码的    
                .build();  
        // Allow TLSv1 protocol only  
        SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(  
                sslcontext,  
                new String[] { "TLSv1" },  
                null,  
                SSLConnectionSocketFactory.BROWSER_COMPATIBLE_HOSTNAME_VERIFIER);  
        CloseableHttpClient httpclient = HttpClients.custom()  
                .setSSLSocketFactory(sslsf)  
                .build();  
        try {  
            HttpPost httpost = new HttpPost(url); // 设置响应头信息  
            httpost.addHeader("Connection", "keep-alive");  
            httpost.addHeader("Accept", "*/*");  
            httpost.addHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");  
            httpost.addHeader("Host", "api.mch.weixin.qq.com");  
            httpost.addHeader("X-Requested-With", "XMLHttpRequest");  
            httpost.addHeader("Cache-Control", "max-age=0");  
            httpost.addHeader("User-Agent", "Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 6.0) ");  
            httpost.setEntity(new StringEntity(data, "UTF-8"));
            CloseableHttpResponse response = httpclient.execute(httpost);  
            try {  
                HttpEntity entity = response.getEntity();
  
                String jsonStr = EntityUtils.toString(response.getEntity(), "UTF-8");
                EntityUtils.consume(entity);
               return jsonStr;  
            } finally {  
                response.close();  
            }  
        } finally {  
            httpclient.close();  
        }  
    } 
}
