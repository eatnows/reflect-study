package me.eatnows.reflect.config;

import me.eatnows.reflect.controller.UserController;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Configuration
public class DispatcherFilter implements Filter {

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        String endPoint = request.getRequestURI().substring(request.getContextPath().length());

        System.out.println(endPoint);

        UserController userController = new UserController();
        if (endPoint.equals("/join")) {
            userController.join();
        } else if (endPoint.equals("/login")) {
            userController.login();
        } else if (endPoint.equals("/user")) {
            userController.user();
        }

    }
}
