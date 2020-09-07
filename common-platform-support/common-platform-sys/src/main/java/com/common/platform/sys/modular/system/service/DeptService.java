package com.common.platform.sys.modular.system.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.common.platform.base.exception.BizExceptionEnum;
import com.common.platform.base.exception.ServiceException;
import com.common.platform.base.page.factory.LayuiPageFactory;
import com.common.platform.base.tree.node.LayuiTreeNode;
import com.common.platform.base.tree.node.TreeviewNode;
import com.common.platform.base.tree.node.ZTreeNode;
import com.common.platform.base.utils.CoreUtil;
import com.common.platform.sys.modular.system.entity.Dept;
import com.common.platform.sys.modular.system.mapper.DeptMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
public class DeptService extends ServiceImpl<DeptMapper, Dept> {

    /**
     * 设置部门的父级ids
     */
    private void deptSetPids(Dept dept) {
        if (CoreUtil.isEmpty(dept.getPid()) || dept.getPid().equals(0L)) {
            dept.setPid(0L);
            dept.setPids("[0],");
        } else {
            Long pid = dept.getPid();
            Dept temp = this.getById(pid);
            String pids = temp.getPids();
            dept.setPid(pid);
            dept.setPids(pids + "[" + pid + "],");
        }
    }

    /**
     * 新增部门
     */
    @Transactional(rollbackFor = Exception.class)
    public void addDept(Dept dept) {

        if (CoreUtil.isOneEmpty(dept, dept.getSimpleName(), dept.getFullName(), dept.getPid(), dept.getDescription())) {
            throw new ServiceException(BizExceptionEnum.REQUEST_NULL);
        }

        //完善pids,根据pid拿到pid的pids
        this.deptSetPids(dept);

        this.save(dept);
    }

    /**
     * 修改部门
     */
    @Transactional(rollbackFor = Exception.class)
    public void editDept(Dept dept) {

        if (CoreUtil.isOneEmpty(dept, dept.getDeptId(), dept.getSimpleName(), dept.getFullName(), dept.getPid())) {
            throw new ServiceException(BizExceptionEnum.REQUEST_NULL);
        }

        //完善pids,根据pid拿到pid的pids
        this.deptSetPids(dept);

        this.updateById(dept);
    }

    /**
     * 删除部门
     */
    @Transactional
    public void deleteDept(Long deptId) {
        Dept dept = this.getById(deptId);

        //根据like查询删除所有级联的部门
        List<Dept> subDepts = this.baseMapper.likePids(dept.getDeptId());

        for (Dept temp : subDepts) {
            this.removeById(temp.getDeptId());
        }

        this.removeById(dept.getDeptId());
    }

    /**
     * 获取layuiTree的节点列表
     */
    public List<LayuiTreeNode> layuiTree() {
        return this.baseMapper.layuiTree();
    }

    /**
     * 获取ztree的节点列表
     */
    public List<ZTreeNode> tree() {
        return this.baseMapper.tree();
    }

    /**
     * 获取ztree的节点列表
     */
    public List<TreeviewNode> treeviewNodes() {
        return this.baseMapper.treeviewNodes();
    }

    /**
     * 获取所有部门列表
     */
    public Page<Map<String, Object>> list(String condition, Long deptId) {
        Page page = LayuiPageFactory.defaultPage();
        return this.baseMapper.list(page, condition, deptId);
    }

}
