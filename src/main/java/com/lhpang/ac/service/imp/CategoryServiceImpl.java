package com.lhpang.ac.service.imp;

import com.lhpang.ac.common.ServerResponse;
import com.lhpang.ac.dao.CategoryMapper;
import com.lhpang.ac.pojo.Category;
import com.lhpang.ac.service.CategoryService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;

/**
*   类路径: com.lhpang.ac.service.imp.CategoryService
*   描述: //TODO 
*   @author: lhpang
*   @date: 2019-04-17 10:20
*/
@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryMapper categoryMapper;

    /**
     * 描 述: 新增品类
     * @date: 2019-04-19 16:47
     * @author: lhpang
     * @param: [name, parentId]
     * @return: com.lhpang.ac.common.ServerResponse<java.lang.String>
     **/
    @Override
    public ServerResponse<String> addCategory(String name, Integer parentId){

        if(StringUtils.isBlank(name) || parentId == null){
            return ServerResponse.createByErrorMessage("添加品类参数错误");
        }

        Category category = new Category();

        category.setName(name);
        category.setParentId(parentId);
        category.setStatus(true);
        category.setCreateTime(new Timestamp(System.currentTimeMillis()));

        int count = categoryMapper.insertSelective(category);
        if(count > 0){
            return ServerResponse.createBySuccessMessage("添加成功");
        }
        return ServerResponse.createByErrorMessage("添加失败");
    }

    public ServerResponse<String> setCategoryName(Integer id, String name){


    }
    
}
