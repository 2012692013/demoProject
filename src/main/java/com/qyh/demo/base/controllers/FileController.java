package com.qyh.demo.base.controllers;

import com.qyh.demo.base.util.FileUtil;
import com.qyh.demo.vo.ResponseResult;
import com.github.pagehelper.util.StringUtil;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;


@RestController
@RequestMapping("sys/file")
public class FileController {

	@RequestMapping(value="uploadFile",method=RequestMethod.POST)
	public ResponseResult upCodeFile(HttpServletRequest request, String type){
		if (StringUtil.isEmpty(type)){
			type = "default";
		}
		Map<String,String> fileMap = FileUtil.upLoadFile("uploadFile/"+type, request);
		if(fileMap != null && fileMap.size() >= 1){
			String filePath = fileMap.get("file");
			if (StringUtil.isNotEmpty(filePath)) {
				return ResponseResult.success("上传成功",filePath);
			}
		}
       return ResponseResult.error("上传失败");
	}
	
	
}
