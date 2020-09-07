package com.common.platform.sys.modular.system.warpper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.common.platform.base.page.PageResult;
import com.common.platform.base.utils.CoreUtil;
import com.common.platform.sys.base.controller.warpper.BaseControllerWrapper;
import com.common.platform.sys.factory.ConstantFactory;
import com.common.platform.sys.util.DecimalUtil;

import java.util.List;
import java.util.Map;

/**
 * 部门列表的包装
 */
public class DeptWrapper extends BaseControllerWrapper {

    public DeptWrapper(Map<String, Object> single) {
        super(single);
    }

    public DeptWrapper(List<Map<String, Object>> multi) {
        super(multi);
    }

    public DeptWrapper(Page<Map<String, Object>> page) {
        super(page);
    }

    public DeptWrapper(PageResult<Map<String, Object>> pageResult) {
        super(pageResult);
    }

    @Override
    protected void wrapTheMap(Map<String, Object> map) {
        Long pid = DecimalUtil.getLong(map.get("pid"));

        if (CoreUtil.isEmpty(pid) || pid.equals(0L)) {
            map.put("pName", "--");
        } else {
            map.put("pName", ConstantFactory.me().getDeptName(pid));
        }
    }
}

