package me.eatnows.reflect.config;

import lombok.extern.slf4j.Slf4j;
import me.eatnows.reflect.anno.CustomRequestMapping;
import me.eatnows.reflect.controller.UserController;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.annotation.Annotation;
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
//        for (Method method : methods) {
//            log.info(method.getName());
//            if (endPoint.equals("/" + method.getName())) {
//                try {
//                    method.invoke(userController);
//                } catch (IllegalAccessException e) {
//                    e.printStackTrace();
//                } catch (InvocationTargetException e) {
//                    e.printStackTrace();
//                }
//            }
//        }

        for (Method method : methods) {
            Annotation annotation = method.getDeclaredAnnotation(CustomRequestMapping.class);
            CustomRequestMapping requestMapping = (CustomRequestMapping) annotation;
            String mapping = requestMapping.value();

            if (mapping.equals(endPoint)) {
                try {
                    // controller에 메서드가 무슨 타입을 반환할지 모르기 떄문에 Object에서 String으로 다운 캐스팅 해주어야함
                    String path = (String) method.invoke(userController);

                    // sendRedirect와 RequestDispatcher 하는 방법이 있다.
                    // RequestDispatcher를 하면 request를 가지고 갈 수 있다.
                    RequestDispatcher dis = request.getRequestDispatcher(path);
                    // 내부에서 실행하기 때문에 필터를 타지 않기 떄문에 인덱스 페이지가 나온다.
                    dis.forward(request, response);

                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }
                break;
            }
        }

    }
}
