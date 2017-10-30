package com.quchwe.web.controller;


import com.github.pagehelper.PageInfo;
import com.quchwe.entity.SysUser;
import com.quchwe.exception.base.BusinessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import com.quchwe.service.ISystemService;
import com.quchwe.util.BaseDto;
import com.quchwe.util.Paging;
import com.quchwe.util.StringHelper;
import com.quchwe.util.WebUtils;
import com.quchwe.web.common.controller.BaseController;
import com.quchwe.web.security.model.AuthUser;

import javax.validation.Valid;

/**
 * The type Sys user controller.
 *
 * @author zhangxd
 */
@Validated
@RestController
@RequestMapping("/sys/user")
public class SysUserController extends BaseController {

    /**
     * 系统用户服务
     */
    @Autowired
    private ISystemService systemService;
    /**
     * 密码加密
     */
    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * Gets current user info.
     *
     * @return the current user info
     */
    @PreAuthorize("isAuthenticated()")
    @GetMapping(value = "/info")
    public Object getCurrentUserInfo() {

        return SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    /**
     * Save current user info response com.quchwe.entity.
     *
     * @param user the user
     * @return the response com.quchwe.entity
     */
    //@PreAuthorize("isAuthenticated()")
    @PostMapping(value = "/info")
    public ResponseEntity saveCurrentUserInfo(@Valid @RequestBody SysUser user) {

        AuthUser authUser = WebUtils.getCurrentUser();
        //只能更新当前用户信息
        if (authUser.getId().equals(user.getId())) {
            // 保存用户信息
            systemService.updateUserInfo(user);
        }

        return new ResponseEntity(HttpStatus.OK);
    }

    /**
     * Reset password response com.quchwe.entity.
     *
     * @param dto the dto
     * @return the response com.quchwe.entity
     * @throws BusinessException the business com.quchwe.exception
     */
    @PreAuthorize("isAuthenticated()")
    @PutMapping(value = "/password")
    public ResponseEntity<String> resetPassword(@RequestBody BaseDto dto) throws BusinessException {

        String oldPassword = dto.getString("oldPassword");
        String newPassword = dto.getString("newPassword");

        AuthUser user = WebUtils.getCurrentUser();

        // 重置密码
        if (StringHelper.isNotBlank(oldPassword) && StringHelper.isNotBlank(newPassword)) {

            if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
                throw new BusinessException("旧密码错误");
            }

            systemService.updateUserPasswordById(user.getId(), passwordEncoder.encode(newPassword));
        }

        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * List page info.
     *
     * @param user the user
     * @param page the page
     * @return the page info
     */
    @PreAuthorize("hasAuthority('sys:user:view')")
    @GetMapping(value = "/list")
    public PageInfo<SysUser> list(SysUser user, Paging page) {

        return systemService.findUserPage(page, user);
    }

    /**
     * Gets user.
     *
     * @param userId the user id
     * @return the user
     */
    @PreAuthorize("hasAuthority('sys:user:view')")
    @GetMapping(value = "/{userId}")
    public SysUser getUser(@PathVariable("userId") String userId) {

        return systemService.getUserById(userId);

    }

    /**
     * Save user sys user.
     *
     * @param user the user
     * @return the sys user
     */
    @PreAuthorize("hasAuthority('sys:user:edit')")
    @PostMapping(value = "")
    public SysUser saveUser(@Valid @RequestBody SysUser user) {

        String password = user.getPassword();
        //用户密码不能为空
        if (StringHelper.isNotBlank(password)) {
            user.setPassword(passwordEncoder.encode(password));
        }

        // 保存用户信息
        return systemService.saveUser(user);
    }

    /**
     * Delete response com.quchwe.entity.
     *
     * @param userId the user id
     * @return the response com.quchwe.entity
     */
    @PreAuthorize("hasAuthority('sys:user:edit')")
    @DeleteMapping(value = "/{userId}")
    public ResponseEntity delete(@PathVariable("userId") String userId) {

        systemService.deleteUserById(userId);

        return new ResponseEntity(HttpStatus.OK);
    }

}
