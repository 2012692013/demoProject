package com.qyh.demo.base.util;
import java.text.SimpleDateFormat;  
  
import net.sf.json.JsonConfig;  
import net.sf.json.processors.JsonValueProcessor;  

/**
 * 把时间类型转化成json时需要加入的规则
 * @author qiuyuehao
 *
 */
public class JsonDateValueProcessor implements JsonValueProcessor {  
	private String format;  
	public JsonDateValueProcessor(String format){  
        this.format = format;  
    }  
      
    public Object processArrayValue(Object value, JsonConfig jsonConfig){  
        return null;  
    }  
  
    public Object processObjectValue(String key, Object value, JsonConfig jsonConfig){  
        if(value == null){  
            return "";  
        }
    if(value instanceof java.sql.Timestamp){  
        String str = new SimpleDateFormat(format).format((java.sql.Timestamp)value);  
        return str;  
    }  
    if (value instanceof java.util.Date){  
        String str = new SimpleDateFormat(format).format((java.util.Date) value);  
        return str;  
    }  
      
    return value.toString();  
}  
  
}  
