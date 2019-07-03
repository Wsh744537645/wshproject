package com.mpool.share.admin.exception;

import com.mpool.common.Result;
import com.mpool.common.exception.MpoolException;
import org.apache.shiro.ShiroException;
import org.apache.shiro.authc.AccountException;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.ExcessiveAttemptsException;
import org.apache.shiro.authz.UnauthorizedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.support.RequestContextUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.Locale;

@RestControllerAdvice
public class MpoolExceptionHandler {

	private static final Logger log = LoggerFactory.getLogger(MpoolExceptionHandler.class);
	@Autowired
//	@Qualifier("mpoolMessageSource")
	private MessageSource messageSource;

	@ExceptionHandler(RuntimeException.class)
	public Result runtimeException(RuntimeException ex) {
		Result result = new Result();
		result.setCode("500");
		result.setMsg(ex.getMessage());
		log.error(ex.getMessage(),ex);
		return result;
	}

	/**
	 * 密码重试过得
	 * 
	 * @param ex
	 * @return
	 */
	@ExceptionHandler(ExcessiveAttemptsException.class)
	public Result excessiveAttemptsException(ExcessiveAttemptsException ex) {
		log.error("QAQ shiroException ", ex);
		Result result = new Result();
		result.setCode(ExceptionCode.password_retry_too_many.getCode());
		result.setMsg(getMessage(ExceptionCode.password_retry_too_many.getCode(),
				ExceptionCode.password_retry_too_many.getMsg()));
		return result;
	}

	@ExceptionHandler(UnauthorizedException.class)
	public Result unauthorizedException(UnauthorizedException ex) {
		return getResult(ExceptionCode.auth_not_perms_oper);
	}

	@ExceptionHandler(ShiroException.class)
	public Result shiroException(ShiroException ex) {
		log.error("QAQ shiroException ", ex);
		Result result = new Result();
		String code, msg;

		if (ex instanceof AuthenticationException) {
			code = ExceptionCode.username_or_password_err.getCode();
			msg = ExceptionCode.username_or_password_err.getMsg();
		} else if (ex instanceof AccountException) {
			code = ExceptionCode.account_ex.getCode();
			msg = ExceptionCode.account_ex.getMsg();
		} else {
			code = ExceptionCode.login_ex.getCode();
			msg = ExceptionCode.login_ex.getMsg();
		}
		result.setCode(code);
		result.setMsg(getMessage(code, msg));
		return result;
	}

	@ExceptionHandler(MpoolException.class)
	public Result result(MpoolException ex) {
		log.error("QAQ Exception ", ex);
		Result result = new Result();
		result.setCode(ex.getCode());
		result.setMsg(getMessage(ex.getCode(), ex.getMsg()));
		return result;
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public Result methodArgumentNotValidException(MethodArgumentNotValidException ex) {
		log.error("QAQ param error -> ", ex);
		Result result = new Result();
		FieldError fieldError = ex.getBindingResult().getFieldError();
		if (fieldError == null) {
			result.setCode(ExceptionCode.param_ex.getCode());
			result.setMsg(getMessage(ExceptionCode.param_ex.getCode(), ExceptionCode.param_ex.getMsg()));
		} else {
			String code = fieldError.getObjectName() + "." + fieldError.getField() + "." + fieldError.getCode();
			result.setCode(code);
			result.setMsg(getMessage(code, fieldError.getDefaultMessage()));
		}
		return result;
	}

	private String getMessage(String code, String defaultMessage) {
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
				.getRequest();
		Locale locale = RequestContextUtils.getLocale(request);
		return messageSource.getMessage(code, null, defaultMessage, locale);
	}

	private Result getResult(ExceptionCode exceptionCode) {
		Result result = new Result();
		result.setCode(exceptionCode.getCode());
		result.setMsg(getMessage(exceptionCode.getCode(), exceptionCode.getMsg()));
		return result;
	}

}
