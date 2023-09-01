//package com.examserver.config.interceptor;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpRequest;
//import org.springframework.http.client.ClientHttpRequestExecution;
//import org.springframework.http.client.ClientHttpRequestInterceptor;
//import org.springframework.http.client.ClientHttpResponse;
//
//import java.io.IOException;
//
//public class RestTemplateInterceptor implements ClientHttpRequestInterceptor {
//
//    @Autowired
//    private JwtTokenService jwtTokenService;
//
//    @Override
//    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
//        ClientHttpResponse response = execution.execute(request, body);
//        request.getHeaders().add("Authorization","Bearer "+jwtTokenService.getJwtToken());
//        return execution.execute(request,body);
//    }
//}