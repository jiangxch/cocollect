package com.github.jiangxch.cocollect.dao.impl.file;

import com.github.jiangxch.cocollect.config.CocollectConfig;
import com.github.jiangxch.cocollect.entity.CategoryEntity;
import com.github.jiangxch.cocollect.util.IdUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * @author: jiangxch
 * @date: 2021/5/15 下午12:09
 */
public abstract class BaseFileDao<T> {
    protected Map<String, T> idEntityMap = new HashMap<>();

    /**
     * 将文件数据反序列化到 IdEntityMap
     */
    @SuppressWarnings("unchecked")
    protected void load() {
        String entityDataFilePath = CocollectConfig.getIns().getCocollectDaoDataCategoryPath() + File.separator + getFileName();
        File file = new File(entityDataFilePath);
        if (file.exists() && file.isFile()) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
                idEntityMap = (Map<String, T>) ois.readObject();
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    protected void importData(Map<String, T> idEntityMap) {
        this.idEntityMap = idEntityMap;
        dump();
    }

    private void initFileDirectory() {
       createDirectoryIfNotExist(CocollectConfig.getIns().getCocollectRootCategoryPath());
       createDirectoryIfNotExist(CocollectConfig.getIns().getCocollectDaoDataCategoryPath());
    }

    private void createDirectoryIfNotExist(String directoryPath) {
        File rootFileCategoryPathFile = new File(directoryPath);
        if (!rootFileCategoryPathFile.exists()) {
            rootFileCategoryPathFile.mkdir();
        }

    }

    /**
     * 将 IdEntityMap 数据序列化到文件
     */
    protected void dump() {
        initFileDirectory();
        String entityDataFilePath = CocollectConfig.getIns().getCocollectDaoDataCategoryPath() + File.separator + getFileName();
        File file = new File(entityDataFilePath);
        if (file.exists()) {
            file.delete();
        }
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file))) {
            oos.writeObject(idEntityMap);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected abstract String getFileName();

    /**
     * 生成唯一id,保证每个entity中唯一
     * @return
     */
    protected String generateUniqueId() {
        String id = null;
        do {
            id = IdUtil.generateId();
        } while (idEntityMap.containsKey(id));
        return id;
    }

    public void deleteAll() {
        idEntityMap = new HashMap<>();
        dump();
    }

}
