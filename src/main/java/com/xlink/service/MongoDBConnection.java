package com.xlink.service;

import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.xlink.TransferFile;
import com.xlink.entity.AliFile;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * 数据库连接及对数据的CRUD
 */
public class MongoDBConnection {
    private static final Logger logger = LoggerFactory.getLogger(MongoDBConnection.class);


    private static final String userName = "";
    private static final String dataBase = "";
    private static final char[] password = "".toCharArray();
    private static final String dataBaseAddress = "dev3";
    private static final int dataBasePort = 27017;
    private static final String localAddress = "127.0.0.1";

    /**
     * 需要验证的数据库登录
     * @return
     */
    public static MongoDatabase mongoDbConnection(){
        MongoDatabase mgdb = null;
        try {
            MongoCredential credential = MongoCredential.createCredential(userName, dataBase, password);
            MongoClient mongoClient = new MongoClient(new ServerAddress(dataBaseAddress, dataBasePort),
                    Arrays.asList(credential));
            logger.info("connecting");
            mgdb = mongoClient.getDatabase("CORP_1007d2ad62cd6c28");
            logger.info("connected");
        }catch (Exception e){
            e.printStackTrace();
            logger.error("connect fail");
        }
        return mgdb;
    }

    /**
     * 本地数据库连接
     * @return
     */
    public static MongoDatabase localMongoDbConnection(){
        MongoDatabase mgdb = null;
        try {
            MongoClient mongoClient = new MongoClient(new ServerAddress(localAddress, dataBasePort));
            logger.info("连接中");
            mgdb = mongoClient.getDatabase("CORP_1007d2ad62cd6c28");
            logger.info("连接完成");
        }catch (Exception e){
            e.printStackTrace();
            logger.error("数据库连接失败");
        }
        return mgdb;
    }



    //查询某张表里面所有数据
    public static MongoCursor<Document> getFindCollection(String tableName){
        logger.info("query info");
        //远程数据库连接
 //       MongoCollection<Document> coll = MongoDBConnection.mongoDbConnection().getCollection(tableName);

        //本地的数据库连接
        MongoCollection<Document> coll = MongoDBConnection.localMongoDbConnection().getCollection(tableName);

        FindIterable findIterable = coll.find();
        MongoCursor<Document> cursor = findIterable.iterator();
        logger.info("queried");
        return cursor;
    }

    public List<AliFile> dataBinding(String tableName){
        MongoCursor<Document> cursor = getFindCollection(tableName);
        List<AliFile> aliFiles = new LinkedList<AliFile>();
        if(cursor != null ){
            while (cursor.hasNext()) {
                AliFile aliFile = new AliFile();
                Document document = cursor.next();
                String fileUrl = document.getString("file_url");
                String fileMD5 = document.getString("file_md5");
                String productId = document.getString("product_id");
                int fileSize = document.getInteger("file_size");
                aliFile.setFileName(fileUrl);
                aliFile.setFileMD5(fileMD5);
                aliFile.setFileSize(String.valueOf(fileSize));
                aliFile.setProduct_id(productId);
                aliFiles.add(aliFile);
            }
        }
        return aliFiles;
    }

    //更新数据库数据
    public static void queryAndUpdate(String tableName, List<AliFile> aliFiles){
        MongoCollection<Document> coll = MongoDBConnection.localMongoDbConnection().getCollection(tableName);

        for (AliFile file:aliFiles) {
            //远程数据库连接
//            MongoCollection<Document> coll = MongoDBConnection.mongoDbConnection().getCollection(tableName);

            //本地数据库连接
            //更新条件
            Bson filter = Filters.eq("product_id", file.getProduct_id());
            //指定修改的更新文档
            Document item1 = new Document("$set", new Document("from_version_url", file.getNewUrl()));
            Document item2 = new Document("$set", new Document("target_version_url", file.getNewUrl()));
            coll.updateOne(filter, item1);
            coll.updateOne(filter, item2);
        }

    }
}
