package com.lhpang.ac.controller.backend;

import com.lhpang.ac.common.Constant;
import com.lhpang.ac.common.ServerResponse;
import com.lhpang.ac.pojo.Category;
import com.lhpang.ac.pojo.User;
import com.lhpang.ac.service.CategoryService;
import com.lhpang.ac.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * 类路径: com.lhpang.ac.controller.backend.CategoryManageController
 * 描述: 分类Controller
 *
 * @author: lhpang
 * @date: 2019-04-19 16:15
 */
@RestController
@RequestMapping("/categorymanage/")
public class CategoryManageController {

    @Autowired
    private CategoryService categoryService;
    @Autowired
    private UserService userService;

    /**
     * 描 述: 新增品类
     *
     * @date: 2019-04-19 17:31
     * @author: lhpang
     * @param: [session, categoryName, parentId]
     * @return: com.lhpang.ac.common.ServerResponse<java.lang.String>
     **/
    @RequestMapping(value = "addCategory", method = RequestMethod.GET)
    public ServerResponse<String> addCategory(HttpSession session, String categoryName, @RequestParam(value = "parentId", defaultValue = "0") int parentId) {
        User user = (User) session.getAttribute(Constant.CURRENT_USER);

        ServerResponse response = userService.checkRoleAndOnLine(user);
        if (!response.isSuccess()) {
            return response;
        }

        return categoryService.addCategory(categoryName, parentId);
    }

    /**
     * 描 述: 修改品类名称
     *
     * @date: 2019-04-20 14:01
     * @author: lhpang
     * @param: [session, categoryId, newName]
     * @return: com.lhpang.ac.common.ServerResponse<java.lang.String>
     **/
    @RequestMapping(value = "setCategoryName", method = RequestMethod.GET)
    public ServerResponse<String> setCategoryName(HttpSession session, Integer categoryId, String newName) {
        User user = (User) session.getAttribute(Constant.CURRENT_USER);

        ServerResponse response = userService.checkRoleAndOnLine(user);
        if (!response.isSuccess()) {
            return response;
        }

        return categoryService.setCategoryName(categoryId, newName);
    }

    /**
     * 描 述: 删除品类
     *
     * @date: 2019-04-20 14:01
     * @author: lhpang
     * @param: [session, categoryId]
     * @return: com.lhpang.ac.common.ServerResponse<java.lang.String>
     **/
    @RequestMapping(value = "deleteCategory", method = RequestMethod.GET)
    public ServerResponse<String> deleteCategory(HttpSession session, Integer categoryId) {

        User user = (User) session.getAttribute(Constant.CURRENT_USER);

        ServerResponse response = userService.checkRoleAndOnLine(user);
        if (!response.isSuccess()) {
            return response;
        }

        return categoryService.deleteCategory(categoryId);
    }

    /**
     * 描 述: 查询所有品类
     *
     * @date: 2019-04-20 14:20
     * @author: lhpang
     * @param: []
     * @return: com.lhpang.ac.common.ServerResponse<java.util.List < com.lhpang.ac.pojo.Category>>
     **/
    @RequestMapping(value = "selectAllCategory", method = RequestMethod.GET)
    public ServerResponse<List<Category>> selectAllCategory() {

        return categoryService.selectAllCategory();
    }

}
