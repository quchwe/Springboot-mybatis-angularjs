package com.quchwe.web.controller;


import com.quchwe.entity.SysUser;
import com.quchwe.service.SystemService;
import com.quchwe.web.security.model.AuthUserFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;
import com.quchwe.security.AuthenticationTokenFilter;
import com.quchwe.web.common.controller.BaseController;
import com.quchwe.web.security.utils.TokenUtil;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * The type Authentication controller.
 *
 * @author zhangxd
 */
@RestController
@RequestMapping("/auth")
public class AuthenticationController extends BaseController {
    /**
     * 权限管理
     */
    @Autowired
    private AuthenticationManager authenticationManager;
    /**
     * 用户信息服务
     */
    @Autowired
    private UserDetailsService userDetailsService;
    /**
     * Token工具
     */
    @Autowired
    private TokenUtil jwtTokenUtil;

    @Autowired
    private SystemService systemService;

    /**
     * Create authentication token bearer auth token.
     *
     * @param sysUser the sys user
     * @return the bearer auth token
     */
    @PostMapping(value = "/token")
    public Map<String, Object> createAuthenticationToken(@RequestBody SysUser sysUser) {

        // Perform the com.quchwe.security
        final Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(sysUser.getLoginName(), sysUser.getPassword())
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        sysUser = systemService.getUserByLoginName(sysUser.getLoginName());
        sysUser.setEnabled(true);
        final UserDetails userDetails = (UserDetails) AuthUserFactory.create(sysUser);
        final String token = jwtTokenUtil.generateToken(userDetails);

        // Return the token
        Map<String, Object> tokenMap = new HashMap<>();
        tokenMap.put("access_token", token);
        tokenMap.put("expires_in", jwtTokenUtil.getExpiration());
        tokenMap.put("token_type", TokenUtil.TOKEN_TYPE_BEARER);
        return tokenMap;
    }

    /**
     * Refresh and get authentication token bearer auth token.
     *
     * @param request the request
     * @return the bearer auth token
     */
    @GetMapping(value = "/refresh")
    public Map<String, Object> refreshAndGetAuthenticationToken(HttpServletRequest request) {

        String tokenHeader = request.getHeader(AuthenticationTokenFilter.TOKEN_HEADER);
        String token = tokenHeader.split(" ")[1];

        String username = jwtTokenUtil.getUsernameFromToken(token);
        final UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        final String refreshedToken = jwtTokenUtil.generateToken(userDetails);

        Map<String, Object> tokenMap = new HashMap<>();
        tokenMap.put("access_token", refreshedToken);
        tokenMap.put("expires_in", jwtTokenUtil.getExpiration());
        tokenMap.put("token_type", TokenUtil.TOKEN_TYPE_BEARER);
        return tokenMap;
    }

    /**
     * Delete authentication token response com.quchwe.entity.
     *
     * @param request the request
     * @return the response com.quchwe.entity
     */
    @DeleteMapping(value = "/token")
    public ResponseEntity deleteAuthenticationToken(HttpServletRequest request) {

        String tokenHeader = request.getHeader(AuthenticationTokenFilter.TOKEN_HEADER);
        String token = tokenHeader.split(" ")[1];

        jwtTokenUtil.removeToken(token);

        return new ResponseEntity(HttpStatus.OK);
    }

}
