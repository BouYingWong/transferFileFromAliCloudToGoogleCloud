package com.xlink.entity;

public class AliFile {

    //文件名
    private String fileName;

    //文件MD5
    private String fileMD5;

    //文件本地路径
    private String filePath;

    //上传后文件新的下载地址
    private String newUrl;

    //产品ID
    private String product_id;

    public String getNewUrl() {
        return newUrl;
    }

    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    public void setNewUrl(String newUrl) {
        this.newUrl = newUrl;
    }

    private String fileSize;

    @Override
    public String toString() {
        return "AliFile{" +
                "fileName='" + fileName + '\'' +
                ", fileMD5='" + fileMD5 + '\'' +
                ", filePath='" + filePath + '\'' +
                ", newUrl='" + newUrl + '\'' +
                ", product_id='" + product_id + '\'' +
                ", fileSize='" + fileSize + '\'' +
                '}';
    }

    public String getFileSize() {
        return fileSize;
    }

    public void setFileSize(String fileSize) {
        this.fileSize = fileSize;
    }

    public AliFile(String fileName, String fileMD5, String filePath, String fileSize) {
        this.fileName = fileName;
        this.fileMD5 = fileMD5;
        this.filePath = filePath;
        this.fileSize = fileSize;
    }



    public AliFile() {
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileMD5() {
        return fileMD5;
    }

    public void setFileMD5(String fileMD5) {
        this.fileMD5 = fileMD5;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

}
