package com.example.system.test;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;

import java.util.ArrayList;
import java.util.List;

public class ExcelListener<T> extends AnalysisEventListener<T> {
    private List<T> list=new ArrayList<T>();

    //读取excel内容
    //从第二行开始读取，把每行封装内容读取到T对象
    @Override
    public void invoke(T t, AnalysisContext analysisContext) {
        list.add(t);
    }

    public List<T> getList() {
        return list;
    }

    //所有操作完成后执行
    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {

    }
}
