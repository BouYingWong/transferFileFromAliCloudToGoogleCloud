package com.xlink;

import com.mongodb.client.MongoCursor;
import com.xlink.entity.AliFile;
import com.xlink.service.AliDownload;
import com.xlink.service.GoogleCloudUpload;
import com.xlink.service.MongoDBConnection;
import com.xlink.util.CompareUtil;
import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.LinkedList;
import java.util.List;


public class TransferFile {
    private static final Logger logger = LoggerFactory.getLogger(TransferFile.class);


    public static void main(String[] args)  {
        try{
            //1，连接数据库并拿取设备信息
            MongoDBConnection conn = new MongoDBConnection();
            List<AliFile> aliFiles = conn.dataBinding("firmware");

            //2.阿里云下载部分
            logger.info("download file from ali");
            AliDownload aliDownload = new AliDownload();
            aliDownload.downloadFromAli(aliFiles);
            logger.info("download complete");
            //3.进行文件大小检验
            aliFiles = CompareUtil.compareFileMD5(aliFiles);

            //4.上传到Google cloud oss
            logger.info("upload to google cloud");
            GoogleCloudUpload googleCloudUpload = new GoogleCloudUpload();
            aliFiles = googleCloudUpload.uploadFile2GoogleCloud(aliFiles);
            logger.info("upload complete");

            //5.修改数据库数据
            logger.info("update database");
           MongoDBConnection.queryAndUpdate("upgrade_task", aliFiles);
            logger.info("update complete");
        }catch (Exception e){
            e.printStackTrace();
        }
    }


}
