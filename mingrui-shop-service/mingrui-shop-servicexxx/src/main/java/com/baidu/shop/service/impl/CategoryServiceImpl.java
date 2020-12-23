package com.baidu.shop.service.impl;

import com.baidu.shop.base.BaseApiService;
import com.baidu.shop.base.Result;
import com.baidu.shop.entity.CategoryEntity;
import com.baidu.shop.mapper.CategoryMapper;
import com.baidu.shop.service.CategoryService;
import com.baidu.shop.utils.ObjectUtils;
import com.google.gson.JsonObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RestController;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.List;

/**
 * @ClassName CategoryServiceImpl
 * @Description: TODO
 * @Author xuezhaochang
 * @Date 2020/12/22
 * @Version V1.0
 **/
@RestController
public class CategoryServiceImpl extends BaseApiService implements CategoryService {
    @Resource
    private CategoryMapper categoryMapper;
    @Override
    public Result<List<CategoryEntity>> getCategoryById(Integer pid) {
        CategoryEntity entity = new CategoryEntity();
        entity.setParentId(pid);
        List<CategoryEntity> list = categoryMapper.select(entity);
        return this.setResultSuccess(list);
    }
    @Transactional
    @Override
    public Result<JsonObject> delCategory(Integer id) {
        //校验id是否合法
        if (ObjectUtils.isNull(id) || id <= 0) return this.setResultError("id不合法");

        //通过id查询一条节点数据
        CategoryEntity categoryEntity = categoryMapper.selectByPrimaryKey(id);

        //判断查询出来的数据是否存在。如果查询出来的结果返回null/证明数据不存在
        if (ObjectUtils.isNull(categoryEntity)) return this.setResultError("您要查找的数据不存在");

        //如果查询出来的数据的isparent字段状态为1.那么说明时父节点。就不能删除。
        if (categoryEntity.getIsParent() == 1 ) return this.setResultError("当前节点下有数据，不能进行删除");


        //相当于把实体类放入这个类中，这个类中的方法可以操作SQL语句。他是tkMapper中的类
        Example example = new Example(CategoryEntity.class);

        //对sql进行拼接 select * from 表名 where 1=1 and parentId = ?
        example.createCriteria().andEqualTo("parentId",categoryEntity.getParentId());

        //将查出的数据装入list集合中
        List<CategoryEntity> list = categoryMapper.selectByExample(example);

        if (list.size() <= 1){
            CategoryEntity updateCategory = new CategoryEntity();
            updateCategory.setIsParent(0);
            updateCategory.setId(categoryEntity.getParentId());

            categoryMapper.updateByPrimaryKeySelective(updateCategory);
        }

        //通过id删除节点
        categoryMapper.deleteByPrimaryKey(id);
        return this.setResultSuccess();
    }
}
