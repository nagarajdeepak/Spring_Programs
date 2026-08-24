package com.example.aop;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

import com.example.aop.service.UserService;

@ComponentScan("com.example.aop")
@EnableAspectJAutoProxy
public class App {

    public static void main(String[] args) {

        AnnotationConfigApplicationContext context =
                new AnnotationConfigApplicationContext(App.class);

        UserService userService = context.getBean(UserService.class);

        userService.addUser();

        context.close();
    }
}
