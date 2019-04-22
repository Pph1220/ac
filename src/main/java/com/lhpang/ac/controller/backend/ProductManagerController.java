package com.lhpang.ac.controller.backend;

import com.lhpang.ac.common.Constant;
import com.lhpang.ac.common.ResponseCode;
import com.lhpang.ac.common.ServerResponse;
import com.lhpang.ac.pojo.Product;
import com.lhpang.ac.pojo.User;
import com.lhpang.ac.service.ProductService;
import com.lhpang.ac.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.net.ssl.HttpsURLConnection;
import javax.servlet.http.HttpSession;

/**
*   类路径: com.lhpang.ac.controller.backend.ProductManagerController
*   描述: 后台商品Controller
*   @author: lhpang
*   @date: 2019-04-22 15:05
*/
@Controller
@RequestMapping("/productmanager/")
public class ProductManagerController {

    @Autowired
    private ProductService productService;
    @Autowired
    private UserService userService;

    /**
     * 描 述: 修改或新增商品
     * @date: 2019-04-22 16:38
     * @author: lhpang
     * @param: [session, product]
     * @return: com.lhpang.ac.common.ServerResponse<java.lang.String>
     **/
    @ResponseBody
    @RequestMapping(value = "saveOrUpdateProduct",method = RequestMethod.GET)
    public ServerResponse<String> saveOrUpdateProduct(HttpSession session , Product product){
        User user = (User)session.getAttribute(Constant.CURRENT_USER);

        ServerResponse response = userService.checkRoleAndOnLine(user);
        if(!response.isSuccess()){
            return response;
        }

        return productService.saveOrUpdateProduct(product);
    }
    /**
     * 描 述: 修改商品上下架状态
     * @date: 2019-04-22 17:07
     * @author: lhpang
     * @param: [session, productId]
     * @return: com.lhpang.ac.common.ServerResponse<java.lang.String>
     **/
    @ResponseBody
    @RequestMapping(value = "setStatus",method = RequestMethod.GET)
    public ServerResponse<String> setStatus(HttpSession session,Integer productId){
        User user = (User)session.getAttribute(Constant.CURRENT_USER);

        ServerResponse response = userService.checkRoleAndOnLine(user);
        if(!response.isSuccess()){
            return response;
        }


        return productService.setStatus(productId);
    }
    /**
     * 描 述: 获取商品详情
     * @date: 2019-04-22 17:33
     * @author: lhpang
     * @param: [session, productId]
     * @return: com.lhpang.ac.common.ServerResponse<com.lhpang.ac.pojo.Product>
     **/
    @ResponseBody
    @RequestMapping(value = "getProductDetail",method = RequestMethod.GET)
    public ServerResponse<Product> getProductDetail(HttpSession session,Integer productId){
        User user = (User)session.getAttribute(Constant.CURRENT_USER);

        ServerResponse response = userService.checkRoleAndOnLine(user);
        if(!response.isSuccess()){
            return response;
        }


    }
    
}
