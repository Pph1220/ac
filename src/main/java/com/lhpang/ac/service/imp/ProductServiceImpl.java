package com.lhpang.ac.service.imp;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.lhpang.ac.common.Constant;
import com.lhpang.ac.common.ResponseCode;
import com.lhpang.ac.common.ServerResponse;
import com.lhpang.ac.dao.CategoryMapper;
import com.lhpang.ac.dao.ProductMapper;
import com.lhpang.ac.pojo.Category;
import com.lhpang.ac.pojo.Product;
import com.lhpang.ac.service.ProductService;
import com.lhpang.ac.utils.DateUtil;
import com.lhpang.ac.vo.ProductDetailVo;
import com.lhpang.ac.vo.ProductListVo;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.ResponseBody;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * 类路径: com.lhpang.ac.service.imp.ProductService
 * 描述: 产品ServiceImpl
 *
 * @author: lhpang
 * @date: 2019-04-17 10:18
 */
@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductMapper productMapper;
    @Autowired
    private CategoryMapper categoryMapper;

    /**
     * 描 述: 商品新增或修改(后台)
     *
     * @date: 2019-04-22 16:36
     * @author: lhpang
     * @param: [product]
     * @return: com.lhpang.ac.common.ServerResponse<java.lang.String>
     **/
    @Override
    @Transactional(rollbackFor = Exception.class)
    public synchronized ServerResponse<String> saveOrUpdateProduct(Product product) {

        if (product != null) {
            if (product.getId() != null) {
                int count = productMapper.updateByPrimaryKeySelective(product);
                if (count > 0) {
                    return ServerResponse.createBySuccessMessage("更新成功");
                }
                return ServerResponse.createByErrorMessage("更新失败");
            } else {
                product.setCreateTime(new Timestamp(System.currentTimeMillis()));
                int count = productMapper.insertSelective(product);
                if (count > 0) {
                    return ServerResponse.createBySuccessMessage("新增成功");
                }
                return ServerResponse.createByErrorMessage("新增失败");
            }
        }
        return ServerResponse.createByErrorMessage("参数错误");
    }

    /**
     * 描 述: 修改上下架状态(后台)
     *
     * @date: 2019-04-22 16:43
     * @author: lhpang
     * @param: [id]
     * @return: com.lhpang.ac.common.ServerResponse<java.lang.String>
     **/
    @Override
    @Transactional
    public synchronized ServerResponse<String> setStatus(Integer productId) {

        if (productId == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(), ResponseCode.ILLEGAL_ARGUMENT.getDesc());
        }
        //判断是否有这个产品
        Product product = productMapper.selectByPrimaryKey(productId);
        Product updateProduct = new Product();
        updateProduct.setId(productId);

        if (product == null) {
            return ServerResponse.createByErrorMessage("商品不存在");
        } else {
            //如果为在下架改为在售
            if (product.getStatus() == Constant.ProductStatusEnum.OFF_SALE.getCode()) {
                updateProduct.setStatus(Constant.ProductStatusEnum.ON_SALE.getCode());
                int count = productMapper.updateByPrimaryKeySelective(updateProduct);
                if (count > 0) {
                    return ServerResponse.createBySuccessMessage("上架成功");
                }
            }
            //如果为在售改为下架
            if (product.getStatus() == Constant.ProductStatusEnum.ON_SALE.getCode()) {
                updateProduct.setStatus(Constant.ProductStatusEnum.OFF_SALE.getCode());
                int count = productMapper.updateByPrimaryKeySelective(updateProduct);
                if (count > 0) {
                    return ServerResponse.createBySuccessMessage("下架成功");
                }
            }
        }

        return ServerResponse.createByErrorMessage("修改失败");
    }

    /**
     * 描 述: 商品详情(后台)
     *
     * @date: 2019-04-22 17:53
     * @author: lhpang
     * @param: [productId]
     * @return: com.lhpang.ac.common.ServerResponse<java.lang.Object>
     **/
    @Override
    public ServerResponse<ProductDetailVo> managerProductDetail(Integer productId) {

        if (productId == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(), ResponseCode.ILLEGAL_ARGUMENT.getDesc());
        }
        Product product = productMapper.selectByPrimaryKey(productId);

        if (product == null) {
            return ServerResponse.createByErrorMessage("商品不存在");
        }

        ProductDetailVo vo = this.poToDetailVo(product);

        return ServerResponse.createBySuccess(vo);
    }

    /**
     * 描 述: 查询Product列表(后台)
     *
     * @date: 2019/4/22 22:58
     * @author: lhpang
     * @param:
     * @return: com.lhpang.ac.common.ServerResponse<com.github.pageHelper.PageInfo>
     **/
    @Override
    public ServerResponse<PageInfo> getProductList(int pageNum, int pageSize) {
        //startPage
        Page page = PageHelper.startPage(pageNum, pageSize);
        //查询
        List<Product> products = productMapper.selectList();

        List<ProductListVo> listVos = new ArrayList<>();
        //po转Vo
        for (Product product : products) {
            listVos.add(this.poToListVo(product));
        }
        //pagerHelper收尾
        PageInfo pageInfo = new PageInfo(products);
        System.out.println(pageInfo);

        return ServerResponse.createBySuccess(pageInfo);
    }

    /**
     * 描 述: 搜索商品(后台)
     *
     * @date: 2019/4/22 23:30
     * @author: lhpang
     * @param:
     * @return: com.lhpang.ac.common.ServerResponse<com.github.pagehelper.PageInfo>
     **/
    @Override
    public ServerResponse<PageInfo> searchProduct(String productName, int pageNum, int pageSize) {

        //startPage
        PageHelper.startPage(pageNum, pageSize);
        if (StringUtils.isBlank(productName)) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(), ResponseCode.ILLEGAL_ARGUMENT.getDesc());
        }
        //查询
        List<Product> products = productMapper.selectByName(productName);

        List<ProductListVo> listVos = new ArrayList<>();
        //po转Vo
        for (Product product : products) {
            listVos.add(this.poToListVo(product));
        }
        //pagerHelper收尾
        PageInfo pageInfo = new PageInfo(products);
        pageInfo.setList(listVos);

        return ServerResponse.createBySuccess(pageInfo);
    }

    /**
     * 描 述: poToListVo
     *
     * @date: 2019/4/22 22:52
     * @author: lhpang
     * @param:
     * @return: com.lhpang.ac.vo.ProductListVo
     **/
    public ProductListVo poToListVo(Product product) {

        ProductListVo vo = new ProductListVo();
        vo.setId(product.getId());
        vo.setCategoryId(product.getCategoryId());
        vo.setName(product.getName());
        vo.setPrice(product.getPrice());
        vo.setStatus(product.getStatus());
        vo.setPrice(product.getPrice());
        vo.setSubtitle(product.getSubtitle());
        vo.setMainImage(product.getMainImage());

        return vo;
    }

    /**
     * 描 述: po转vo
     *
     * @date: 2019/4/22 22:15
     * @author: lhpang
     * @param:
     * @return: com.lhpang.ac.vo.ProductDetailVo
     **/
    private ProductDetailVo poToDetailVo(Product product) {

        ProductDetailVo vo = new ProductDetailVo();
        vo.setId(product.getId());
        vo.setMainImage(product.getMainImage());
        vo.setCategoryId(product.getCategoryId());
        vo.setDetail(product.getDetail());
        vo.setName(product.getName());
        vo.setPrice(product.getPrice());
        vo.setStatus(product.getStatus());
        vo.setStock(product.getStock());
        vo.setSubtitle(product.getSubtitle());
        vo.setCreateTime(product.getCreateTime().toString());
        vo.setCreateTime(DateUtil.dateToString(product.getCreateTime()));
        vo.setUpdateTime(DateUtil.dateToString(product.getUpdateTime()));
        return vo;
    }

    /**
     * 描 述: 获得商品信息详情(前台)
     *
     * @date: 2019-04-23 10:18
     * @author: lhpang
     * @param: [productId]
     * @return: com.lhpang.ac.common.ServerResponse<com.lhpang.ac.vo.ProductDetailVo>
     **/
    @Override
    public ServerResponse<ProductDetailVo> getProductDetail(Integer productId) {

        if (productId == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(), ResponseCode.ILLEGAL_ARGUMENT.getDesc());
        }
        Product product = productMapper.selectByPrimaryKey(productId);

        if (product == null) {
            return ServerResponse.createByErrorMessage("商品不存在");
        }

        if (product.getStatus() == Constant.ProductStatusEnum.OFF_SALE.getCode()) {
            return ServerResponse.createByErrorMessage("商品已下架");
        }

        ProductDetailVo vo = this.poToDetailVo(product);

        return ServerResponse.createBySuccess(vo);
    }

    /**
     * 描 述: 搜索(前台)
     *
     * @date: 2019-04-23 10:57
     * @author: lhpang
     * @param: [productName, pageSize, pageNum, orderBy]
     * @return: com.lhpang.ac.common.ServerResponse<com.github.pagehelper.PageInfo>
     **/
    @Override
    public ServerResponse<PageInfo> getProductByproductNameCategoryId(String productName, Integer categoryId, int pageSize, int pageNum, String orderBy) {
        if (StringUtils.isBlank(productName) && categoryId == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(), ResponseCode.ILLEGAL_ARGUMENT.getDesc());
        }
        //查询有没有这个分类
        Category categorie = categoryMapper.selectByPrimaryKey(categoryId);
        //PageHelper start
        PageHelper.startPage(pageNum, pageSize);
        if (categorie == null && StringUtils.isBlank(productName)) {
            //没有该分类也没有关键字时返回空
            List<ProductListVo> productListVos = Lists.newArrayList();
            PageInfo page = new PageInfo(productListVos);
            return ServerResponse.createBySuccess(page);
        }

        //排序
        if (StringUtils.isNotBlank(orderBy)) {
            if (Constant.ProductListOrderBy.PRICE_ASC_DESC.contains(orderBy)) {
                String[] orderByArray = orderBy.split("_");
                PageHelper.orderBy(orderByArray[0] + " " + orderByArray[1]);
            }
        }
        List<Product> products = productMapper.selectByNameCategoryId(productName, categoryId);

        List<ProductListVo> productListVos = new ArrayList<>();

        for (Product product : products) {
            ProductListVo productListVo = poToListVo(product);
            productListVos.add(productListVo);
        }

        PageInfo pageInfo = new PageInfo(products);
        pageInfo.setList(productListVos);

        return ServerResponse.createBySuccess(pageInfo);
    }

    /**
     * 描 述: 全部商品（前台）
     *
     * @date: 2019/4/29 22:34
     * @author: lhpang
     * @param: [pageNum, pageSize]
     * @return: com.lhpang.ac.common.ServerResponse<com.github.pagehelper.PageInfo>
     **/
    @Override
    public ServerResponse<PageInfo> list(int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Product> productList = productMapper.list();
        List<ProductListVo> productListVoList = Lists.newArrayList();
        for (Product product : productList) {
            ProductListVo vo = poToListVo(product);
            productListVoList.add(vo);
        }
        PageInfo pageInfo = new PageInfo<>(productList);
        pageInfo.setList(productListVoList);
        return ServerResponse.createBySuccess(pageInfo);
    }

    /**
     * 描 述: 后台删除商品
     *
     * @date: 2019/5/6 12:03
     * @author: lhpang
     * @param: [productId]
     * @return: com.lhpang.ac.common.ServerResponse
     **/
    @Override
    public ServerResponse delete(Integer productId) {

        if (productId == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(), ResponseCode.ILLEGAL_ARGUMENT.getDesc());
        }

        int count = productMapper.deleteByPrimaryKey(productId);

        if (count > 0) {
            return ServerResponse.createBySuccess();
        }
        return ServerResponse.createByError();
    }
}
