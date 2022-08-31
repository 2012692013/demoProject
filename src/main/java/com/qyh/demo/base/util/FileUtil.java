package com.qyh.demo.base.util;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import lombok.extern.slf4j.Slf4j;
import net.coobird.thumbnailator.Thumbnails;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

/**
 * <p style="color:blue"> 用于文件上传 包括图片视频等</p>
 *
 * @author qiuyuehao
 *
 */
@Slf4j
public class FileUtil {

    /**
     * 上传文件,是多文件上传,文件验证就在前端验证.比如限制文件上传格式
     * 需要修改上传文件大小的话,就修改配置文件spring-mvc.xml
     * @param goalFolder 目标文件夹（上传的目的地）
     * @param request
     * @return 返回上传后的新文件路径
     */
    public static Map<String,String> upLoadFile(String goalFolder,HttpServletRequest request) {
        CommonsMultipartResolver mutilpartResolver = new CommonsMultipartResolver(request.getSession().getServletContext());
        Map<String,String> retMap = new HashMap<String,String>();

        // request如果是Multipart类型、
        if (mutilpartResolver.isMultipart(request)) {
            // 强转成 MultipartHttpServletRequest
            MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) request;
            String projectAddress = request.getSession().getServletContext().getRealPath("/"+goalFolder);
            Iterator<String> it = multiRequest.getFileNames();
            while (it.hasNext()) {
                String textName = it.next();
                // 获取MultipartFile类型文件
                List<MultipartFile> fileList = multiRequest.getFiles(textName);
                for(MultipartFile fileDetail:fileList){
                    if (fileDetail != null && fileDetail.getSize() >= 1) {
                        String path = getFileAddress(fileDetail, projectAddress);
                        File localFile = new File(path);

                        path = path.replaceAll("\\\\", "/");
                        try {
                            // 将上传文件写入到指定文件出、核心！
                            //fileDetail.transferTo(localFile);
                            // 非常重要、有了这个想做什么处理都可以 ,可以自己处理
                            InputStream in =  fileDetail.getInputStream();
                            FileOutputStream out = new FileOutputStream(path);
                            int b = 0;
                            while((b = in.read()) != -1){
                                out.write(b);
                            }
                            in.close();
                            out.close();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        path = path.substring(path.indexOf(goalFolder));
                        if(retMap.get(textName) == null){
                            retMap.put(textName, path);
                        }else{
                            retMap.put(textName, retMap.get(textName)+","+path);
                        }
                    }
                }
            }
        }
        return retMap;

    }



    /**
     * 创建或者获取文件目录,并且修改文件名称
     *
     * @param fileDetail
     * @return
     */
    private static String getFileAddress(MultipartFile fileDetail,String projectAddress) {
        String[] fileNames = fileDetail.getOriginalFilename().split("\\.");
        String fileNamedir = projectAddress + "/";
        File localFileDir = new File(fileNamedir);
        if (!localFileDir.exists()) {
            localFileDir.mkdirs();
        }
        String fileName = UUID.randomUUID().toString().replace("-", "") + "." + fileNames[fileNames.length - 1];
        String path = fileNamedir + fileName;
        return path;
    }

    /**
     * 在网上下载东西
     *
     * @param response
     * @param destUrl
     *            目标下载地址
     * @throws MalformedURLException
     * @throws IOException
     * @throws UnsupportedEncodingException
     */
    public  static void downUrlFile(HttpServletResponse response, String destUrl)
            throws MalformedURLException, IOException,
            UnsupportedEncodingException {
        int param=destUrl.lastIndexOf("/");
        String fileName=destUrl.substring(param+1, destUrl.length());
        // 建立链接
        URL url = new URL(destUrl);
        HttpURLConnection httpUrl = (HttpURLConnection) url.openConnection();
        // 连接指定的资源
        httpUrl.connect();
        // 获取网络输入流
        BufferedInputStream bis = new BufferedInputStream(httpUrl.getInputStream());

        response.setContentType("application/x-msdownload");
        response.setHeader("Content-Disposition", "attachment; filename="+java.net.URLEncoder.encode(fileName,"UTF-8"));
        OutputStream out = response.getOutputStream();
        byte[] buf = new byte[1024];
        if (destUrl != null) {
            BufferedInputStream br = bis;
            int len = 0;
            while ((len = br.read(buf)) > 0){
                out.write(buf, 0, len);
            }
            br.close();
        }
        out.flush();
        out.close();
    }

    /**
     * 根据文件路径删除文件
     * @param request
     * @param filePath 文件路径
     * @return 是否删除成功
     */
    public static boolean delFileByPath(HttpServletRequest request,String filePath){
        String path = request.getSession().getServletContext().getRealPath("/")+"/"+filePath;
        File file = new File(path);
        if (file.exists()) {
            file.delete();
            return true;
        }
        return false;
    }


    /**
     * 复制单个文件
     *
     * @param srcFileName
     *            待复制的文件名
     * @param destFileName
     *            目标文件名
     * @param overlay
     *            如果目标文件存在，是否覆盖
     * @return 如果复制成功返回true，否则返回false
     */
    public static boolean copyFile(String srcFileName, String destFileName,boolean overlay) {
        File srcFile = new File(srcFileName);

        // 判断源文件是否存在
        if (!srcFile.exists()) {
            System.err.println("源文件：" + srcFileName + "不存在！");
            return false;
        } else if (!srcFile.isFile()) {
            System.err.println("复制文件失败，源文件：" + srcFileName + "不是一个文件！");
            return false;
        }

        // 判断目标文件是否存在
        File destFile = new File(destFileName);
        if (destFile.exists()) {
            // 如果目标文件存在并允许覆盖
            if (overlay) {
                // 删除已经存在的目标文件，无论目标文件是目录还是单个文件
                new File(destFileName).delete();
            }
        } else {
            // 如果目标文件所在目录不存在，则创建目录
            if (!destFile.getParentFile().exists()) {
                // 目标文件所在目录不存在
                if (!destFile.getParentFile().mkdirs()) {
                    // 复制文件失败：创建目标文件所在目录失败
                    return false;
                }
            }
        }

        // 复制文件
        int byteread = 0; // 读取的字节数
        InputStream in = null;
        OutputStream out = null;

        try {
            in = new FileInputStream(srcFile);
            out = new FileOutputStream(destFile);
            byte[] buffer = new byte[1024];

            while ((byteread = in.read(buffer)) != -1) {
                out.write(buffer, 0, byteread);
            }
            return true;
        } catch (FileNotFoundException e) {
            return false;
        } catch (IOException e) {
            return false;
        } finally {
            try {
                if (out != null)
                    out.close();
                if (in != null)
                    in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 复制整个目录的内容
     *
     * @param srcDirName
     *            待复制目录的目录名
     * @param destDirName
     *            目标目录名
     * @param overlay
     *            如果目标目录存在，是否覆盖
     * @return 如果复制成功返回true，否则返回false
     */
    public static boolean copyDirectory(String srcDirName, String destDirName,
                                        boolean overlay) {
        // 判断源目录是否存在
        File srcDir = new File(srcDirName);
        if (!srcDir.exists()) {
            System.err.println("复制目录失败：源目录" + srcDirName + "不存在！");
            return false;
        } else if (!srcDir.isDirectory()) {
            System.err.println("复制目录失败：" + srcDirName + "不是目录！");
            return false;
        }

        // 如果目标目录名不是以文件分隔符结尾，则加上文件分隔符
        if (!destDirName.endsWith(File.separator)) {
            destDirName = destDirName + File.separator;
        }
        File destDir = new File(destDirName);
        // 如果目标文件夹存在
        if (destDir.exists()) {
            // 如果允许覆盖则删除已存在的目标目录
            if (overlay) {
                new File(destDirName).delete();
            } else {
                System.err.println("复制目录失败：目的目录" + destDirName + "已存在！");
                return false;
            }
        } else {
            // 创建目的目录
            System.out.println("目的目录不存在，准备创建。。。");
            if (!destDir.mkdirs()) {
                System.out.println("复制目录失败：创建目的目录失败！");
                return false;
            }
        }

        boolean flag = true;
        File[] files = srcDir.listFiles();
        for (int i = 0; i < files.length; i++) {
            // 复制文件
            if (files[i].isFile()) {
                flag = copyFile(files[i].getAbsolutePath(),
                        destDirName + files[i].getName(), overlay);
                if (!flag)
                    break;
            } else if (files[i].isDirectory()) {
                flag = copyDirectory(files[i].getAbsolutePath(),
                        destDirName + files[i].getName(), overlay);
                if (!flag)
                    break;
            }
        }
        if (!flag) {
            System.err.println("复制目录" + srcDirName + "至" + destDirName + "失败！");
            return false;
        } else {
            return true;
        }
    }


    /**
     * 上传文件,是多文件上传,文件验证就在前端验证.比如限制文件上传格式
     * 需要修改上传文件大小的话,就修改配置文件spring-mvc.xml
     * 添加修复iphone上传图片颠倒的问题
     * @param goalFolder 目标文件夹（上传的目的地）
     * @param request
     * @return 返回上传后的新文件路径
     */
    public static Map<String,String> upLoadFileRotate(String goalFolder,HttpServletRequest request) {
        CommonsMultipartResolver mutilpartResolver = new CommonsMultipartResolver(request.getSession().getServletContext());
        Map<String,String> retMap = new HashMap<String,String>();

        // request如果是Multipart类型、
        if (mutilpartResolver.isMultipart(request)) {
            // 强转成 MultipartHttpServletRequest
            MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) request;
            String projectAddress = request.getSession().getServletContext().getRealPath("/"+goalFolder);
            Iterator<String> it = multiRequest.getFileNames();

            String rotate = request.getParameter("rotate");
            int i=0;
            String[] rotates = rotate.split(",");

            System.out.println(rotate);
            while (it.hasNext()) {
                String textName = it.next();
                // 获取MultipartFile类型文件
                List<MultipartFile> fileList = multiRequest.getFiles(textName);
                for(MultipartFile fileDetail:fileList){
                    if (fileDetail != null && fileDetail.getSize() >= 1) {
                        String path = getFileAddress(fileDetail, projectAddress);
                        File localFile = new File(path);

                        path = path.replaceAll("\\\\", "/");
                        try {
                            // 将上传文件写入到指定文件出、核心！
                            //fileDetail.transferTo(localFile);
                            // 非常重要、有了这个想做什么处理都可以 ,可以自己处理

                            InputStream in =  fileDetail.getInputStream();
	                        /* Metadata metadata = JpegMetadataReader.readMetadata(in);
	                         Directory directory = metadata.getFirstDirectoryOfType(ExifDirectoryBase.class);
	                         System.out.println(directory==null);
	                         int orientation=0;
	                         if(directory != null && directory.containsTag(ExifDirectoryBase.TAG_ORIENTATION)){ // Exif信息中有保存方向,把信息复制到缩略图
	                             orientation = directory.getInt(ExifDirectoryBase.TAG_ORIENTATION); // 原图片的方向信息
	                             System.out.println(orientation);
	                         }*/
                            if("1".equals(rotates[i])){
                                System.out.println("rotate 90");
                                BufferedImage src = ImageIO.read(in);
                                BufferedImage des = RotateImage.Rotate(src, 90);
                                ImageIO.write(des,"jpg", new File(path));
                            }else{
                                in =  fileDetail.getInputStream();
                                FileOutputStream out = new FileOutputStream(path);
                                int b = 0;
                                while((b = in.read()) != -1){
                                    out.write(b);
                                }
                                in.close();
                                out.close();
                            }


                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        path = path.substring(path.indexOf(goalFolder));
                        if(retMap.get(textName) == null){
                            retMap.put(textName, path);
                        }else{
                            retMap.put(textName, retMap.get(textName)+","+path);
                        }
                    }
                    i++;
                }
            }
        }
        return retMap;

    }

    /**
     * @description 给图片添加水印文字、可设置水印文字的旋转角度
     * @param logoText 要写入的文字
     * @param srcImgPath 源图片路径
     * @param newImagePath 新图片路径
     * @param degree 旋转角度
     * @param color  字体颜色
     * @param formaName 图片后缀
     * @date     2019年02月19日 14:23:28
     * @author qiuyuehao
     */
    public static void markImageByText(String logoText, String srcImgPath, String newImagePath, Integer degree, Color color, String formaName) {
        InputStream is = null;
        OutputStream os = null;
        try {
            // 1、源图片
            java.awt.Image srcImg = ImageIO.read(new File(srcImgPath));
            BufferedImage buffImg = new BufferedImage(srcImg.getWidth(null),srcImg.getHeight(null), BufferedImage.TYPE_INT_RGB);
            // 2、得到画笔对象
            Graphics2D g = buffImg.createGraphics();
            // 3、设置对线段的锯齿状边缘处理
            g.setRenderingHint(RenderingHints.KEY_INTERPOLATION,RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            g.drawImage(srcImg.getScaledInstance(srcImg.getWidth(null), srcImg.getHeight(null), java.awt.Image.SCALE_SMOOTH), 0, 0, null);
            // 4、设置水印旋转
            if (null != degree) {
                g.rotate(Math.toRadians(degree),  buffImg.getWidth()/2,buffImg.getHeight() /2);
            }
            // 5、设置水印文字颜色
            g.setColor(color);
            // 6、设置水印文字Font
            g.setFont(new java.awt.Font("宋体", java.awt.Font.BOLD, buffImg.getHeight() /2));
            // 7、设置水印文字透明度
            g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, 0.15f));
            // 8、第一参数->设置的内容，后面两个参数->文字在图片上的坐标位置(x,y)
            g.drawString(logoText,  buffImg.getWidth()/2 , buffImg.getHeight()/2);
            // 9、释放资源
            g.dispose();
            // 10、生成图片
            os = new FileOutputStream(newImagePath);
            ImageIO.write(buffImg, formaName, os);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (null != is)
                    is.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                if (null != os)
                    os.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
    * 图片压缩
    * @author qiuyuehao
    * @return
    * @exception
    * @date        2019/12/19 11:11 AM
    */
    public static void compressImg(){
        try {
            Thumbnails.of(new File("原文件路径")).scale(1f).outputQuality(0.3f).toFile("目标文件路径");
        } catch (IOException e) {
            log.error("==========================压缩异常");
            e.printStackTrace();
        }
    }

}
