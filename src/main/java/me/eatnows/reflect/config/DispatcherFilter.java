package me.eatnows.reflect.config;

import lombok.extern.slf4j.Slf4j;
import me.eatnows.reflect.controller.UserController;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

@Slf4j
@Configuration
public class DispatcherFilter implements Filter {

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        String endPoint = request.getRequestURI().substring(request.getContextPath().length());

        log.info("엔드 포인트 : " + endPoint);

        UserController userController = new UserController();

        // 리플렉션 -> 메서드를 런타임 시점에서 찾아내어 실행
        Method[] methods = userController.getClass().getDeclaredMethods();
        for (Method method : methods) {
            log.info(method.getName());
            if (endPoint.equals("/" + method.getName())) {
                try {
                    method.invoke(userController);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
        }

    }
}
