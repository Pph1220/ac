package com.lhpang.ac.service.imp;

import com.lhpang.ac.common.ServerResponse;
import com.lhpang.ac.dao.CategoryMapper;
import com.lhpang.ac.pojo.Category;
import com.lhpang.ac.service.CategoryService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;

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
    public synchronized ServerResponse<String> addCategory(String name, Integer parentId){

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
    /**
     * 描 述: 修改分类名称
     * @date: 2019-04-20 12:22
     * @author: lhpang
     * @param: [id, name]
     * @return: com.lhpang.ac.common.ServerResponse<java.lang.String>
     **/
    @Override
    public synchronized ServerResponse<String> setCategoryName(Integer id, String name){

        if (id == null || StringUtils.isBlank(name)){
            return ServerResponse.createByErrorMessage("参数错误");
        }
        Category category = categoryMapper.selectByPrimaryKey(id);
        if (category ==null ){
            return ServerResponse.createByErrorMessage("类目不存在");
        }
        category.setName(name);
        category.setUpdateTime(new Timestamp(System.currentTimeMillis()));
        int count = categoryMapper.updateByPrimaryKeySelective(category);

        if (count > 0){
            return ServerResponse.createBySuccessMessage("修改成功");
        }
        return ServerResponse.createByErrorMessage("修改失败");
    }
    /**
     * 描 述: 删除品类
     * @date: 2019-04-20 14:00
     * @author: lhpang
     * @param: [categoryId]
     * @return: com.lhpang.ac.common.ServerResponse<java.lang.String>
     **/
    @Override
    public synchronized ServerResponse<String> deleteCategory(Integer categoryId){

        if (categoryId == null){
            return ServerResponse.createByErrorMessage("参数错误");
        }
        Category category = categoryMapper.selectByPrimaryKey(categoryId);
        //如果没有这个品类或者这个品类已被删除返回"类目不存在"
        if (category ==null || !category.getStatus()){
            return ServerResponse.createByErrorMessage("类目不存在");
        }
        category.setStatus(false);
        category.setUpdateTime(new Timestamp(System.currentTimeMillis()));
        int count = categoryMapper.updateByPrimaryKeySelective(category);

        if (count > 0){
            return ServerResponse.createBySuccessMessage("删除成功");
        }
        return ServerResponse.createByErrorMessage("删除失败");
    }
    /**
     * 描 述: 查询所有品类
     * @date: 2019-04-20 14:11
     * @author: lhpang
     * @param: []
     * @return: com.lhpang.ac.common.ServerResponse<java.util.List<com.lhpang.ac.pojo.Category>>
     **/
    @Override
    public ServerResponse<List<Category>> selectAllCategory(){

        List<Category> list = categoryMapper.selectAllCategory();

        return ServerResponse.createBySuccess(list);
    }
    
}
