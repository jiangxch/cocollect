package com.github.jiangxch.cocollect.dao;

import com.github.jiangxch.cocollect.dao.impl.file.FileCategoryDaoImpl;
import com.github.jiangxch.cocollect.dao.impl.file.FileCodeSegmentDaoImpl;
import com.github.jiangxch.cocollect.entity.CodeSegmentEntity;

import java.util.List;

/**
 * @author: jiangxch
 * @date: 2021/5/13 下午8:56
 */
public interface CodeSegmentDao {
    void insert(CodeSegmentEntity codeSegmentEntity);

    void updateById(CodeSegmentEntity codeSegmentEntity, String codeSegmentId);

    void deleteById(String codeSegmentId);

    List<CodeSegmentEntity> listAll();

    CodeSegmentDao fileCodeSegmentDao = new FileCodeSegmentDaoImpl();

    static CodeSegmentDao getIns() {
        return fileCodeSegmentDao;
    }

    void importData(List<CodeSegmentEntity> entities);

    List<CodeSegmentEntity> listByCategoryId(String categoryId);

    void deleteAll();

}
