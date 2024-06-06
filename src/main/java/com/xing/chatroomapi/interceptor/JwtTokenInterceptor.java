package com.xing.chatroomapi.interceptor;

import com.xing.chatroomapi.constant.MessageConstant;
import com.xing.chatroomapi.util.BaseContext;
import com.xing.chatroomapi.util.JwtUtil;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Author:WangXing
 * @DATE:2024/4/28
 */
@Component
@Slf4j
@SuppressWarnings("all")
public class JwtTokenInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {


        //判断当前拦截到的是Controller的方法还是其他资源
        if (!(handler instanceof HandlerMethod)) {
            //当前拦截到的不是动态方法，直接放行
            return true;
        }
        String token = request.getHeader(MessageConstant.TOKEN_HEADER);
        //2、校验令牌
        try {
            log.info("jwt校验:{}", token);
            Claims claims = JwtUtil.parseJWT(MessageConstant.SECRETE_KEY, token);

            //解析出User
            Integer userId = claims.get("user", Integer.class);
            log.info("userId======================>{}",userId);
            //放到ThreadLocal中
            BaseContext.setCurrentUser(userId);

            return true;
        } catch (Exception ex) {
            //4、不通过，响应401状态码
            log.info("token验证失败");
            response.setCharacterEncoding("UTF-8");
            response.setStatus(401);
            response.getWriter().write("{code:401,msg:'身份验证失败!'}");
            return false;
        }
    }
}
