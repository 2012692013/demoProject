package com.qyh.demo.base.util.bucket;

import com.qyh.demo.constants.ContextConstants;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
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
 * 因项目规范以swagger为主 关于swagger上下文的内容都在此获取
 * @author qiuyuehao
 * @date 2020/3/19 15:44
 * create by 2012692013@qq.com
 */
@Slf4j
@Component
public class QSwaggerContext implements ApplicationContextAware {

    private static ApplicationContext applicationContext;

    public static Map<String, List<String>> CONTENT_MAP = new HashMap<>();

    /**
     * 主要应用于ParamFilter
     * @return
     */
    public static void setContext() throws ClassNotFoundException {
        Map<String, Object> beansWithAnnotation = applicationContext.getBeansWithAnnotation(RestController.class);
        for (String key :
                beansWithAnnotation.keySet()) {
            String head = "";
            Object controllerObj = beansWithAnnotation.get(key);
            String controllerStr = controllerObj.toString();
            controllerStr = controllerStr.substring(0, controllerStr.lastIndexOf("@"));
            Class<?> controller = Class.forName(controllerStr);
            head = TouristUtil.uriUtil(controller.getAnnotation(RequestMapping.class).value()[0]);
            Method[] methods = controller.getDeclaredMethods();
            for (int i = 0; i < methods.length; i++)
                setMethodUriWithParams(head,methods[i]);
        }
    }

    /**
     * 放置方法URI及所含参数
     * @param head
     * @param declaredMethod
     */
    public static void setMethodUriWithParams(String head,Method declaredMethod) {
        String[] type = null;
        String uri = "";
        if (declaredMethod.getAnnotation(RequestMapping.class)!=null){
            RequestMapping annotation = declaredMethod.getAnnotation(RequestMapping.class);
            uri = head+TouristUtil.uriUtil(declaredMethod.getAnnotation(RequestMapping.class).value()[0]);
            RequestMethod[] method = annotation.method();
            if (method==null||method.length==0){
                type = new String[]{
                        RequestMethod.GET.name(), RequestMethod.POST.name(),
                        RequestMethod.PUT.name(),RequestMethod.DELETE.name(),
                        RequestMethod.HEAD.name(),RequestMethod.OPTIONS.name(),
                        RequestMethod.PATCH.name(),RequestMethod.TRACE.name()};
            }
            if (method.length>0){
                type = new String[method.length];
                for (int i= 0 ;i<method.length;i++)
                    type[i]=method[i].name();
            }
        }
        if (declaredMethod.getAnnotation(GetMapping.class)!=null){
            uri = head+TouristUtil.uriUtil(declaredMethod.getAnnotation(GetMapping.class).value()[0]);
            type = new String[]{"GET"};
        }
        if (declaredMethod.getAnnotation(PostMapping.class)!=null){
            uri = head+TouristUtil.uriUtil(declaredMethod.getAnnotation(PostMapping.class).value()[0]);
            type = new String[]{"POST"};
        }
        if (declaredMethod.getAnnotation(PutMapping.class)!=null){
            uri = head+TouristUtil.uriUtil(declaredMethod.getAnnotation(PutMapping.class).value()[0]);
            type = new String[]{"PUT"};
        }
        if (declaredMethod.getAnnotation(DeleteMapping.class)!=null){
            uri = head+TouristUtil.uriUtil(declaredMethod.getAnnotation(DeleteMapping.class).value()[0]);
            type = new String[]{"DELETE"};
        }

        List<String> params = getMethodApiImplicitParam(declaredMethod);

        if (type!=null
                && type.length > 0)
            for (int i = 0; i < type.length; i++)
                CONTENT_MAP.put(ContextConstants.SERVER_PATH + uri + ":" + type[i],params);
    }

    /**
     * 在此获取方法上的内容
     * @param method
     * @return
     */
    public static List<String> getMethodApiImplicitParam(Method method) {
        List<String> apiImplicitParamList = new ArrayList<>();
            if (method.isAnnotationPresent(ApiImplicitParams.class)) {
                ApiImplicitParams apiImplicitParams = method.getAnnotation(ApiImplicitParams.class);
                if (apiImplicitParams.value().length > 0) {
                    ApiImplicitParam[] apiImplicitParamArr = apiImplicitParams.value();
                    for (int i = 0; i < apiImplicitParamArr.length; i++) {
                        ApiImplicitParam apiImplicitParam = apiImplicitParamArr[i];
                        apiImplicitParamList.add(apiImplicitParam.name());
                }
            }
        }
        return apiImplicitParamList;
    }

    @Autowired
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
