package com.springcloud.zuul_1;

import org.springframework.cloud.netflix.zuul.filters.route.FallbackProvider;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * 熔断配置
 * @author jmfen
 * date 2020-05-12
 */

@Component
public class ZuulFallBack implements FallbackProvider {
    @Override
    public String getRoute() {
        //作用于所有被zuul代理的服务
        return "*";
    }

    @Override
    public ClientHttpResponse fallbackResponse(String route, Throwable cause) {
        return new ClientHttpResponse() {
            @Override
            public HttpStatus getStatusCode() throws IOException {
                return HttpStatus.BAD_REQUEST;
            }

            @Override
            public int getRawStatusCode() throws IOException {
                return 400;
            }

            @Override
            public String getStatusText() throws IOException {
                return null;
            }

            @Override
            public void close() {

            }

            @Override
            public InputStream getBody() throws IOException {
                //如果熔断了展现出什么信息
                byte[] bytes = "your serve has boom".getBytes("UTF-8");
                return new ByteArrayInputStream(bytes);
            }

            @Override
            public HttpHeaders getHeaders() {
                //添加头信息，必须要添加，不添加则熔断失败
                HttpHeaders httpHeaders=new HttpHeaders();
                httpHeaders.add("reason","theboom");
                return httpHeaders;
            }
        };
    }
}