package com.common.platform.base.page.factory;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.common.platform.base.config.context.HttpContext;
import com.common.platform.base.page.LayuiPageInfo;
import com.common.platform.base.utils.CoreUtil;

import javax.servlet.http.HttpServletRequest;

/**
 * Layui Table默认的分页参数创建
 */
public class LayuiPageFactory {

    /**
     * 获取layui table的分页参数
     */
    public static Page defaultPage() {
        HttpServletRequest request = HttpContext.getRequest();

        int limit = 20;
        int page = 1;

        //每页多少条数据
        String limitString = request.getParameter("limit");
        if (CoreUtil.isNotEmpty(limitString)) {
            limit = Integer.parseInt(limitString);
        }

        //第几页
        String pageString = request.getParameter("page");
        if (CoreUtil.isNotEmpty(pageString)) {
            page = Integer.parseInt(pageString);
        }

        return new Page(page, limit);
    }

    /**
     * 创建layui能识别的分页响应参数
     */
    public static LayuiPageInfo createPageInfo(IPage page) {
        LayuiPageInfo result = new LayuiPageInfo();
        result.setCount(page.getTotal());
        result.setData(page.getRecords());
        return result;
    }
}
