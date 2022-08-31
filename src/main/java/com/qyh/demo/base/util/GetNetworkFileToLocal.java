package com.qyh.demo.base.util;

import java.io.ByteArrayOutputStream;  
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;  
import java.net.HttpURLConnection;  
import java.net.URL;  
/** 
 * @说明 从网络获取文件到本地 
 * @author qiuyuehao
 * @version 1.0 
 * @since 
 */  
public class GetNetworkFileToLocal {  
    /** 
     * 测试 
     * @param args 
     */  
    public static void main(String[] args) {  
        String url = "http://127.0.0.1:8080/fwdj/1.txt";  
        try {
        	writeFileToDisk(url,"D:\\2.txt");
		} catch (Exception e) {
			e.printStackTrace();
		}
    }  
    /** 
     * 将图片写入到磁盘 
     * @param url  文件请求路径
     * @param path 文件保存时全路径
     * @throws Exception 
     */  
    public static boolean writeFileToDisk(String url,String path) throws Exception{ 
         byte[] btImg = getImageFromNetByUrl(url);  
         if(null != btImg && btImg.length > 0){  
             System.out.println("读取到：" + btImg.length + " 字节");  

             File file = new File(path);  
             FileOutputStream fops = new FileOutputStream(file);  
             fops.write(btImg);  
             fops.flush();  
             fops.close();  
             System.out.println("图片已写入");  
             return true;
         }else{  
             System.out.println("没有从该连接获得内容");  
             return false;
         }  
    }  
    /** 
     * 根据地址获得数据的字节流 
     * @param strUrl 网络连接地址 
     * @return 
     */  
    public static byte[] getImageFromNetByUrl(String strUrl){  
        try {  
            URL url = new URL(strUrl);  
            HttpURLConnection conn = (HttpURLConnection)url.openConnection();  
            conn.setRequestMethod("GET");  
            conn.setConnectTimeout(5 * 1000);  
            InputStream inStream = conn.getInputStream();//通过输入流获取图片数据  
            byte[] btImg = readInputStream(inStream);//得到图片的二进制数据  
            return btImg;  
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
        return null;  
    }  
    /** 
     * 从输入流中获取数据 
     * @param inStream 输入流 
     * @return 
     * @throws Exception 
     */  
    public static byte[] readInputStream(InputStream inStream) throws Exception{  
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();  
        byte[] buffer = new byte[1024];  
        int len = 0;  
        while( (len=inStream.read(buffer)) != -1 ){  
            outStream.write(buffer, 0, len);  
        }  
        inStream.close();  
        return outStream.toByteArray();  
    }  
}  