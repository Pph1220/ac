package com.lhpang.ac.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * 类路径: com.lhpang.ac.controller.controller
 * 描述: //TODO
 * @author: lhpang
 * @date: 2019-04-16 20:33
 */

@RestController
public class controller {

    @RequestMapping(value = "test",method = RequestMethod.GET)
    public String test(){

        return "hello";
    }
}
