package com.qyh.demo.base.config;

import com.fasterxml.jackson.databind.introspect.BeanPropertyDefinition;
import com.google.common.base.Function;
import com.google.common.base.Optional;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.Extension;
import io.swagger.annotations.ExtensionProperty;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.Annotations;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.schema.contexts.ModelPropertyContext;
import springfox.documentation.spring.web.DescriptionResolver;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger.common.SwaggerPluginSupport;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

@Configuration
@EnableSwagger2//启用swagger2
@Slf4j
public class Swagger2 {

	private final DescriptionResolver descriptions;
	@Value("${swagger.enable}")
	private Boolean enable;

	@Bean
	public Docket createRestApi() {


		List<Parameter> pars = new ArrayList<Parameter>();

		ParameterBuilder ticketPar = new ParameterBuilder();
		ticketPar.name("token").description("登录校验")
				.modelRef(new ModelRef("string")).parameterType("header")
				//required表示是否必填，defaultvalue表示默认值
				.required(false).defaultValue("dev").build();
		pars.add(ticketPar.build());

		ParameterBuilder pageNum = new ParameterBuilder();
		pageNum.name("pageNum").description("页码")
				.modelRef(new ModelRef("string")).parameterType("query")
				.required(false).defaultValue("1").build();
		pars.add(pageNum.build());

		ParameterBuilder pageSize = new ParameterBuilder();
		pageSize.name("pageSize").description("分页数")
				.modelRef(new ModelRef("string")).parameterType("query")
				.required(false).defaultValue("10").build();
		pars.add(pageSize.build());

		ParameterBuilder secret = new ParameterBuilder();
		secret.name("apiSecret").description("加密字符(开发环境不校验)")
				.modelRef(new ModelRef("string")).parameterType("header")
				.required(false).defaultValue("1").build();
		pars.add(secret.build());

		ParameterBuilder timestamp = new ParameterBuilder();
		timestamp.name("timestamp").description("当前时间戳(开发环境不校验)")
				.modelRef(new ModelRef("string")).parameterType("header")
				.required(false).defaultValue(String.valueOf(System.currentTimeMillis())).build();
		pars.add(timestamp.build());

		return new Docket(DocumentationType.SWAGGER_2).apiInfo(apiInfo())
				// 是否开启
				.enable(this.enable).select()
				// 扫描的路径包
				.apis(RequestHandlerSelectors.basePackage("com.qyh.security"))
				// 指定路径处理PathSelectors.any()代表所有的路径
				.paths(PathSelectors.any()).build().pathMapping("/")
				//开启协议支持
				.protocols(new HashSet<String>(){{
					add("http");
					add("https");
				}})
				.globalOperationParameters(pars);
	}

	private ApiInfo apiInfo() {
		return new ApiInfoBuilder()
				.title("demo")
				.description("包括app、pc、后台接口文档")
				.termsOfServiceUrl("")
				.version("1.0")
				.build();
	}


	@Autowired
	public Swagger2(DescriptionResolver descriptions) {
		this.descriptions = descriptions;
	}

	public void apply(ModelPropertyContext context) {
		Optional<ApiModelProperty> annotation = Optional.absent();

		if (context.getBeanPropertyDefinition().isPresent()) {
			annotation = annotation.or(Annotations.findPropertyAnnotation((BeanPropertyDefinition) context.getBeanPropertyDefinition().get(), ApiModelProperty.class));
		}

		if (annotation.isPresent()) {
			context.getBuilder().extensions((List<VendorExtension>) annotation.transform(toExtension()).orNull());
		}

	}

	public boolean supports(DocumentationType delimiter) {
		return SwaggerPluginSupport.pluginDoesApply(delimiter);
	}


	static Function<ApiModelProperty, List<VendorExtension>> toExtension() {
		return new Function<ApiModelProperty, List<VendorExtension>>() {
			public List<VendorExtension> apply(ApiModelProperty annotation) {
				Extension[] extensions = annotation.extensions();
				List<VendorExtension> list = new ArrayList<>();
				if (extensions != null && extensions.length > 0) {
					for (int i = 0; i < extensions.length; i++) {

						Extension extension = extensions[i];
						String name = extension.name();
						ObjectVendorExtension objectVendorExtension = new ObjectVendorExtension(name);
						if (!name.equals("")) {
							for (int j = 0; j < extension.properties().length; j++) {
								ExtensionProperty extensionProperty = extension.properties()[j];
								StringVendorExtension stringVendorExtension = new StringVendorExtension(extensionProperty.name(), extensionProperty.value());
								objectVendorExtension.addProperty(stringVendorExtension);
							}
							list.add(objectVendorExtension);
						}
					}
				}
				return list;
			}
		};
	}
}
