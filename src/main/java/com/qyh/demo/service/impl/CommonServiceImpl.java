package com.qyh.demo.service.impl;

import com.qyh.demo.base.util.FileUtil;
import com.qyh.demo.service.CommonService;
import com.qyh.demo.vo.ResponseResult;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.*;

@Service
public class CommonServiceImpl implements CommonService {
    @Override
    public ResponseResult updateFile(HttpServletRequest request) {
        Map<String,String> fileMap = FileUtil.upLoadFile("uploadFile", request);
        Set<Map.Entry<String,String>> set = fileMap.entrySet();

        StringBuffer sb = new StringBuffer();
        for(Map.Entry<String,String> e : set){
            sb.append(e.getValue()+",");
        }

        return ResponseResult.success(sb.toString().substring(0,sb.length()-1));
    }

    @Override
    public ResponseResult uploadFile(HttpServletRequest request, List<MultipartFile> fileList) {
        // request如果是Multipart类型、
            // 强转成 MultipartHttpServletRequest
        String projectAddress = request.getSession().getServletContext().getRealPath("/uploadFile");

        StringBuilder sb = new StringBuilder();

        for(MultipartFile fileDetail:fileList){
            if (fileDetail != null && fileDetail.getSize() >= 1) {

                String[] fileNames = fileDetail.getOriginalFilename().split("\\.");
                String fileNamedir = projectAddress + "/";
                File localFileDir = new File(fileNamedir);
                if (!localFileDir.exists()) {
                    localFileDir.mkdirs();
                }
                String fileName = UUID.randomUUID().toString().replace("-", "") + "." + fileNames[fileNames.length - 1];
                String path = fileNamedir + fileName;

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
                sb.append(path.substring(path.indexOf("/uploadFile"))+",");

            }
        }
        return new ResponseResult(200,"上传成功",sb.toString());
    }
}
