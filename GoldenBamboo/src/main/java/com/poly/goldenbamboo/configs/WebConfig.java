package com.poly.goldenbamboo.configs;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

//import com.poly.goldenbamboo.auth.AuthInterceptor;

@Configuration
public class WebConfig implements WebMvcConfigurer {

//	@Autowired
//	private AuthInterceptor authInterceptor;
//
//	@Override
//    public void addInterceptors(InterceptorRegistry registry) {
//        registry.addInterceptor(authInterceptor)
//                .addPathPatterns("/Manager/**", "/Client/**", "/Staff/**")
//                .excludePathPatterns("/Login", "/Login/CheckLogin", "/Home", "/");
//    }

	@Override
	public void addCorsMappings(CorsRegistry registry) {
		registry.addMapping("/**").allowedOrigins("http://localhost:3000")
				.allowedMethods("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS").allowedHeaders("*")
				.allowCredentials(true).exposedHeaders("Location");
		
	}

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/uploads/**").addResourceLocations("file:uploads/").setCachePeriod(3600);
	}
}