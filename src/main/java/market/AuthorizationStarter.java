package market;

import com.github.scribejava.apis.VkontakteApi;
import com.github.scribejava.core.builder.ServiceBuilder;
import com.github.scribejava.core.oauth.OAuth20Service;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;


@SpringBootApplication
@EnableEurekaClient
@EnableFeignClients
@EnableHystrix
@PropertySource("classpath:oauth2Client.properties")
public class AuthorizationStarter {

    public static void main(String[] args) {
        SpringApplication.run(AuthorizationStarter.class, args);
    }

    @Value("${vk.client.id}")
    private String clientID;
    @Value("${vk.client.secret}")
    private String clientSecret;
    @Value("${vk.client.scope}")
    private String scope;
    @Value("${vk.client.redirect}")
    private String redirectCallback;

    @Bean
    public OAuth20Service getVkOAuth20Service() {
        OAuth20Service service = new ServiceBuilder(clientID)
                .apiSecret(clientSecret)
                .defaultScope(scope)
                .callback(redirectCallback)
                .build(VkontakteApi.instance());
        return service;
    }
}
