package com.github.jiangxch.cocollect.plugin.ui;

import com.github.jiangxch.cocollect.config.CocollectConfig;
import com.github.jiangxch.cocollect.constant.GlobalConstant;
import com.github.jiangxch.cocollect.dao.CategoryDao;
import com.github.jiangxch.cocollect.dao.CodeSegmentDao;
import com.github.jiangxch.cocollect.entity.CategoryEntity;
import com.github.jiangxch.cocollect.entity.CodeSegmentEntity;
import com.github.jiangxch.cocollect.util.SwingUtil;
import com.github.jiangxch.cocollect.util.SystemUtil;

import javax.swing.*;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author: jiangxch
 * @date: 2021/5/13 下午9:49
 */
public class CocollectToolWindow {
    private static CocollectToolWindow INS = new CocollectToolWindow();

    private JPanel root;
    private JButton addCategoryButton;
    private JButton updateCategoryButton;
    private JButton deleteCategoryButton;
    private JButton addCodeSegmentButton;
    private JButton updateCodeSegmentButton;
    private JButton deleteCodeSegmentButton;
    private JTree categoryCodeTree;
    private JLabel tipsLabel;
    private JButton importDaoButton;
    private JButton exportButton;
    private JButton clearAllDataButton;


    private JFrame jframe;

    private CategoryCurdWindow categoryCurdWindow = new CategoryCurdWindow(this);

    public CodeSegmentCurdWindow getCodeSegmentCurdWindow() {
        return codeSegmentCurdWindow;
    }

    private CodeSegmentCurdWindow codeSegmentCurdWindow = new CodeSegmentCurdWindow(this);

    private DefaultMutableTreeNode selectTreeNode = null;

    public static CocollectToolWindow getIns() {
        return INS;
    }

    public CocollectToolWindow() {
        init();
    }

    private void init() {
        initButton();
        initJTree();
        initListener();
        jframe = new JFrame("cocollect");
        jframe.pack();
        jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jframe.setVisible(false);
    }

    private void initListener() {
        categoryCodeTree.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
                if (' ' == e.getKeyChar()) { // 空格
                    Object userObject = getUserObject();
                    if (!(userObject instanceof CodeSegmentEntity)) {
                        SwingUtil.showTips(tipsLabel,"请选择代码片段节点", 2000);
                        return;
                    }
                    // 复制代码片段到剪贴板
                    CodeSegmentEntity select = (CodeSegmentEntity) userObject;
                    SystemUtil.copyText2Clipboard(select.getCode());
                    SwingUtil.showTips(tipsLabel,"复制成功", 1500);
                }
            }

            @Override
            public void keyPressed(KeyEvent e) {

            }

            @Override
            public void keyReleased(KeyEvent e) {

            }
        });
    }

    private void initJTree() {
        categoryCodeTree.addTreeSelectionListener(new TreeSelectionListener() {
            @Override
            public void valueChanged(TreeSelectionEvent e) {
                selectTreeNode = (DefaultMutableTreeNode) categoryCodeTree.getLastSelectedPathComponent();
            }
        });
        refreshTree();
    }

    public void refreshTree() {
        List<String> oldExpandTreeNodeCategoryIds = new ArrayList<>();
        for (int i = 0; i < categoryCodeTree.getRowCount(); i++) {
            TreePath pathForRow = categoryCodeTree.getPathForRow(i);
            if (categoryCodeTree.isExpanded(i)) {// expand 状态的节点才是目录节点
                DefaultMutableTreeNode lastPathComponent = (DefaultMutableTreeNode) (pathForRow.getLastPathComponent());
                if ((lastPathComponent).getUserObject() instanceof CategoryEntity) {
                    CategoryEntity expendCategoryEntity = (CategoryEntity) lastPathComponent.getUserObject() ;
                    oldExpandTreeNodeCategoryIds.add(expendCategoryEntity.getId());
                }
            }
        }

        CategoryEntity rootCategory = new CategoryEntity();
        rootCategory.setId(GlobalConstant.CATEGORY_ROOT_ID);
        rootCategory.setPid(null);
        rootCategory.setName(GlobalConstant.CATEGORY_ROOT_NAME);

        // 构建目录树形结构
        DefaultMutableTreeNode rootTreeNode = new DefaultMutableTreeNode(rootCategory);
        Map<String, DefaultMutableTreeNode> idCategoryTreeNodeMap = CategoryDao.getIns().listAll().stream().collect(Collectors.toMap(CategoryEntity::getId, DefaultMutableTreeNode::new));
        for (Map.Entry<String, DefaultMutableTreeNode> idCategoryTreeNodeEntry : idCategoryTreeNodeMap.entrySet()) {
            DefaultMutableTreeNode categoryTreeNode = idCategoryTreeNodeEntry.getValue();
            CategoryEntity categoryEntity = (CategoryEntity) categoryTreeNode.getUserObject();

            if (GlobalConstant.CATEGORY_ROOT_ID.equals(categoryEntity.getPid())) {
                // 父节点是根节点
                rootTreeNode.add(categoryTreeNode);
            } else {
                DefaultMutableTreeNode parentCategoryTreeNode = idCategoryTreeNodeMap.get(categoryEntity.getPid());
                if (parentCategoryTreeNode == null) {
                    throw new RuntimeException("can't find this category't parent,pid=" + categoryEntity.getPid());
                }
                parentCategoryTreeNode.add(categoryTreeNode);
            }
        }

        // 填充代码片段节点
        List<CodeSegmentEntity> codeSegmentEntities = CodeSegmentDao.getIns().listAll();
        for (CodeSegmentEntity codeSegmentEntity : codeSegmentEntities) {
            DefaultMutableTreeNode codeSegmentTreeNode = new DefaultMutableTreeNode(codeSegmentEntity);
            if (GlobalConstant.CATEGORY_ROOT_ID.equals(codeSegmentEntity.getCategoryId())) {
                rootTreeNode.add(codeSegmentTreeNode);
            } else {
                DefaultMutableTreeNode categoryTreeNode = idCategoryTreeNodeMap.get(codeSegmentEntity.getCategoryId());
                categoryTreeNode.add(codeSegmentTreeNode);
            }
        }
        DefaultTreeModel rootModel = new DefaultTreeModel(rootTreeNode);
        rootModel.reload();
        categoryCodeTree.setModel(rootModel);
        for (String oldExpandCategoryId : oldExpandTreeNodeCategoryIds) {
            DefaultMutableTreeNode defaultMutableTreeNode = null;
            if (oldExpandCategoryId.equals(GlobalConstant.CATEGORY_ROOT_ID)) {
                defaultMutableTreeNode = rootTreeNode;
            } else {
                defaultMutableTreeNode = idCategoryTreeNodeMap.get(oldExpandCategoryId);
            }
            if (defaultMutableTreeNode == null) {
                continue;
            }
            // 必须通过new TreePath(defaultMutableTreeNode) 创建的TreePath才能在刷新JTree数据后expand成功
            categoryCodeTree.expandPath(new TreePath(defaultMutableTreeNode.getPath()));
        }
    }

    private Object getUserObject() {
        Object userObject = selectTreeNode.getUserObject();
        if (userObject == null) {
            SwingUtil.showTips(tipsLabel, "请选择树节点后在点击按钮", 5000);
        }
        return userObject;
    }

    @SuppressWarnings("unchecked")
    private void initButton() {
        addCategoryButton.addActionListener(e -> {
            categoryCurdWindow.showInsertWindow((CategoryEntity) getUserObject());
        });
        updateCategoryButton.addActionListener(e -> {
            categoryCurdWindow.showUpdateWindow((CategoryEntity) getUserObject());
        });
        deleteCategoryButton.addActionListener(e -> {
            CategoryEntity selectCategoryEntity = (CategoryEntity) getUserObject();
            CategoryDao.getIns().deleteById(selectCategoryEntity.getId());
            refreshTree();
        });
        addCodeSegmentButton.addActionListener(e -> {
            codeSegmentCurdWindow.showCreateCodeSegmentWindow((CategoryEntity) getUserObject());
        });
        updateCodeSegmentButton.addActionListener(e -> {
            codeSegmentCurdWindow.showUpdateCodeSegmentWindow((CodeSegmentEntity) getUserObject());
        });
        deleteCodeSegmentButton.addActionListener(e -> {
            CodeSegmentEntity codeSegmentEntity = (CodeSegmentEntity) getUserObject();
            CodeSegmentDao.getIns().deleteById(codeSegmentEntity.getId());
            refreshTree();
        });
        importDaoButton.addActionListener(e -> {
            if (!CategoryDao.getIns().listAll().isEmpty()
                    || !CodeSegmentDao.getIns().listAll().isEmpty()) {
                SwingUtil.showTips(tipsLabel, "请先清空所有数据后在导入", 5000);
                return;
            }
            SwingUtil.showFileSelectWindow(CocollectConfig.getIns().getCocollectRootCategoryPath(),CocollectToolWindow.this.jframe, selectFile -> {
                try(ObjectInputStream ois = new ObjectInputStream(new FileInputStream(selectFile))) {
                    CategoryDao.getIns().importData((List<CategoryEntity>) ois.readObject());
                    CodeSegmentDao.getIns().importData((List<CodeSegmentEntity>) ois.readObject());
                } catch (IOException | ClassNotFoundException ex) {
                    ex.printStackTrace();
                }
                return null;
            });
            refreshTree();
        });
        exportButton.addActionListener(e -> {
            SwingUtil.showFileSelectWindow(CocollectConfig.getIns().getCocollectRootCategoryPath(),CocollectToolWindow.this.jframe,selectFile -> {
                try(ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(selectFile))) {
                    oos.writeObject(CategoryDao.getIns().listAll());
                    oos.writeObject(CodeSegmentDao.getIns().listAll());
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                return null;
            });
        });
        clearAllDataButton.addActionListener(e -> {
            CategoryDao.getIns().deleteAll();
            CodeSegmentDao.getIns().deleteAll();
            refreshTree();
        });
    }

    public JPanel getRoot() {
        return root;
    }
}
