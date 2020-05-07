package com.qyh.demo.base.util;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix; 

import javax.imageio.ImageIO; 

import java.io.File; 
import java.io.OutputStream; 
import java.io.IOException; 
import java.util.Hashtable;
import java.awt.image.BufferedImage;

public class QRUtil {
	
   private static final int BLACK = 0xFF000000; 
   private static final int WHITE = 0xFFFFFFFF; 
    
   private QRUtil() {} 
   
   /**
    * 创建二维码
    * @param content	二维码内容
    * @param width		宽度
    * @param height		高度
    * @param suffix		二维码文件后缀
    * @param filePath	二维码保存地址
    * @return
    */
   public static boolean createQRCode(String content,int width,int height,String suffix,String filePath){
	   try{
	       //二维码的图片格式 
	       Hashtable hints = new Hashtable(); 
	       //内容所使用编码 
	       hints.put(EncodeHintType.CHARACTER_SET, "utf-8"); 
	       BitMatrix bitMatrix = new MultiFormatWriter().encode(content, 
	               BarcodeFormat.QR_CODE, width, height, hints); 
	       //生成二维码 
	       File outputFile = new File(filePath); 
	       if(!outputFile.exists()) {    
	    	   outputFile.mkdirs();
			} 
	       writeToFile(bitMatrix, suffix, outputFile); 
		   
	       return true;
	   }catch(Exception e){
		   e.printStackTrace();
		   return false;
	   }
   }
   
   public static BufferedImage toBufferedImage(BitMatrix matrix) { 
     int width = matrix.getWidth(); 
     int height = matrix.getHeight(); 
     BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB); 
     for (int x = 0; x < width; x++) { 
       for (int y = 0; y < height; y++) { 
         image.setRGB(x, y, matrix.get(x, y) ? BLACK : WHITE); 
       } 
     } 
     return image; 
   } 
    
      
   public static void writeToFile(BitMatrix matrix, String format, File file) 
       throws IOException { 
     BufferedImage image = toBufferedImage(matrix); 
     if (!ImageIO.write(image, format, file)) { 
       throw new IOException("Could not write an image of format " + format + " to " + file); 
     } 
   } 
    
      
   public static void writeToStream(BitMatrix matrix, String format, OutputStream stream) 
       throws IOException { 
     BufferedImage image = toBufferedImage(matrix); 
     if (!ImageIO.write(image, format, stream)) { 
       throw new IOException("Could not write an image of format " + format); 
     } 
   } 
}
