package com.qyh.demo.base.util;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.util.EntityUtils;

import java.util.Map;
import java.util.Set;

public class HttpsGet {


	public static String doGet(String url, Map<String,String> headerMap){
		String charset = "utf-8";
		HttpClient httpClient = null;
		HttpGet httpGet= null;
		String result = null;
		try {
			httpClient = new SSLClient();
			httpGet = new HttpGet(url);

			if(headerMap!=null&&!headerMap.isEmpty()){
				Set<Map.Entry<String,String>> set = headerMap.entrySet();
				for(Map.Entry<String,String> e:set){
					httpGet.addHeader(e.getKey(), e.getValue());
				}
			}

			HttpResponse response = httpClient.execute(httpGet);
			if(response != null){
				HttpEntity resEntity = response.getEntity();
				if(resEntity != null){
					result = EntityUtils.toString(resEntity,charset);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return result;
	}
}
