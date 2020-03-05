package com.xlink.service;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.GetObjectRequest;
import com.aliyun.oss.model.OSSObjectSummary;
import com.aliyun.oss.model.ObjectListing;
import com.xlink.TransferFile;
import com.xlink.entity.AliFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.List;

public class AliDownload {
    private static final Logger logger = LoggerFactory.getLogger(TransferFile.class);


    //阿里云终端
    private final static String endpoint = "http://oss-cn-hangzhou.aliyuncs.com";

    //访问ID
    private final static String accessKeyId = "";

    //访问口令
    private final static String accessKeyPassword = "";

    //桶名称
    private final static String bucketName = "";

    //下载到本地的路径

    public List<AliFile> downloadFromAli(List<AliFile> aliFiles){
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeyPassword);
        String downloadPath = System.getProperty("user.dir") + File.separator + "downloadFile" + File.separator;

        //如果文件夹不存在
        File downloadFile = new File(downloadPath);
        if (!downloadFile.exists()){
            downloadFile.mkdirs();
        }
        //下载
        for (AliFile file: aliFiles){
            String objectName = file.getFileName().split("com/")[1];
            String localPath = downloadPath + objectName;
            file.setFilePath(localPath);
            ossClient.getObject(new GetObjectRequest(bucketName, objectName), new File(localPath));
        }
        ossClient.shutdown();
        return aliFiles;
    }




}
