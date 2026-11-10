package org.example.filter;

import io.jsonwebtoken.Claims;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.utils.CurrentHolder;
import org.example.utils.JwtUtils;
import org.springframework.http.HttpStatus;
import lombok.extern.slf4j.Slf4j;
import java.io.IOException;



@Slf4j
@WebFilter(urlPatterns = "/*")
public class TokenFilter implements Filter {
    @Override
    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) resp;
        try {
            //1. 获取请求url。
            String url = request.getRequestURL().toString();

            //2. 判断请求url中是否包含login，如果包含，说明是登录操作，放行。
            if(url.contains("login")){ //登录请求
                log.info("登录请求 , 直接放行");
                chain.doFilter(request, response);
                return;
            }

            //3. 获取请求头中的令牌（token）。
            String jwt = request.getHeader("token");

            //4. 判断令牌是否存在，如果不存在，返回错误结果（未登录）。
            if(jwt == null || jwt.isEmpty()){ //jwt为空
                log.info("获取到jwt令牌为空, 返回错误结果");
                response.setStatus(HttpStatus.UNAUTHORIZED.value());
                return;
            }

            //5. 解析token，如果解析失败，返回错误结果（未登录）。
            try {
                Claims claims = JwtUtils.parse(jwt);
                Integer empId = Integer.valueOf(claims.get("id").toString());
                CurrentHolder.setCurrentId(empId);
                log.info("token解析成功, 放行");
            } catch (Exception e) {
                log.info("token解析失败, 响应401状态码");
                response.setStatus(401);
                return;
            }

            //6. 放行。
            log.info("令牌合法, 放行");
            chain.doFilter(request, response);
        } finally {
            // 正常、401、业务抛异常都必须清理，否则 Tomcat 线程复用时
            // 会把上一个请求的登录人带到下一个请求，日志操作人就记错了。
            CurrentHolder.remove();
        }
    }
}