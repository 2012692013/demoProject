package com.qyh.demo.base.util;

import com.qyh.demo.vo.Result;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Random;

/**
 * @title VerificationCodeServiceImp.java
 * @package com.hmkj.framework.service.imp
 * @author qiuyuehao
 * @E-mail:
 * @version 2012-5-4 下午05:34:39
 * @version V1.0
 * @description 
 */
public class VerificationCodeCreate {
	
	public static Color getRanColor(int fc, int bc) {
		 Random random=new Random();
	        if(fc>255) fc=255;
	        if(bc>255) bc=255;
	        int r=fc+random.nextInt(bc-fc);
	        int g=fc+random.nextInt(bc-fc);
	        int b=fc+random.nextInt(bc-fc);
	        return new Color(r,g,b);
	}

	public static void createVerifyCode(HttpServletRequest request,
			HttpServletResponse response) {
		response.setHeader("Pragma","No-cache");
		response.setHeader("Cache-Control","no-che");
		response.setDateHeader("Expires",0);
		response.setContentType("image/jpeg");
		HttpSession session = request.getSession();
		session.removeAttribute("rand");
		
		int width=60;
		int height=40;
		BufferedImage image=new BufferedImage(width,height,BufferedImage.TYPE_INT_RGB);
		Graphics g =image.getGraphics();
		Random  random=new Random();
		g.setColor(getRanColor(200,250));
		g.fillRect(0,0,width,height);
		g.setFont(new Font("Times New Roman",Font.PLAIN,20));
		g.setColor(getRanColor(160,200));
		
		for(int i=0;i<155;i++)
		{
			int x=random.nextInt(width);
			int y=random.nextInt(height);
			int xl=random.nextInt(12);
			int yl=random.nextInt(12);
			g.drawLine(x,y,x+xl,y+yl);
		}
		String sRand="";
		for(int i=0;i<4;i++)
		{
			String rand=String.valueOf(random.nextInt(10));
			sRand+=rand;
			g.setColor(new Color(20+random.nextInt(110),40+random.nextInt(110),60+random.nextInt(110)));
			g.drawString(rand,13*i+6,26);
		}
		session.setAttribute("rand",sRand);//保存验证码
		g.dispose();
		try {
			ImageIO.write(image,"JPEG",response.getOutputStream());
		} catch (IOException e) {
			e.printStackTrace();
		} 
	}

	public static Result checkVerify( HttpServletRequest request,String code) {
		Result result = new Result();
		HttpSession session = request.getSession();
		String rand=(String)session.getAttribute("rand");//服务器存的  验证码
		String isCheck = "0";//校验不通过
		if(StringUtils.isEmpty(code)) {
			return result;
		}
		if(code.equals(rand)){
			isCheck = "1";//校验通过
		}
		result.setResltInfo("1", "操作成功", isCheck);
		return result;
	}


}

