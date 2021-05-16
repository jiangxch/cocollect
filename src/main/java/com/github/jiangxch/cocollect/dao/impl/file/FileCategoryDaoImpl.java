package com.github.jiangxch.cocollect.dao.impl.file;

import com.github.jiangxch.cocollect.dao.CategoryDao;
import com.github.jiangxch.cocollect.dao.CodeSegmentDao;
import com.github.jiangxch.cocollect.entity.CategoryEntity;
import com.github.jiangxch.cocollect.entity.CodeSegmentEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author: jiangxch
 * @date: 2021/5/13 下午9:09
 */
public class FileCategoryDaoImpl extends BaseFileDao<CategoryEntity> implements CategoryDao {
    @Override
    public void insert(String categoryName, String pid) {
        CategoryEntity entity = new CategoryEntity();
        entity.setId(generateUniqueId());
        entity.setPid(pid);
        entity.setName(categoryName);
        idEntityMap.put(entity.getId(),entity);
        dump();
    }

    @Override
    public void updateById(String categoryName, String categoryId) {
        CategoryEntity entity = idEntityMap.get(categoryId);
        entity.setName(categoryName);
        dump();
    }

    @Override
    public void deleteById(String categoryId) {
        List<CodeSegmentEntity> codeSegmentEntities = CodeSegmentDao.getIns().listByCategoryId(categoryId);
        if (!codeSegmentEntities.isEmpty()) {
            throw new RuntimeException("请先移除该目录下的代码片段,在进行删除");
        }
        List<CategoryEntity> categoryEntities = CategoryDao.getIns().listByPid(categoryId);
        if (!categoryEntities.isEmpty()) {
            throw new RuntimeException("请先移除该目录子目录,在进行删除");
        }
        idEntityMap.remove(categoryId);
        dump();
    }

    @Override
    public List<CategoryEntity> listAll() {
        load();
        return new ArrayList<>(idEntityMap.values());
    }

    @Override
    public List<CategoryEntity> listByPid(String pid) {
        load();
        return new ArrayList<>(idEntityMap.values()).stream().filter(x -> x.getPid().equals(pid)).collect(Collectors.toList());
    }

    @Override
    public void importData(List<CategoryEntity> entities) {
        importData(entities.stream().collect(Collectors.toMap(CategoryEntity::getId, Function.identity())));
    }

    @Override
    protected String getFileName() {
        return "category";
    }
}
