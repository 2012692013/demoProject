package com.qyh.demo.service;

import com.qyh.demo.vo.ResponseResult;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface CommonService {
    /**
     * 通用上传文件
     * @param request
     * @return
     */
    ResponseResult updateFile(HttpServletRequest request);

    /**
    * 上传文件,将文件流单独分离开
    * @author      hx
    * @return
    * @exception
    * @date        2019/10/21 3:47 PM
    */
    ResponseResult uploadFile(HttpServletRequest request , List<MultipartFile> fileList);
}

