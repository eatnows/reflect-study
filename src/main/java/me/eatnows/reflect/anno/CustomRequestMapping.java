package me.eatnows.reflect.anno;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

// 실행시점을 정할 수 있다.
// 컴파일 시점에 애너테이션을 읽게할 수도, 런타임 시점에 읽게할 수도 있다.
@Target(ElementType.METHOD) // 애너테이션을 어디에 걸것인지
@Retention(RetentionPolicy.RUNTIME)
public @interface CustomRequestMapping {
    String value();
}
