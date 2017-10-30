package com.quchwe.web.security.utils;


import com.quchwe.entity.SysUser;
import com.quchwe.security.AbstractTokenUtil;
import com.quchwe.service.SystemService;
import com.quchwe.web.security.model.AuthUserFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

/**
 * The type Token com.quchwe.util.
 *
 * @author zhangxd
 */
@Component
@ConfigurationProperties("com.quchwe.security.jwt")
public class TokenUtil extends AbstractTokenUtil {

    private final SystemService systemService;


    @Autowired
    public TokenUtil(SystemService systemService) {
     this.systemService = systemService;
    }

    @Override
    public UserDetails getUserDetails(String token) {
        final String username = getUsernameFromToken(token);
        SysUser user = systemService.getUserByLoginName(username);

        return AuthUserFactory.create(user);

    }

}