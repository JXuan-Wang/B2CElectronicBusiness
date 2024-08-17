package com.example.system.test;

import com.alibaba.excel.EasyExcel;
import org.example.system.model.vo.product.CategoryExcelVo;

import java.util.ArrayList;
import java.util.List;

public class EasyExcelTest {
    public static void main(String[] args) {
//        read();

        write();
    }

    //读操作
    public static void read() {
        //定义读取excel文件位置
        String fileName="C:/Users/炫/Desktop/01.xlsx";
        //调用方法
        ExcelListener excelListener = new ExcelListener();
        EasyExcel.read(fileName, CategoryExcelVo.class, excelListener)
                .sheet().doRead();
        List<CategoryExcelVo> list=excelListener.getList();
        System.out.println(list);
    }

    //写操作
    public static void write() {
        List<CategoryExcelVo> list=new ArrayList<>();
        list.add(new CategoryExcelVo(1L , "数码办公" , "",0L, 1, 1)) ;
        list.add(new CategoryExcelVo(11L , "华为手机" , "",1L, 1, 2)) ;
        //定义读取excel文件位置
        String fileName="C:/Users/炫/Desktop/01.xlsx";
        EasyExcel.write(fileName, CategoryExcelVo.class)
                .sheet("分类的数据")
                .doWrite(list);
    }
}
