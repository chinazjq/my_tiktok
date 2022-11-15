package com.zjq.utils;

import com.github.pagehelper.PageInfo;
import lombok.Data;

import java.util.List;

@Data
public class PagedGridResult {

    private int page;            // 当前页数
    private long total;            // 总页数
    private long records;        // 总记录数
    private List<?> rows;        // 每行显示的内容

    public static PagedGridResult setterPagedGrid(List<?> list,
                                                  Integer page) {
        PageInfo<?> pageList = new PageInfo<>(list);
        PagedGridResult gridResult = new PagedGridResult();
        gridResult.setRows(list);
        gridResult.setPage(page);
        gridResult.setRecords(pageList.getTotal());
        gridResult.setTotal(pageList.getPages());
        return gridResult;
    }
}
