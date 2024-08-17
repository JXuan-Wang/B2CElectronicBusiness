package org.example.system.manager.service.impl;

import com.alibaba.excel.EasyExcel;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;
import org.example.system.common.exception.MyException;
import org.example.system.manager.listener.ExcelListener;
import org.example.system.manager.mapper.CategoryMapper;
import org.example.system.manager.service.CategoryService;
import org.example.system.model.entity.product.Category;
import org.example.system.model.vo.common.ResultCodeEnum;
import org.example.system.model.vo.product.CategoryExcelVo;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {
    @Resource
    private CategoryMapper categoryMapper;

    //分类列表，每次查询一层数据
    @Override
    public List<Category> findCategoryList(Long id) {
        //根据id条件进行查询
        List<Category> categoryList=categoryMapper.selectCategoryByParentId(id);

        //遍历返回list集合，判断每个分类是否有下一层分类，如果有设置hasChildren=true
        if(!CollectionUtils.isEmpty(categoryList)){
            for(Category category:categoryList){
                //判断每个分类是否有下一层分类
                int count=categoryMapper.selectCountByParentId(category.getId());
                category.setHasChildren(count > 0);
            }
        }

        return categoryList;
    }

    //导出数据
    @Override
    public void exportData(HttpServletResponse response) {
        //设置响应头信息和其他信息
        // 设置响应结果类型
        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding("utf-8");
        // 这里URLEncoder.encode可以防止中文乱码 当然和easyexcel没有关系
        try {
            String fileName = URLEncoder.encode("分类数据", "UTF-8");
            //设置响应头信息
            response.setHeader("Content-disposition","attachment;filename="+fileName+".xlsx");
            //调用mapper方法查询所有分类，返回list集合
            List<Category> categoryList=categoryMapper.findAll();
            //Category-->CategoryExcelVo
            List<CategoryExcelVo> categoryExcelVoList=new ArrayList<CategoryExcelVo>();
            for(Category category:categoryList){
                CategoryExcelVo categoryExcelVo=new CategoryExcelVo();
                //把category的值获取出来，设置到categoryExcelVo
                BeanUtils.copyProperties(category,categoryExcelVo);
                categoryExcelVoList.add(categoryExcelVo);
            }
            //调用EasyExcel的write方法完成写操作
            EasyExcel.write(response.getOutputStream(), CategoryExcelVo.class)
                    .sheet("分类数据")
                    .doWrite(categoryExcelVoList);
        } catch (Exception e) {
            e.printStackTrace();
            throw new MyException(ResultCodeEnum.DATA_ERROR);
        }
    }

    //导入
    @Override
    public void importData(MultipartFile file) {
        //TODO 监听器
        ExcelListener<Category> excelListener = new ExcelListener<>(categoryMapper);
        try {
            EasyExcel.read(file.getInputStream(), CategoryExcelVo.class,excelListener)
                    .sheet().doRead();
        } catch (IOException e) {
            e.printStackTrace();
            throw new MyException(ResultCodeEnum.DATA_ERROR);
        }
    }
}
