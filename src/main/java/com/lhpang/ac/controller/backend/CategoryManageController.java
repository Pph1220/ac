package com.lhpang.ac.controller.backend;

import com.lhpang.ac.common.Constant;
import com.lhpang.ac.common.ResponseCode;
import com.lhpang.ac.common.ServerResponse;
import com.lhpang.ac.pojo.User;
import com.lhpang.ac.service.CategoryService;
import com.lhpang.ac.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

/**
*   类路径: com.lhpang.ac.controller.backend.CategoryManageController
*   描述: 分类Controller
*   @author: lhpang
*   @date: 2019-04-19 16:15
*/
@RestController("/categorymanage/")
public class CategoryManageController {

    @Autowired
    private CategoryService categoryService;
    @Autowired
    private UserService userService;

    /**
     * 描 述: 新增品类
     * @date: 2019-04-19 17:31
     * @author: lhpang
     * @param: [session, categoryName, parentId]
     * @return: com.lhpang.ac.common.ServerResponse<java.lang.String>
     **/
    @RequestMapping(value = "addCategory",method = RequestMethod.GET)
    public ServerResponse<String> addCategory(HttpSession session,String categoryName,@RequestParam(value = "parentId",defaultValue = "0") int parentId){
        User user = (User)session.getAttribute(Constant.CURRENT_USER);

        //判断是否为登陆状态
        ServerResponse isOnLine = userService.isOnLine(user);
        if(!isOnLine.isSuccess()){
            return isOnLine;
        }
        //判断是否为管理员
        ServerResponse checkAdminRole = userService.checkAdminRole(user);
        if(!checkAdminRole.isSuccess()){
            return ServerResponse.createByErrorMessage("没有操作权限");
        }
        return categoryService.addCategory(categoryName, parentId);
    }
    @RequestMapping(value = "setCategoryName",method = RequestMethod.GET)
    public ServerResponse<String> setCategoryName(HttpSession session,int id,String newName){
        User user = (User)session.getAttribute(Constant.CURRENT_USER);

        //判断是否为登陆状态
        ServerResponse isOnLine = userService.isOnLine(user);
        if(!isOnLine.isSuccess()){
            return isOnLine;
        }
        ServerResponse response = userService.checkAdminRole(user);
        if(!response.isSuccess()){
            return ServerResponse.createByErrorMessage("没有操作权限");
        }
        int count categoryService.

    }
    
}
