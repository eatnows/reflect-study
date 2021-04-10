package me.eatnows.reflect.controller;

import me.eatnows.reflect.anno.CustomRequestMapping;
import me.eatnows.reflect.controller.dto.JoinDto;
import me.eatnows.reflect.controller.dto.LoginDto;
import me.eatnows.reflect.model.User;

public class UserController {

    @CustomRequestMapping("/join")
    public String join(JoinDto dto) {
        System.out.println("join() 함수 호출됨");
        System.out.println(dto);
        return "/";
    }

    @CustomRequestMapping("/login")
    public String login(LoginDto dto) {
        System.out.println("join() 함수 호출됨");
        System.out.println(dto);

        return "/";
    }

    @CustomRequestMapping("/user")
    public void user() {
        System.out.println("user() 함수 호출됨");
    }

    @CustomRequestMapping("/hello")
    public String hello() {
        System.out.println("hello() 함수 호출됨");
        return "/";
    }

    @CustomRequestMapping("/list")
    public String list(User user) {
        System.out.println("list() 함수 호출됨");
        System.out.println(user);
        return "/";
    }
}
