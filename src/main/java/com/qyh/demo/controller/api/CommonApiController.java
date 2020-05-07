package com.qyh.demo.controller.api;

import com.qyh.demo.base.cache.SysCodeUtil;
import com.qyh.demo.base.util.StringUtils;
import com.qyh.demo.base.util.bucket.TouristApi;
import com.qyh.demo.service.CommonService;
import com.qyh.demo.vo.ResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

import static com.qyh.demo.vo.ResponseResult.error;
import static com.qyh.demo.vo.ResponseResult.success;

/**
 * 通用接口
 */

@RestController
@RequestMapping("commonApi")
@Api(tags = "通用")
public class CommonApiController {


    @Autowired
    private CommonService commonService;

    /**
     * 通用上传文件
     * @param request
     * @return
     */
    @TouristApi
    @RequestMapping(value = "updateFile",method = RequestMethod.POST,headers = "api-version=1")
    public ResponseResult updateFile(HttpServletRequest request){
        return commonService.updateFile(request);
    }

    /**
     * 通用上传文件 将文件单独分离
     * @param request
     * @return
     */
    @TouristApi
    @RequestMapping(value = "uploadFile",method = RequestMethod.POST,headers = "api-version=1")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "fileList", value = "文件集合", required = true, dataType = "file",paramType="path")
    })
    @ApiOperation("上传文件")
    public ResponseResult uploadFile(HttpServletRequest request, List<MultipartFile> fileList){
        return commonService.uploadFile(request,fileList);
    }



    /**
     * 查询系统参数
     * @param codeKey 参数键
     * @param type 参数类型
     * @return
     */
    @RequestMapping(value = "getSysCode",method = RequestMethod.GET,headers = "api-version=1")
    @ResponseBody
    public ResponseResult getSysCode(String codeKey,String type){
        if(StringUtils.isEmpty(codeKey)){
            return ResponseResult.error();
        }
        return ResponseResult.success(SysCodeUtil.getCode(type,codeKey));
    }
}
