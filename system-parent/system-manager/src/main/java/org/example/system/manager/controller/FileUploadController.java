package org.example.system.manager.controller;

import jakarta.annotation.Resource;
import org.example.system.manager.service.FileUploadService;
import org.example.system.model.vo.common.Result;
import org.example.system.model.vo.common.ResultCodeEnum;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/admin/system")
public class FileUploadController {
    @Resource
    private FileUploadService fileUploadService;

    @PostMapping("/fileUpload")
    public Result fileUpload(@RequestParam("file") MultipartFile file) {
        //获取上传文件
        //调用service的方法上传，放回minio路径
        String url=fileUploadService.upload(file);

        return Result.build(url, ResultCodeEnum.SUCCESS);
    }
}
