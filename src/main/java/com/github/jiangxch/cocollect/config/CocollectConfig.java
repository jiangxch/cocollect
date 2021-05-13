package com.github.jiangxch.cocollect.config;

import com.github.jiangxch.cocollect.util.SystemUtil;
import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.util.xmlb.XmlSerializerUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;

/**
 * @author: jiangxch
 * @date: 2021/5/13 下午8:46
 */
@State(name = "cocollect",storages = {@Storage("cocollect.xml")})
public class CocollectConfig implements PersistentStateComponent<CocollectConfig> {

    /**
     * cocollect持久化文件的根目录路径，默认为 用户家目录下面的 .cocollect目录,
     * 目录下结构
     *
     * .cocollect
     *      data         文件,存放DAO层序列化数据文件
     *      import       文件,内容为所有DAO导出的序列化数据文件,用于导入数据(为什么不用data目录下的文件?因为一个文件方便导入导出)
     *      export       文件,内容为所有DAO导出的序列化数据文件,用于导出数据
     *
     *
     * 不包含 {@link CocollectConfig} 该配置持久化目录路径由IDEA控制
     */
    private String COCOLLECT_ROOT_CATEGORY_PATH = SystemUtil.getUserDir() + File.separator + ".cocollect";


    @Nullable
    @Override
    public CocollectConfig getState() {
        return this;
    }

    @Override
    public void loadState(@NotNull CocollectConfig state) {
        XmlSerializerUtil.copyBean(state, this);
    }

    public static CocollectConfig getIns() {
        return ServiceManager.getService(CocollectConfig.class);
    }
}
