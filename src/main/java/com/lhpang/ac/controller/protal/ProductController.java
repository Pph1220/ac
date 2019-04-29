package com.lhpang.ac.controller.protal;

import com.google.common.collect.Maps;
import com.lhpang.ac.common.Constant;
import com.lhpang.ac.common.ServerResponse;
import com.lhpang.ac.pojo.User;
import com.lhpang.ac.service.ProductService;
import com.lhpang.ac.service.UserService;
import com.lhpang.ac.vo.ProductDetailVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import java.util.Map;

/**
*   类路径: com.lhpang.ac.controller.protal.ProductController
*   描述: 商品前台接口
*   @author: lhpang
*   @date: 2019-04-23 10:08
*/
@Controller
@RequestMapping("/product/")
public class ProductController {

    @Autowired
    private ProductService productService;
    @Autowired
    private UserService userService;


    /**
     * 描 述: 获得商品信息详情(前台)
     * @date: 2019-04-23 10:18
     * @author: lhpang
     * @param: [productId]
     * @return: com.lhpang.ac.common.ServerResponse<com.lhpang.ac.vo.ProductDetailVo>
     **/
    @ResponseBody
    @RequestMapping(value = "detail",method = RequestMethod.GET)
    public ModelAndView detail(HttpSession session,Integer productId){
        Map<String,Object> map = Maps.newHashMap();
        User user = (User) session.getAttribute(Constant.CURRENT_USER);
        ServerResponse response = userService.checkOnLine(user);
        if(!response.isSuccess()){
            map.put("result", response);
            return new ModelAndView("common/fail",map);
        }
        map.put("result", productService.getProductDetail(productId));
        return new ModelAndView("portal/product/productDetail",map);
    }
    /**
     * 描 述: 搜索(前台)
     * @date: 2019-04-23 10:57
     * @author: lhpang
     * @param: [productName, pageSize, pageNum, orderBy]
     * @return: com.lhpang.ac.common.ServerResponse<com.github.pagehelper.PageInfo>
     **/
    @RequestMapping(value = "search",method = RequestMethod.GET)
    public ModelAndView search(HttpSession session,
                                           @RequestParam(value = "productName" ,required = false) String productName,
                                           @RequestParam(value = "categoryId",required = false) Integer categoryId,
                                           @RequestParam(value = "pageNum",defaultValue = "1") int pageNum,
                                           @RequestParam(value = "pageSize",defaultValue = "10") int pageSize,
                                           @RequestParam(value = "orderBy",defaultValue = "") String orderBy){
        Map<String,Object> map = Maps.newHashMap();
        User user = (User) session.getAttribute(Constant.CURRENT_USER);
        ServerResponse response = userService.checkOnLine(user);
        if(!response.isSuccess()){
            map.put("result",response);
            return new ModelAndView("common/fail",map);
        }
        map.put("result", productService.getProductByproductNameCategoryId(productName,categoryId, pageSize, pageNum, orderBy));
        return new ModelAndView("portal/product/productList",map);
    }

    /**
     * 描 述: 所有商品
     * @date: 2019/4/22 23:15
     * @author: lhpang
     * @param:
     * @return: com.lhpang.ac.common.ServerResponse<com.github.pagehelper.PageInfo>
     **/
    @RequestMapping(value = "getProductList",method = RequestMethod.GET)
    public ModelAndView getProductList(HttpSession session, @RequestParam(value = "pageNum",
            defaultValue = "1") int pageNum, @RequestParam(value = "pageSize",defaultValue = "10") int pageSize){
        Map<String,Object> map = Maps.newHashMap();
        User user = (User)session.getAttribute(Constant.CURRENT_USER);

        ServerResponse response = userService.checkOnLine(user);
        if(!response.isSuccess()){
            map.put("result",response);
            return new ModelAndView("common/fail",map);
        }
        map.put("result", productService.getProductList(pageNum, pageSize));
        return new ModelAndView("portal/product/productList",map);
    }

}
