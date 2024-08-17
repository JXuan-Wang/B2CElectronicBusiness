package org.example.system.manager.listener;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.read.listener.ReadListener;
import com.alibaba.excel.util.ListUtils;
import jakarta.annotation.Resource;
import org.example.system.manager.mapper.CategoryMapper;
import org.example.system.model.entity.product.Category;
import org.example.system.model.vo.product.CategoryExcelVo;


import java.io.IOException;
import java.util.List;

//监听器
public class ExcelListener<T> implements ReadListener<T> {
    private CategoryMapper categoryMapper;

    public ExcelListener(CategoryMapper categoryMapper) {
        this.categoryMapper = categoryMapper;
    }

    /**
     每隔5条存储数据库，实际使用中可以100条，然后清理list ，方便内存回收
     */
    private static final int BATCH_COUNT = 100;
    /**
     * 缓存的数据
     */
    private List<CategoryExcelVo> cachedDataList = ListUtils.newArrayListWithExpectedSize(BATCH_COUNT);


    @Override
    public void invoke(T t, AnalysisContext analysisContext) {
        //把每行数据对象t放到cachedDataList集合里面
        cachedDataList.add((CategoryExcelVo) t);
        //达到BATCH_COUNT了，需要去存储一次数据库，防止数据几万条在内存，容易OOM
        if(cachedDataList.size() >= BATCH_COUNT) {
            //调用方法一次性批量添加数据库里面
            saveData();
            //清理list集合
            cachedDataList=ListUtils.newArrayListWithExpectedSize(BATCH_COUNT);
        }
    }

    private void saveData() {
        categoryMapper.batchInsert(cachedDataList);
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {
        //保存数据
        saveData();
    }
}
