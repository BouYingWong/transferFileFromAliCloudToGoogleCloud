package com.xlink.service;

import com.alibaba.fastjson.JSONObject;
import com.google.api.gax.paging.Page;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.*;
import com.xlink.TransferFile;
import com.xlink.entity.AliFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class GoogleCloudUpload {
    private static final Logger logger = LoggerFactory.getLogger(TransferFile.class);


    //id of gsc bucket
    private static final String bucketName = "";

    //新的下载地址前缀
    private static final String urlPrefix = "http://storage.googleapis.com/";

    private static Storage storage = null;
    //Google Cloud登录验证信息
    public static InputStream GoogleLoginMsg(){
        JSONObject json = new JSONObject();
        json.put("type","service_account");
        json.put("project_id","");
        json.put("private_key_id","");
        json.put("private_key","-----BEGIN PRIVATE KEY----------END PRIVATE KEY-----\n");
        json.put("client_email","");
        json.put("client_id","");
        json.put("auth_uri","https://accounts.google.com/o/oauth2/auth");
        json.put("token_uri","https://oauth2.googleapis.com/token");
        json.put("auth_provider_x509_cert_url","https://www.googleapis.com/oauth2/v1/certs");
        json.put("client_x509_cert_url","");
        return new ByteArrayInputStream(json.toJSONString().getBytes());
    }


    /**
     * 上传到google cloud 并返回新的下载地址
     * @param aliFiles 文件信息list
     * @return newUrl 新的下载地址
     * @throws IOException
     */
    public List<AliFile> uploadFile2GoogleCloud(List<AliFile> aliFiles) throws IOException {
        for (AliFile aliFile: aliFiles) {
            String objectName = aliFile.getFileName().split("com/")[1];
            String filePath = aliFile.getFilePath();

            GoogleCredentials googleCredentials = GoogleCredentials.fromStream(GoogleCloudUpload.GoogleLoginMsg());
            storage = StorageOptions.newBuilder().setCredentials(googleCredentials).build().getService();
            BlobId blobId = BlobId.of(bucketName, objectName);
            BlobInfo blobInfo = BlobInfo.newBuilder(blobId).build();
            Blob blob = storage.create(blobInfo, Files.readAllBytes(Paths.get(filePath)));
            String newUrl = urlPrefix + blob.getBucket() + "/" + blob.getName();
            aliFile.setNewUrl(newUrl);
        }
        return aliFiles;
    }





}
