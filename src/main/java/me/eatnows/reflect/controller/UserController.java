package me.eatnows.reflect.controller;

import me.eatnows.reflect.anno.CustomRequestMapping;

public class UserController {

    @CustomRequestMapping("/join")
    public void join() {
        System.out.println("join() 함수 호출됨");
    }

    @CustomRequestMapping("/login")
    public void login() {
        System.out.println("join() 함수 호출됨");
    }

    @CustomRequestMapping("/user")
    public void user() {
        System.out.println("user() 함수 호출됨");
    }

    @CustomRequestMapping("/hello")
    public void hello() {
        System.out.println("hello() 함수 호출됨");
    }
}
