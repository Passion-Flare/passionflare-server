package flare.passion.config;

import flare.passion.interceptor.AuthChecker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebAppConfig implements WebMvcConfigurer {

    @Autowired
    private AuthChecker authChecker;
    @Value("${files.path}")
    private String filePath;

    /*
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(authChecker)
                .addPathPatterns("/**")
                .excludePathPatterns("/api/user/register", "/api/user/login", "/api/time")
                .excludePathPatterns("/swagger-resources/**", "/webjars/**", "/v2/**", "/swagger-ui/**");
    }
    */

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins(
                        "http://10.251.254.101/",
                        "http://127.0.0.1:8080/",
                        "http://114.116.248.156/",
                        "http://127.0.0.1:8081/",
                        "http://114.116.225.248/")
                .allowCredentials(true)
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .maxAge(3600);
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/pictures/**")
                .addResourceLocations("file:" + filePath + "/pictures/");
    }

}
