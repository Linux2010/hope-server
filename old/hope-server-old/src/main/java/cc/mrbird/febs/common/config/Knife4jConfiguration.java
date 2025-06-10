package cc.mrbird.febs.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2WebMvc;

/**
 * @author fanta
 */
@Configuration
@EnableSwagger2WebMvc
public class Knife4jConfiguration {

    @Bean(value = "defaultApi2")
    public Docket defaultApi2() {
        Docket docket=new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(new ApiInfoBuilder()
                        .title("hope server APIs")
                        .description("# hope server APIs")
                        .termsOfServiceUrl("http://www.kylin.ink/")
                        .contact("949465194@qq.com")
                        .version("1.0")
                        .build())
                //分组名称
                .groupName("1.0版本")
                .select()
                //这里指定Controller扫描包路径
                .apis(RequestHandlerSelectors.basePackage("cc.mrbird.febs.hope.controller"))
                .paths(PathSelectors.any())
                .build();
        return docket;
    }

//
//    @Bean(value = "defaultApi2")
//    public Docket defaultApi2() {
//        //schema
//        List<GrantType> grantTypes=new ArrayList<>();
//        //密码模式
//        String passwordTokenUrl="http://192.168.1.10:8080/oauth/token";
//        ResourceOwnerPasswordCredentialsGrant resourceOwnerPasswordCredentialsGrant=new ResourceOwnerPasswordCredentialsGrant(passwordTokenUrl);
//        grantTypes.add(resourceOwnerPasswordCredentialsGrant);
//
//        OAuth oAuth=new OAuthBuilder().name("oauth2")
//                .grantTypes(grantTypes).build();
//        //context
//        //scope方位
//        List<AuthorizationScope> scopes=new ArrayList<>();
//        scopes.add(new AuthorizationScope("read","read all resources"));
//        SecurityReference securityReference=new SecurityReference("oauth2",scopes.toArray(new AuthorizationScope[]{}));
//        SecurityContext securityContext=new SecurityContext(CollectionUtils.newArrayList(securityReference),PathSelectors.ant("/api/**"));
//        //schemas
//        List<SecurityScheme> securitySchemes=CollectionUtils.newArrayList(oAuth);
//        //securyContext
//        List<SecurityContext> securityContexts=CollectionUtils.newArrayList(securityContext);
//
//        String groupName="1.0版本";
//        Docket docket=new Docket(DocumentationType.SWAGGER_2)
//                .host("https://www.baidu.com")
//                .apiInfo((new ApiInfoBuilder()
//                        .title("hope server APIs")
//                        .description("# hope server APIs")
//                        .termsOfServiceUrl("http://www.kylin.ink/")
//                        .contact("949465194@qq.com")
//                        .version("1.0")
//                        .build()))
//                .groupName(groupName)
//                .select()
//                .apis(RequestHandlerSelectors.basePackage("cc.mrbird.febs.hope"))
//                .paths(PathSelectors.any())
//                .build()
//                .securityContexts(securityContexts).securitySchemes(securitySchemes);
//        return docket;
//    }
}