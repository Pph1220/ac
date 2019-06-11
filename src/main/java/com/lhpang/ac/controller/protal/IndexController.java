package com.lhpang.ac.controller.protal;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 类路径: com.lhpang.ac.controller.protal.IndexController
 * 描述: //TODO
 *
 * @author: lhpang
 * @date: 2019-04-28 17:24
 */
@Controller
public class IndexController {

    @RequestMapping("/")
    public String index() {
        return "index";
    }
}
