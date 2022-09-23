package com.fwzc.rbcollect.oos.service;

import java.io.InputStream;
import java.util.List;

/**
 * @Auther:wzc
 * @Data:2021/11/29 - 11 - 29 - 16:18
 */
public interface FileService {
    String upload(InputStream inputStream, String module, String fileName);

    void removeFile(String url);


    List<String> getFileUrlList(String prefix);

    String uploadImg(InputStream inputStream, String module, String originalFilename);

    void removeImg(String url);

}
