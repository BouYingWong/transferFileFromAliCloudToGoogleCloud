package com.xlink.util;

import com.xlink.TransferFile;
import com.xlink.entity.AliFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

public class CompareUtil {
    private static final Logger logger = LoggerFactory.getLogger(CompareUtil.class);



    //文件进行MD5对比
    public static List<AliFile> compareFileMD5(List<AliFile> aliFiles){
        for (AliFile aliFile :aliFiles) {
            try {
                FileInputStream fileInputStream = new FileInputStream(new File(aliFile.getFilePath()));
                byte[] bytes = new byte[fileInputStream.available()];
                fileInputStream.read(bytes);

                MessageDigest md = MessageDigest.getInstance("MD5");
                String md5 = byteArrayToHexString(md.digest(bytes));

                if (md5.equals(aliFile.getFileMD5())){
                    System.out.println("same MD5");
                }else{
                    System.out.println("different MD5");
                }

            } catch (NoSuchAlgorithmException | IOException e) {
                e.printStackTrace();
            }
        }
        return aliFiles;
    }

    private static String byteArrayToHexString(byte[] bytes) {
        StringBuffer buf = new StringBuffer(bytes.length * 2);
        for (int i = 0; i < bytes.length; i++) {
            if (((int) bytes[i] & 0xff) < 0x10) {
                buf.append("0");
            }
            buf.append(Long.toString((int) bytes[i] & 0xff, 16));
        }
        return buf.toString();
    }
}
