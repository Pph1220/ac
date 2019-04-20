package com.lhpang.ac.service;

import com.lhpang.ac.common.ServerResponse;
import com.lhpang.ac.pojo.Category;

import java.util.List;

/**
*   类路径: com.lhpang.ac.service.CategoryService
*   描述: 品类Service
*   @author: lhpang
*   @date: 2019-04-17 10:15
*/
public interface CategoryService {

    /**
     * 描 述: 新增品类
     * @date: 2019-04-20 13:00
     * @author: lhpang
     * @param:
     * @return:
    **/
    ServerResponse<String> addCategory(String name, Integer parentId);
    /**
     * 描 述: 修改品类名称
     * @date: 2019-04-20 13:01
     * @author: lhpang
     * @param:
     * @return:
    **/
    ServerResponse<String> setCategoryName(Integer id, String name);
    /**
     * 描 述: 删除品类
     * @date: 2019-04-20 14:00
     * @author: lhpang
     * @param: [categoryId]
     * @return: com.lhpang.ac.common.ServerResponse<java.lang.String>
     **/
    ServerResponse<String> deleteCategory(Integer categoryId);
    /**
     * 描 述: 查询所有品类
     * @date: 2019-04-20 14:11
     * @author: lhpang
     * @param: []
     * @return: com.lhpang.ac.common.ServerResponse<java.util.List<com.lhpang.ac.pojo.Category>>
     **/
    ServerResponse<List<Category>> selectAllCategory();
    
}
