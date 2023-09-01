//package com.examserver.filter;
//
//import org.springframework.stereotype.Component;
//import org.springframework.web.servlet.HandlerInterceptor;
//
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//
//@Component
//public class JwtTokenInterceptor implements HandlerInterceptor {
//
//    @Override
//    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
//        String token = request.getHeader("Authorization");
//
//        return true;
//
//      //  boolean isValidToken = validateToken(token);
//
////        if (isValidToken) {
////            return true;
////        } else {
////            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
////            return false;
////        }
////    }
//
//    }
//}
//
