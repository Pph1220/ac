package com.lhpang.ac.controller.backend;

import com.github.pagehelper.PageInfo;
import com.google.common.collect.Maps;
import com.lhpang.ac.common.Constant;
import com.lhpang.ac.common.ServerResponse;
import com.lhpang.ac.pojo.Product;
import com.lhpang.ac.pojo.User;
import com.lhpang.ac.service.ProductService;
import com.lhpang.ac.service.UserService;
import com.lhpang.ac.vo.ProductDetailVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import java.util.Map;

/**
*   类路径: com.lhpang.ac.controller.backend.ProductManagerController
*   描述: 后台商品Controller
*   @author: lhpang
*   @date: 2019-04-22 15:05
*/
@Controller
@RequestMapping("/productManager/")
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
    @PostMapping("saveOrUpdateProduct")
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
    @PostMapping("setStatus")
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
    public ModelAndView getProductDetail(HttpSession session, Integer productId){

        Map map = Maps.newHashMap();
        User user = (User)session.getAttribute(Constant.CURRENT_USER);

        ServerResponse response = userService.checkRoleAndOnLine(user);
        if(!response.isSuccess()){
            map.put("result", response);
            return new ModelAndView("common/fail",map);
        }
        map.put("result",productService.managerProductDetail(productId));
        return new ModelAndView("backend/product/productDetail",map);
    }
    /**
     * 描 述: 所有商品
     * @date: 2019/4/22 23:15
     * @author: lhpang
     * @param:
     * @return: com.lhpang.ac.common.ServerResponse<com.github.pagehelper.PageInfo>
     **/
    @ResponseBody
    @RequestMapping(value = "getProductList",method = RequestMethod.GET)
    public ModelAndView getProductList(HttpSession session, @RequestParam(value = "pageNum",
            defaultValue = "1") int pageNum, @RequestParam(value = "pageSize",defaultValue = "10") int pageSize){
        Map map = Maps.newHashMap();
        User user = (User)session.getAttribute(Constant.CURRENT_USER);

        ServerResponse response = userService.checkRoleAndOnLine(user);
        if(!response.isSuccess()){
            map.put("result", response);
            return new ModelAndView("common/fail",map);
        }
        map.put("result",productService.getProductList(pageNum,pageSize));
        return new ModelAndView("/backend/product/productList",map);
    }
    /**
     * 描 述: 搜索商品
     * @date: 2019-04-23 9:45
     * @author: lhpang
     * @param: [session, productName, pageNum, pageSize]
     * @return: com.lhpang.ac.common.ServerResponse<com.github.pagehelper.PageInfo>
     **/
    @ResponseBody
    @RequestMapping(value = "searchProduct",method = RequestMethod.GET)
    public ServerResponse<PageInfo> searchProduct(HttpSession session,String productName, @RequestParam(value =
            "pageNum",
            defaultValue = "1") int pageNum, @RequestParam(value = "pageSize",defaultValue = "10") int pageSize){

        User user = (User)session.getAttribute(Constant.CURRENT_USER);

        ServerResponse response = userService.checkRoleAndOnLine(user);
        if(!response.isSuccess()){
            return response;
        }
        return productService.searchProduct(productName,pageNum,pageSize);
    }
    /**
     * 描 述: 删除商品
     * @date: 2019/5/6 12:04
     * @author: lhpang
     * @param: [session, productId]
     * @return: com.lhpang.ac.common.ServerResponse
     **/
    @ResponseBody
    @PostMapping("deleteProduct")
    public ServerResponse deleteProduct(HttpSession session,Integer productId){

        User user = (User)session.getAttribute(Constant.CURRENT_USER);

        ServerResponse response = userService.checkRoleAndOnLine(user);
        if(!response.isSuccess()){
            return response;
        }
        return productService.delete(productId);
    }
}
