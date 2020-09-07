package com.common.platform.sys.factory;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.common.platform.base.config.context.SpringContextHolder;
import com.common.platform.base.utils.CoreUtil;
import com.common.platform.sys.cache.Cache;
import com.common.platform.sys.cache.CacheKey;
import com.common.platform.sys.log.LogObjectHolder;
import com.common.platform.sys.modular.system.entity.Dept;
import com.common.platform.sys.modular.system.entity.Dict;
import com.common.platform.sys.modular.system.entity.DictType;
import com.common.platform.sys.modular.system.entity.User;
import com.common.platform.sys.modular.system.mapper.DeptMapper;
import com.common.platform.sys.modular.system.mapper.DictMapper;
import com.common.platform.sys.modular.system.mapper.DictTypeMapper;
import com.common.platform.sys.modular.system.mapper.UserMapper;
import com.common.platform.sys.state.ManagerStatus;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * 快捷查询方法
 */
@Component
@DependsOn("springContextHolder")
public class ConstantFactory implements IConstantFactory {

    private DeptMapper deptMapper = SpringContextHolder.getBean(DeptMapper.class);
    private DictMapper dictMapper = SpringContextHolder.getBean(DictMapper.class);
    private DictTypeMapper dictTypeMapper = SpringContextHolder.getBean(DictTypeMapper.class);
    private UserMapper userMapper=SpringContextHolder.getBean(UserMapper.class);

    public static IConstantFactory me() {
        return SpringContextHolder.getBean("constantFactory");
    }

    @Override
    @Cacheable(value = Cache.CONSTANT, key = "'" + CacheKey.DEPT_NAME + "'+#deptId")
    public String getDeptName(Long deptId) {
        if (deptId == null) {
            return "";
        } else if (deptId == 0L) {
            return "顶级";
        } else {
            Dept dept = deptMapper.selectById(deptId);
            if (CoreUtil.isNotEmpty(dept) && CoreUtil.isNotEmpty(dept.getFullName())) {
                return dept.getFullName();
            }
            return "";
        }
    }

    @Override
    public String getDictName(Long dictId) {
        if (CoreUtil.isEmpty(dictId)) {
            return "";
        } else {
            Dict dict = dictMapper.selectById(dictId);
            if (dict == null) {
                return "";
            } else {
                return dict.getName();
            }
        }
    }

    @Override
    public String getDictsByName(String name, String code) {
        DictType temp = new DictType();
        temp.setName(name);
        QueryWrapper<DictType> queryWrapper = new QueryWrapper<>(temp);
        DictType dictType = dictTypeMapper.selectOne(queryWrapper);
        if (dictType == null) {
            return "";
        } else {
            QueryWrapper<Dict> wrapper = new QueryWrapper<>();
            wrapper = wrapper.eq("dict_type_id", dictType.getDictTypeId());
            List<Dict> dicts = dictMapper.selectList(wrapper);
            for (Dict item : dicts) {
                if (item.getCode() != null && item.getCode().equals(code)) {
                    return item.getName();
                }
            }
            return "";
        }
    }

    @Override
    public String getDictNameByCode(String dictCode) {
        if (CoreUtil.isEmpty(dictCode)) {
            return "";
        } else {
            QueryWrapper<Dict> dictQueryWrapper = new QueryWrapper<>();
            dictQueryWrapper.eq("code", dictCode);
            Dict dict = dictMapper.selectOne(dictQueryWrapper);
            if (dict == null) {
                return "";
            } else {
                return dict.getName();
            }
        }
    }

    @Override
    public List<Dict> findInDict(Long id) {
        if (CoreUtil.isEmpty(id)) {
            return null;
        } else {
            QueryWrapper<Dict> wrapper = new QueryWrapper<>();
            List<Dict> dicts = dictMapper.selectList(wrapper.eq("pid", id));
            if (dicts == null || dicts.size() == 0) {
                return null;
            } else {
                return dicts;
            }
        }
    }

    @Override
    public String getCacheObject(String para) {
        return LogObjectHolder.me().get().toString();
    }

    @Override
    public List<Long> getSubDeptId(Long deptId) {
        ArrayList<Long> deptIds = new ArrayList<>();

        if (deptId == null) {
            return deptIds;
        } else {
            List<Dept> depts = this.deptMapper.likePids(deptId);
            if (depts != null && depts.size() > 0) {
                for (Dept dept : depts) {
                    deptIds.add(dept.getDeptId());
                }
            }

            return deptIds;
        }
    }

    @Override
    public List<Long> getParentDeptIds(Long deptId) {
        Dept dept = deptMapper.selectById(deptId);
        String pids = dept.getPids();
        String[] split = pids.split(",");
        ArrayList<Long> parentDeptIds = new ArrayList<>();
        for (String s : split) {
            parentDeptIds.add(Long.valueOf(StrUtil.removeSuffix(StrUtil.removePrefix(s, "["), "]")));
        }
        return parentDeptIds;
    }

    /**
     * 根据用户id获取用户名称
     *
     * @param userId
     */
    @Override
    public String getUserNameById(Long userId) {
        User user = this.userMapper.selectById(userId);
        if(user==null){
            return "--";
        }else{
            return user.getName();
        }
    }

    @Override
    public String getUserAccountById(Long userId) {
        User user = this.userMapper.selectById(userId);
        if(user==null){
            return "--";
        }else{
            return user.getAccount();
        }
    }

    @Override
    public String getSingleRoleName(Long roleId) {
        return null;
    }

    @Override
    public String getRoleName(String roleIds) {
        return null;
    }

    @Override
    public String getSingleRoleTip(Long roleId) {
        return null;
    }

    @Override
    public String getSexName(String sexCode) {
        return getDictsByName("性别",sexCode);
    }

    @Override
    public String getStatusName(String status) {
        return ManagerStatus.getDescription(status);
    }

}

