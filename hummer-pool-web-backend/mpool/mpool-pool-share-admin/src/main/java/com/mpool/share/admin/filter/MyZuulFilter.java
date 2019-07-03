//package com.mpool.share.admin.filter;
//
//import com.netflix.zuul.ZuulFilter;
//import com.netflix.zuul.context.RequestContext;
//import com.netflix.zuul.exception.ZuulException;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.data.redis.core.StringRedisTemplate;
//import org.springframework.http.MediaType;
//import org.springframework.stereotype.Component;
//import org.springframework.util.StringUtils;
//
//import javax.servlet.http.HttpServletRequest;
//
///**
// * 使用过滤器判定验证码
// */
//
//@Component
//@Slf4j
//public class MyZuulFilter extends ZuulFilter {
//
//    @Autowired
//    private StringRedisTemplate redisTemplate = null;
//
//    /**
//     * 过滤器类型
//     * pre：请求执行之前 filter。
//     * route：处理请求，进行路由。
//     * post：请求处理完成后执行的 filter。
//     * error：出现错误时执行的 filter。
//     * @return
//     */
//    @Override
//    public String filterType() {
//        return "pre";
//    }
//
//    /**
//     * 指定过滤器顺序，值越小则越优先
//     * @return
//     */
//    @Override
//    public int filterOrder() {
//        return 0;
//    }
//
//    // 是否过滤
//    @Override
//    public boolean shouldFilter() {
//        //请求上下文
//        RequestContext ctx = RequestContext.getCurrentContext();
//        //获取HttpServletRequest对象
//        HttpServletRequest req = ctx.getRequest();
//        //取出表单序列号
//        String serialNumber = req.getParameter("serialNumber");
//
////        log.error("session= {}, id={}", req.getSession(), req.getSession().getId());
//
//        //如果存在验证码，则返回true，启用过滤器
//        return !StringUtils.isEmpty(serialNumber);
//    }
//
//    @Override
//    public Object run() throws ZuulException {
//        RequestContext ctx = RequestContext.getCurrentContext();
//        HttpServletRequest req = ctx.getRequest();
//        //取出表单序列号和验证码
//        String serialNumber = req.getParameter("serialNumber");
//        String reqCode = req.getParameter("verificationCode");
//        //从Redis中取出验证码
//        String verifCode = redisTemplate.opsForValue().get(serialNumber);
//        // Redis验证码为空或者与请求不一致，拦截请求报出错误
//        if(verifCode == null || !verifCode.equals(reqCode)){
//            //不再转发
//            ctx.setSendZuulResponse(false);
//            //设置http响应码为401（未授权）
//            ctx.setResponseStatusCode(401);
//            //设置响应类型为JSON数据集
//            ctx.getResponse().setContentType(MediaType.APPLICATION_JSON_UTF8.getType());
//            //设置响应体
//            ctx.setResponseBody("{'success': false, 'message':'Verification Code Error'}");
//        }
//
//        // 一致放过
//        return null;
//    }
//}
