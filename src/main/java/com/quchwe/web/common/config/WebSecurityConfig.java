package com.quchwe.web.common.config;


import com.quchwe.common.config.AbstractWebSecurityConfig;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

/**
 * spring-security配置
 *
 * @author zhangxd
 */
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, jsr250Enabled = true)
@Configuration
public class WebSecurityConfig extends AbstractWebSecurityConfig {

    @Override
    protected void configure(HttpSecurity security) throws Exception {
        security
                .authorizeRequests()
                .antMatchers(HttpMethod.POST, "/auth/token").permitAll()
                .antMatchers(("/js/**")).permitAll()
                .antMatchers(("/css/**")).permitAll()
                .antMatchers(("/fonts/**")).permitAll()
                .antMatchers(("/img/**")).permitAll()
                .antMatchers(("/tpl/**")).permitAll()
                .antMatchers(("/vendor/**")).permitAll()
                .antMatchers(("/index.html")).permitAll();

        super.configure(security);
    }
}
