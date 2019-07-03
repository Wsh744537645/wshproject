package com.mpool.account.config.shiro;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.SimplePrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.subject.SubjectContext;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.mpool.common.util.JWTUtil;

/**
 * @author chen token
 *
 */
@Deprecated
public class TokenWebSecurityManager extends DefaultWebSecurityManager {
	private static final Logger log = LoggerFactory.getLogger(TokenWebSecurityManager.class);

	public HttpServletRequest getCurrentRequest() throws IllegalStateException {
		ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
		if (attrs == null) {
			throw new IllegalStateException("当前线程中不存在 Request 上下文");
		}
		return attrs.getRequest();
	}

	public HttpServletResponse getCurrentResponse() throws IllegalStateException {
		ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
		if (attrs == null) {
			throw new IllegalStateException("当前线程中不存在 Request 上下文");
		}
		return attrs.getResponse();
	}

	@Override
	protected SubjectContext resolvePrincipals(SubjectContext context) {
		HttpServletRequest request = getCurrentRequest();
		PrincipalCollection principals = null;
		String token = request.getHeader("token");
		boolean verify = JWTUtil.verify(token);
		log.debug(" resolvePrincipals token={}", token);
		log.debug("token verify {}", verify);
		if (verify) {
			Map<String, String> tokenValue = JWTUtil.getTokenValue(token);
			if (tokenValue != null) {
				SimplePrincipalCollection principalCollection = new SimplePrincipalCollection();
				principalCollection.add(tokenValue.get("username"), tokenValue.get("roleId"));
				log.debug("username={},roleId={}", tokenValue.get("username"), tokenValue.get("roleId"));
				principals = principalCollection;
			}
		}
		context.setPrincipals(principals);
		// TODO Auto-generated method stub
		return context;
	}

	@Override
	public Subject createSubject(SubjectContext subjectContext) {

		subjectContext = ensureSecurityManager(subjectContext);

		subjectContext = resolveSession(subjectContext);

		subjectContext = resolvePrincipals(subjectContext);
		Subject subject = doCreateSubject(subjectContext);

		return subject;
	}
}
