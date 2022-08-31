package com.qyh.demo.base.util.bucket;

import com.qyh.demo.constants.ContextConstants;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Method;
import java.util.*;

/**
 * @author qiuyuehao
 * @date 2019/5/31 14:19
 * create by 2012692013@qq.com
 */
@Slf4j
@Component
public class TouristUtil implements ApplicationContextAware {
    private static ApplicationContext applicationContext;

    public static Map<String,Boolean> ANO_MAP = new HashMap<>();

    public static Map tourist(){
        Map<String, Object> beansWithAnnotation = applicationContext.getBeansWithAnnotation(RestController.class);
        String[] activeProfiles = applicationContext.getEnvironment().getActiveProfiles();
        ContextConstants.PROFILES_ACTIVE = activeProfiles[0];
        String head;
        String uri = "";
        for (String key:beansWithAnnotation.keySet()
                ) {
            head = "";
            Object controller = beansWithAnnotation.get(key);
            try {
                Class<?> aClass = Class.forName(controller.toString().substring(0, controller.toString().lastIndexOf("@")));
                TouristApi api = aClass.getAnnotation(TouristApi.class);
                RequestMapping annotationControl = aClass.getAnnotation(RequestMapping.class);

                String[] value = annotationControl.value();
                head = uriUtil(value[0]);
                Method[] declaredMethods = aClass.getDeclaredMethods();
                for (Method declaredMethod:declaredMethods
                        ) {
                    //不进行主函数扫描
                    if (declaredMethod.getName().equals("main"))
                        continue;

                    if (declaredMethod.isAnnotationPresent(TouristApi.class)
                            //覆盖判定
                            || api!=null){
                        TouristAndType touristAndType = new TouristAndType();
                        if (declaredMethod.getAnnotation(RequestMapping.class)!=null){
                            RequestMapping annotation = declaredMethod.getAnnotation(RequestMapping.class);
                            uri = head+uriUtil(declaredMethod.getAnnotation(RequestMapping.class).value()[0]);
                            RequestMethod[] method = annotation.method();
                            if (method==null||method.length==0){
                                String[] type = new String[]{
                                        RequestMethod.GET.name(), RequestMethod.POST.name(),
                                        RequestMethod.PUT.name(),RequestMethod.DELETE.name(),
                                        RequestMethod.HEAD.name(),RequestMethod.OPTIONS.name(),
                                        RequestMethod.PATCH.name(),RequestMethod.TRACE.name()};
                                touristAndType.setType(type);
                            }
                            if (method.length>0){
                                String[] strings = new String[method.length];
                                for (int i= 0 ;i<method.length;i++){
                                    strings[i]=method[i].name();
                                    touristAndType.setType(strings);
                                }
                            }
                        }
                        if (declaredMethod.getAnnotation(GetMapping.class)!=null){
                            uri = head+uriUtil(declaredMethod.getAnnotation(GetMapping.class).value()[0]);
                            touristAndType.setType(new String[]{"GET"});
                        }
                        if (declaredMethod.getAnnotation(PostMapping.class)!=null){
                            uri = head+uriUtil(declaredMethod.getAnnotation(PostMapping.class).value()[0]);
                            touristAndType.setType(new String[]{"POST"});
                        }
                        if (declaredMethod.getAnnotation(PutMapping.class)!=null){
                            uri = head+uriUtil(declaredMethod.getAnnotation(PutMapping.class).value()[0]);
                            touristAndType.setType(new String[]{"PUT"});
                        }
                        if (declaredMethod.getAnnotation(DeleteMapping.class)!=null){
                            uri = head+uriUtil(declaredMethod.getAnnotation(DeleteMapping.class).value()[0]);
                            touristAndType.setType(new String[]{"DELETE"});
                        }
                        TouristApi annotation = declaredMethod.getAnnotation(TouristApi.class);
                        if (api==null)
                            touristAndType.setTourist(annotation.tourist());
                        else
                            touristAndType.setTourist(annotation!=null&&!annotation.tourist()?false:true);

                        try {
                            if (touristAndType.getType() != null
                                    && touristAndType.getType().length > 0)
                                for (int i = 0; i < touristAndType.getType().length; i++)
                                    ANO_MAP.put(ContextConstants.SERVER_PATH + uri + ":" + touristAndType.getType()[i], touristAndType.isTourist());
                        } catch (Exception e) {
                            System.err.println("异常接口=========================="+uri);
                            System.err.println("请勿在该Controller下写main方法");
                        }
                    }
                }
            } catch (ClassNotFoundException c) {
                c.printStackTrace();
            }
        }
        return ANO_MAP;
    }

    public static String uriUtil(String uri){
        StringBuffer stringBuffer = new StringBuffer();
        if (uri!=null&&(!uri.startsWith("/"))){
            stringBuffer.append("/").append(uri);
            return stringBuffer.toString();
        }else return uri;
    }


    public static Boolean isTouristResouce(String uri, String method){
        String key = uri+":"+method;
        if (ANO_MAP.containsKey(key)&&ANO_MAP.get(key))
            return true;
        else return false;
    }

    @Data
    public static class TouristAndType{
        private boolean tourist;
        private String[] type;
    }

    @Autowired
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
