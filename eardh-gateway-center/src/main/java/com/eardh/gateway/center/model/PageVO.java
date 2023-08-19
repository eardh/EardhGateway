package com.eardh.gateway.center.model;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.Data;

import java.util.List;

/**
 * @author eardh
 * @date 2023/4/11 22:44
 */
@Data
public class PageVO<T> {

    private Long currentPage;

    private Long pageSize;

    private Long total;

    private List<T> items;

    public <S> PageVO(Page<S> page, Class<T> clazz) {
        this.currentPage = page.getCurrent();
        this.pageSize = page.getSize();
        this.total = page.getTotal();
        this.items = BeanUtil.copyToList(page.getRecords(), clazz);
    }
}
