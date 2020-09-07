package com.common.platform.sys.modular.system.warpper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.common.platform.base.page.PageResult;
import com.common.platform.sys.base.controller.warpper.BaseControllerWrapper;
import com.common.platform.sys.factory.ConstantFactory;
import com.common.platform.sys.util.DecimalUtil;

import java.util.List;
import java.util.Map;

/**
 * 用户管理的包装类
 */
public class UserWrapper extends BaseControllerWrapper {

    public UserWrapper(Map<String, Object> single) {
        super(single);
    }

    public UserWrapper(List<Map<String, Object>> multi) {
        super(multi);
    }

    public UserWrapper(Page<Map<String, Object>> page) {
        super(page);
    }

    public UserWrapper(PageResult<Map<String, Object>> pageResult) {
        super(pageResult);
    }

    @Override
    protected void wrapTheMap(Map<String, Object> map) {
        map.put("sexName", ConstantFactory.me().getSexName((String) map.get("sex")));
        map.put("roleName", ConstantFactory.me().getRoleName((String) map.get("roleId")));
        map.put("deptName", ConstantFactory.me().getDeptName(DecimalUtil.getLong(map.get("deptId"))));
        map.put("statusName", ConstantFactory.me().getStatusName((String) map.get("status")));
        map.put("positionName", ConstantFactory.me().getParentDeptIds(DecimalUtil.getLong(map.get("userId"))));
    }

}