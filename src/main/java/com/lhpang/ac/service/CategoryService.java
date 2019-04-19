package com.lhpang.ac.service;

import com.lhpang.ac.common.ServerResponse;

/**
*   类路径: com.lhpang.ac.service.CategoryService
*   描述: 品类Service
*   @author: lhpang
*   @date: 2019-04-17 10:15
*/
public interface CategoryService {

    ServerResponse<String> addCategory(String name, Integer parentId);
    
}
