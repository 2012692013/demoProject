package com.qyh.demo.controller.api;

import com.qyh.demo.base.util.JedisUtil;
import com.qyh.demo.base.util.bucket.TouristApi;
import com.qyh.demo.base.util.VerifyCodeUtils;
import com.qyh.demo.dto.SysUserDto;
import com.qyh.demo.enums.JedisIndex;
import com.qyh.demo.service.UserService;
import com.qyh.demo.vo.ResponseResult;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Date;

import static com.qyh.demo.vo.ResponseResult.error;
import static com.qyh.demo.vo.ResponseResult.success;

/**
 * Created with IDEA
 * author:huxi
 * Date:2019/1/17
 * Time:9:27 PM
 */
@RestController
@RequestMapping("user")
public class UserApiController {

    @Autowired
    private UserService userService;

    /**
     * 获取时间戳
     * @param phone
     * @return
     * 2018年8月15日
     * @author yh
     */
    @RequestMapping(value="/getCurSysTimestamp",method= RequestMethod.GET,headers="api-version=1")
    public ResponseResult getCurSysTimestamp(String phone){
        if(StringUtils.isEmpty(phone)){
            return ResponseResult.error();
        }
        Long ts = System.currentTimeMillis();
        //存入缓存
        JedisUtil.getJedisUtil().set(phone+"timestamp",String.valueOf(ts), JedisIndex.MSG_CODE);
        JedisUtil.getJedisUtil().expire(phone+"timestamp",5,JedisIndex.MSG_CODE);
        return ResponseResult.success("请求成功",String.valueOf(ts));
    }

    /**
     * 获取验证码
     * @param userDto
     * @return
     * 2018年9月26日
     * @author yh
     * @throws Exception
     */
    @RequestMapping(value="/getMsgCode",method= RequestMethod.GET,headers="api-version=1")
    @ResponseBody
    public ResponseResult getMsgCode(SysUserDto userDto) throws Exception{
        return this.userService.getMsgCode(userDto);
    }

    @GetMapping("/imgVerify")
    @TouristApi
    @ApiResponses({
            @ApiResponse(code = 200,message = "response headers:imgCode")
    })
    public void getImageCode1(HttpServletRequest request, HttpServletResponse response){
        try {
            // 设置响应的类型格式为图片格式
            response.setContentType("image/jpeg");
            // 禁止图像缓存。
            response.setHeader("Pragma","no-cache");
            response.setHeader("Cache-Control","no-cache");
            response.setDateHeader("Expires",0);

            // 生成随机字串
            String verifyCode = VerifyCodeUtils.generateVerifyCode(4);

            HttpSession session = request.getSession(true);
            // 删除以前的
            session.removeAttribute("verifyCode");
            // 存入会话session
            session.setAttribute("verifyCode", verifyCode.toLowerCase());
            request.getSession().setAttribute("codeTime",new Date().getTime());
            // 生成图片
            int w = 150, h = 30;
            response.setHeader("imgCode",verifyCode);
            VerifyCodeUtils.outputImage(w, h, response.getOutputStream(), verifyCode);
        } catch (IOException e) {

        }
    }
}
