package com.quchwe.exception;


import com.quchwe.exception.base.BusinessException;

/**
 * 无效验证码
 *
 * @author zhangxd
 */
public class InvalidCaptchaException extends BusinessException {

    public InvalidCaptchaException(String message) {
        super(message);
    }

}
