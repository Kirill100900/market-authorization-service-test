package market.config;

import market.oauth2.VkTokenResponseConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.core.convert.converter.Converter;
import org.springframework.http.RequestEntity;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.endpoint.DefaultAuthorizationCodeTokenResponseClient;
import org.springframework.security.oauth2.client.endpoint.OAuth2AccessTokenResponseClient;
import org.springframework.security.oauth2.client.endpoint.OAuth2AuthorizationCodeGrantRequest;
import org.springframework.security.oauth2.client.http.OAuth2ErrorResponseErrorHandler;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.http.converter.OAuth2AccessTokenResponseHttpMessageConverter;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;

@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    private final Converter<OAuth2AuthorizationCodeGrantRequest, RequestEntity<?>> vkRequestEntityConverter;
    private final OAuth2UserService<OAuth2UserRequest, OAuth2User> vkOAuth2UserService;
    private final OAuth2AuthorizedClientService vkOAuth2AuthorizedClientService;
    private final AuthenticationSuccessHandler vkSavedRequestAwareAuthenticationSuccessHandler;

    private static final String[] AUTH_WHITELIST = {
            "/api/auth/signup",
            "/api/auth/signin",

            // -- swagger ui
            "/api/doc/**",
            "/api/auth/v3/api-docs"
    };

    public SecurityConfig(Converter<OAuth2AuthorizationCodeGrantRequest, RequestEntity<?>> vkRequestEntityConverter,
                            OAuth2UserService<OAuth2UserRequest, OAuth2User> vkOAuth2UserService,
                            OAuth2AuthorizedClientService vkOAuth2AuthorizedClientService,
                            AuthenticationSuccessHandler vkSavedRequestAwareAuthenticationSuccessHandler) {
        this.vkRequestEntityConverter = vkRequestEntityConverter;
        this.vkOAuth2UserService = vkOAuth2UserService;
        this.vkOAuth2AuthorizedClientService = vkOAuth2AuthorizedClientService;
        this.vkSavedRequestAwareAuthenticationSuccessHandler = vkSavedRequestAwareAuthenticationSuccessHandler;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .authorizeRequests()
                    .antMatchers(AUTH_WHITELIST).permitAll()
                    .anyRequest().authenticated()
                .and()
                .logout()
                    .permitAll()
                .and()
                .oauth2Login()
                    .authorizationEndpoint()
                    .baseUri("/api/auth/oauth2/authorization")
                .and()
                    .redirectionEndpoint()
                    .baseUri("/api/auth/oauth2/callback/**")
                .and()
                    .tokenEndpoint()
                    .accessTokenResponseClient(accessTokenResponseClient())
                .and()
                    .userInfoEndpoint()
                    .userService(vkOAuth2UserService)
                .and()
                .authorizedClientService(vkOAuth2AuthorizedClientService)
                .successHandler(vkSavedRequestAwareAuthenticationSuccessHandler);
    }

    @Bean
    public OAuth2AccessTokenResponseClient<OAuth2AuthorizationCodeGrantRequest> accessTokenResponseClient() {
        DefaultAuthorizationCodeTokenResponseClient accessTokenResponseClient =
                new DefaultAuthorizationCodeTokenResponseClient();
        accessTokenResponseClient.setRequestEntityConverter(vkRequestEntityConverter);

        OAuth2AccessTokenResponseHttpMessageConverter tokenResponseHttpMessageConverter =
                new OAuth2AccessTokenResponseHttpMessageConverter();
        tokenResponseHttpMessageConverter.setTokenResponseConverter(new VkTokenResponseConverter());

        RestTemplate restTemplate = new RestTemplate(Arrays.asList(new FormHttpMessageConverter(),
                tokenResponseHttpMessageConverter));
        restTemplate.setErrorHandler(new OAuth2ErrorResponseErrorHandler());

        accessTokenResponseClient.setRestOperations(restTemplate);
        return accessTokenResponseClient;
    }
}
