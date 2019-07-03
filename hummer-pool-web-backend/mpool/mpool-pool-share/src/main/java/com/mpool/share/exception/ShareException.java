package com.mpool.share.exception;

import com.mpool.common.exception.MpoolException;

/**
 * @Author: stephen
 * @Date: 2019/4/30 15:58
 */
public class ShareException extends MpoolException {
    public ShareException(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public ShareException(ExceptionCode ecode) {
        this.code = ecode.getCode();
        this.msg = ecode.getMsg();
    }
}
