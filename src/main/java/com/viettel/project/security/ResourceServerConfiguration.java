package com.viettel.project.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

import java.io.*;
import java.util.stream.Collectors;

@SuppressWarnings(value="deprecation")
@Configuration
@EnableResourceServer
public class ResourceServerConfiguration extends ResourceServerConfigurerAdapter {
    @Override
    public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
        resources.tokenStore(getTokenStore());
    }

    @Bean
    public TokenStore getTokenStore(){
        return new JwtTokenStore(getTokenConverter());
    }

    @Bean
    public JwtAccessTokenConverter getTokenConverter() {
        JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
        String verifyKey = "";
        Resource resource = new ClassPathResource("publickey.txt");
        Reader reader = null;
        BufferedReader bufferedReader = null;
        try {
            reader = new InputStreamReader(resource.getInputStream());
            bufferedReader = new BufferedReader(reader);
        } catch (IOException e) {
            e.printStackTrace();
        }
//        BufferedReader reader = new BufferedReader();
        verifyKey = bufferedReader.lines().collect(Collectors.joining("\n"));
        converter.setVerifierKey(verifyKey);
        return converter;
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests().antMatchers("/actuator/**", "/swagger-ui/**", "/v3-/api-docs/**").permitAll();
    }
}
