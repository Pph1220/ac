package com.lhpang.ac.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.lhpang.ac.service.Imp.UserService;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.logging.Logger;

/**
 * 类路径: com.lhpang.ac.controller.controller
 * 描述: //TODO
 * @author: lhpang
 * @date: 2019-04-16 20:33
 */

@RestController
public class controller {

    @Autowired
    private UserService userService;
    @Autowired
    private Gson gson;

    @RequestMapping(value = "test",method = RequestMethod.GET)
    public String test(){

        System.out.println(gson.toJson(userService.select(1)));

        return gson.toJson(userService.select(1));
    }
}
