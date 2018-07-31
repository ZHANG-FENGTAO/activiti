package com.example.activiti.helloword;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author zft
 * @date 2018/6/5.
 */
@RestController
@RequestMapping("/api/test")
public class HelloWorld {

    @RequestMapping("/hi")
    public String test(){

        return "hello world";
    }


}
