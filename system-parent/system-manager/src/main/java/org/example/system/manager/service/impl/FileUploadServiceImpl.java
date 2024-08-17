package org.example.system.manager.service.impl;


import cn.hutool.core.date.DateUtil;
import io.minio.*;
import jakarta.annotation.Resource;
import org.example.system.common.exception.MyException;
import org.example.system.manager.properties.MinioProperties;
import org.example.system.manager.service.FileUploadService;
import org.example.system.model.vo.common.ResultCodeEnum;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.UUID;

@Service
public class FileUploadServiceImpl implements FileUploadService {
    @Resource
    private MinioProperties minioProperties;

    @Override
    public String upload(MultipartFile file) {
        try {
            // 创建一个Minio的客户端对象
            MinioClient minioClient = MinioClient.builder()
                    .endpoint(minioProperties.getEndpointUrl() )
                    .credentials(minioProperties.getAccessKey(), minioProperties.getSecretKey())
                    .build();

            boolean found = minioClient.bucketExists(BucketExistsArgs.builder().bucket(minioProperties.getBucketName()).build());

            // 如果不存在，那么此时就创建一个新的桶
            if (!found) {
                minioClient.makeBucket(MakeBucketArgs.builder().bucket(minioProperties.getBucketName()).build());
            }
//            else {  // 如果存在打印信息
//                System.out.println("Bucket 'system-bucket' already exists.");
//            }
            //文件上传
            //每个上传文件名称唯一
            //根据当前日期对上传文件进行分组
            String dateDir = DateUtil.format(new Date(), "yyyyMMdd");
            String uuid= UUID.randomUUID().toString().replaceAll("-","");
            String filename=dateDir+"/"+uuid+file.getOriginalFilename();

            minioClient.putObject(
                    PutObjectArgs.builder().bucket(minioProperties.getBucketName())
                            .object(filename)
                            .stream(file.getInputStream(),file.getSize(),-1)
                            .build()
            );
            // 构建fileUrl
            String fileUrl =minioProperties.getEndpointUrl() +"/"+ minioProperties.getBucketName()+"/"+filename;

            return fileUrl;
        }catch(Exception e){
            e.printStackTrace();
            throw new MyException(ResultCodeEnum.SYSTEM_ERROR);
        }
    }
}
