package br.com.taian.bank.api.config

import org.springframework.beans.factory.annotation.*
import org.springframework.context.annotation.*
import org.springframework.web.servlet.config.annotation.*
import springfox.documentation.builders.*
import springfox.documentation.service.*
import springfox.documentation.spi.*
import springfox.documentation.spring.web.plugins.*
import springfox.documentation.swagger2.annotations.*

@Configuration
@EnableSwagger2
class AppConfig : WebMvcConfigurationSupport() {

    @Autowired
    lateinit var emailInterceptor: EmailInterceptor

    override fun addInterceptors(registry: InterceptorRegistry) {
        registry.addInterceptor(emailInterceptor)
    }

    @Bean
    fun swaggerUI(): Docket {
        return Docket(DocumentationType.SWAGGER_2)
            .select()
            .apis(RequestHandlerSelectors.basePackage("br.com.taian.bank.api"))
            .build()
            .apiInfo(metaData())
    }

    private fun metaData(): ApiInfo {
        return ApiInfoBuilder()
            .title("Taian Bank REST API")
            .description("\"Taian Bank REST API\"")
            .version("1.0.0")
            .license("Apache License Version 2.0")
            .licenseUrl("https://www.apache.org/licenses/LICENSE-2.0")
            .build()
    }

    override fun addResourceHandlers(registry: ResourceHandlerRegistry) {
        registry
            .addResourceHandler("swagger-ui.html")
            .addResourceLocations("classpath:/META-INF/resources/")

        registry
            .addResourceHandler("/webjars/**")
            .addResourceLocations("classpath:/META-INF/resources/webjars/")
    }
}
