package com.qyh.demo.constants;

import lombok.Data;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * @author q_y_h
 * @date 2019/5/23 10:55
 * create by 2012692013@qq.com
 */
@Data
@Component
public class PayConstants {

    private static final String[] PAY_TYPE_CONTANTS = new String[]{"alipay","wechat","balance","point","offline","wechatApp","wechatPc","alipayApp","alipayPc"};

    public static final String ALIPAY = "alipay";
    public static final String WECHAT = "wechat";
    public static final String BALANCE = "balance";
    public static final String POINT = "point";
    public static final String OFFLINE = "offline";
    public static List<String> PAY_TYPE = new ArrayList<>();

    public static final String WECHAT_APP = "wechatApp";
    public static final String WECHAT_PC = "wechatPc";
    public static final String ALIPAY_APP = "alipayApp";
    public static final String ALIPAY_PC = "alipayPc";

    static {
        for (int i = 0; i < PAY_TYPE_CONTANTS.length; i++) {
            PAY_TYPE.add(PAY_TYPE_CONTANTS[i]);
        }
    }
}
