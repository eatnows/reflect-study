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
import java.io.PrintWriter;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Enumeration;

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

        boolean isMatching = false;

        for (Method method : methods) {
            Annotation annotation = method.getDeclaredAnnotation(CustomRequestMapping.class);
            CustomRequestMapping requestMapping = (CustomRequestMapping) annotation;
            String mapping = requestMapping.value();

            if (mapping.equals(endPoint)) {
                isMatching = true;
                try {

                    System.out.println("엔드포인트 same");
                    Parameter[] params = method.getParameters();
                    String path = null;

                    if (params.length != 0) {
                        System.out.println("파라미터가 있다.");
                        // 파라미터가 여러개면 for문 돌려야함
//                        System.out.println("params[0] : " + params[0]);
//                        System.out.println("params[0].getName() : " + params[0].getName());
//                        System.out.println("params[0].getType() : " + params[0].getType());

                        // Class.newInstance()는 deprecated됨
                        Object dtoInstance = params[0].getType().getDeclaredConstructor().newInstance();

//                        String email = request.getParameter("email");
//                        String password = request.getParameter("password");
//                        System.out.println("email : " + email);
//                        System.out.println("password : " + password);

                        setData(dtoInstance, request);

                        path = (String) method.invoke(userController, dtoInstance);
                    } else {
                        // controller에 메서드가 무슨 타입을 반환할지 모르기 떄문에 Object에서 String으로 다운 캐스팅 해주어야함
                        path = (String) method.invoke(userController);
                    }


                    // sendRedirect와 RequestDispatcher 하는 방법a이 있다.
                    // RequestDispatcher를 하면 request를 가지고 갈 수 있다.
                    RequestDispatcher dis = request.getRequestDispatcher(path);
                    // 내부에서 실행하기 때문에 필터를 타지 않기 떄문에 인덱스 페이지가 나온다.
                    // return sendRedirect는 필터를 지나면서 그 기반으로 톰캣으로 request, response를 만들어준다.
                    // RequestDispatcher는 request와 response를 다시 만드는 것이 아니라 들고 있는것으로 덮어씌우기 떄문에 다시 톰캣을 타지 않고 내부적으로 동작한다.
                    dis.forward(request, response);

                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                } catch (InstantiationException e) {
                    e.printStackTrace();
                }
                break;
            }

        }
        if (!isMatching) {
            // 같은 주소가 없으면
            response.setContentType("text/html; charset=utf-8");
            PrintWriter out = response.getWriter();
            out.println("잘못된 주소 요청입니다. 404 에러");
            out.flush();
        }
    }

    private <T> void setData(T instance, HttpServletRequest request) {
        Enumeration<String> keys = request.getParameterNames(); // parameter들의 key값들이 들어있음
        // keys 값을 변형 email -> setEmail
        // keys 값을 변형 password -> setPassword

        while (keys.hasMoreElements()) {
            String key = keys.nextElement();
            String methodKey = keyToMethodKey(key);

            Method[] methods = instance.getClass().getDeclaredMethods();
            for (Method method : methods) {
                if (method.getName().equals(methodKey)) {
                    try {

                        Object parameter = request.getParameter(key);
                        // String 값으로 넘어오기 떄문에 숫자형 setter일 경우 변환해주어야 한다.
                        // 파라미터가 여러개일 경우 for문을 돌려야 한다.
                        if (method.getParameterTypes()[0] == int.class) {
                            parameter = Integer.parseInt((String) parameter);
                        }

                        method.invoke(instance, parameter);
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

    private String keyToMethodKey(String key) {
        String firstKey = "set";
        String upperKey = key.substring(0, 1).toUpperCase();
        String remainKey = key.substring(1);

        return firstKey + upperKey + remainKey;
    }
}
