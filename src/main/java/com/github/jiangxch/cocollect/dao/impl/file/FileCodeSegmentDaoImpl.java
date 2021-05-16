package com.github.jiangxch.cocollect.dao.impl.file;

import com.github.jiangxch.cocollect.dao.CodeSegmentDao;
import com.github.jiangxch.cocollect.entity.CategoryEntity;
import com.github.jiangxch.cocollect.entity.CodeSegmentEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author: jiangxch
 * @date: 2021/5/13 下午9:09
 */
public class FileCodeSegmentDaoImpl extends BaseFileDao<CodeSegmentEntity> implements CodeSegmentDao {
    @Override
    public void insert(CodeSegmentEntity codeSegmentEntity) {
        codeSegmentEntity.setId(generateUniqueId());
        idEntityMap.put(codeSegmentEntity.getId(),codeSegmentEntity);
        dump();
    }

    @Override
    public void updateById(CodeSegmentEntity codeSegmentEntity, String codeSegmentId) {
        CodeSegmentEntity remove = idEntityMap.remove(codeSegmentId);
        codeSegmentEntity.setId(codeSegmentId);
        if (remove != null) {
            idEntityMap.put(codeSegmentId,codeSegmentEntity);
        }
        dump();
    }


    @Override
    public void importData(List<CodeSegmentEntity> entities) {
        importData(entities.stream().collect(Collectors.toMap(CodeSegmentEntity::getId, Function.identity())));
    }


    @Override
    public void deleteById(String codeSegmentId) {
        idEntityMap.remove(codeSegmentId);
        dump();
    }

    @Override
    public List<CodeSegmentEntity> listAll() {
        load();
        return new ArrayList<>(idEntityMap.values());
    }

    @Override
    public List<CodeSegmentEntity> listByCategoryId(String categoryId) {
        load();
        return new ArrayList<>(idEntityMap.values()).stream().filter(x -> x.getCategoryId().equals(categoryId)).collect(Collectors.toList());
    }

    @Override
    protected String getFileName() {
        return "codeSegment";
    }
}
