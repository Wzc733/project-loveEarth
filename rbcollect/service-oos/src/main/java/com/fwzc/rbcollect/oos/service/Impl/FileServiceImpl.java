package com.fwzc.rbcollect.oos.service.Impl;


import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.CannedAccessControlList;
import com.aliyun.oss.model.OSSObjectSummary;
import com.aliyun.oss.model.ObjectListing;
import com.fwzc.rbcollect.oos.service.FileService;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * @Auther:wzc
 * @Data:2021/11/29 - 11 - 29 - 16:19
 */
@Service
public class FileServiceImpl implements FileService {
    @Value(value = "${aliyun.oss.endpoint1}")
    private String endpoint1;
    @Value(value = "${aliyun.oss.endpoint2}")
    private String endpoint2;
    @Value(value = "${aliyun.oss.keyId}")
    private String accessKeyId;
    @Value(value = "${aliyun.oss.keySecret}")
    private String accessKeySecret;
    @Value(value = "${aliyun.oss.bucketName1}")
    private String bucketName1;
    @Value(value = "${aliyun.oss.bucketName2}")
    private String bucketName2;

    @Resource
    private RedisTemplate redisTemplate;

    @Override
    public String upload(InputStream inputStream, String module, String fileName) {
        // 创建OSSClient实例。
        OSS ossClient = new OSSClientBuilder().build(endpoint1, accessKeyId, accessKeySecret);

        //判断Bucket存在
        if (!ossClient.doesBucketExist(bucketName1)) {
            // 不存在则创建存储空间。
            ossClient.createBucket(bucketName1);
            // 设置存储空间的访问权限为公共读。
            ossClient.setBucketAcl(bucketName1, CannedAccessControlList.PublicRead);
        }

        // Object完整路径中不能包含Bucket名称。
        //文件目录结构："icon/2021/06/06/UUID.jpg"
        //构建日期路径
        String timeFolder = new DateTime().toString("yyyy/MM/dd");
        //文件名生成
        // 有包括"."
        fileName = UUID.randomUUID().toString() + fileName.substring(fileName.lastIndexOf("."));
        String key = module + "/" + timeFolder + "/" + fileName;
        ossClient.putObject(bucketName1, key, inputStream);

        // 关闭OSSClient。
        ossClient.shutdown();
        //文件的url地址：
        //https://srb-file-wzc.oss-cn-guangzhou.aliyuncs.com/icon/xxxxxxxxx.PNG
        //https://{bucketname}.{endpoint}/{key}
        return "https://" + bucketName1 + "." + endpoint1 + "/" + key;
        //前端就可以根据这个url地址查看图片
    }

    @Override
    public void removeFile(String url) {

        //文件上传完成后传给前端的url地址
        //https://srb-file-wzc.oss-cn-guangzhou.aliyuncs.com/test/2021/11/29/6e1947b0-d685-40841a331ef8.jpg
        //要用的：test/2021/11/29/6e1947b0-d685-40841a331ef8.jpg
        String host = "https://" + bucketName1 + "." + endpoint1 + "/";
        String objectName = url.substring(host.length());

        // 创建OSSClient实例。
        OSS ossClient = new OSSClientBuilder().build(endpoint1, accessKeyId, accessKeySecret);

        // 删除文件或目录。如果要删除目录，目录必须为空。
        ossClient.deleteObject(bucketName1, objectName);

        // 关闭OSSClient。
        ossClient.shutdown();

    }

    @Override
    public List<String> getFileUrlList(String prefix) {
        // 创建OSSClient实例。
        OSS ossClient = new OSSClientBuilder().build(endpoint1, accessKeyId, accessKeySecret);
        List<String> list = new ArrayList<>();
//        ObjectListing objectListing = ossClient.listObjects(bucketName,"icon/rb");
        ObjectListing objectListing = ossClient.listObjects(bucketName1, prefix);

        //从redis查询缓存
         list = redisTemplate.opsForList().range("img:" + prefix, 0, -1);

        //命中，直接返回
        if (list!=null&&list.size()!=0) {
            return list;
        }
        //没命中，查询
       for (OSSObjectSummary objectSummary : objectListing.getObjectSummaries()) {
            list.add("https://" + bucketName1 + "." + endpoint1 + "/" + objectSummary.getKey());
        }

        //不存在，返回错误
        if (list==null){
            return null;
        }
        //存在,写入redis,返回
        list.forEach(item->{
            redisTemplate.opsForList().rightPush("img:" + prefix,item);
        });

        // 关闭OSSClient。
        ossClient.shutdown();
        return list;
    }

    @Override
    public String uploadImg(InputStream inputStream, String module, String fileName) {
        // 创建OSSClient实例。
        OSS ossClient = new OSSClientBuilder().build(endpoint2, accessKeyId, accessKeySecret);

        //判断Bucket存在
        if (!ossClient.doesBucketExist(bucketName2)) {
            // 不存在则创建存储空间。
            ossClient.createBucket(bucketName2);
            // 设置存储空间的访问权限为公共读。
            ossClient.setBucketAcl(bucketName2, CannedAccessControlList.PublicRead);
        }

        // Object完整路径中不能包含Bucket名称。
        //文件目录结构："icon/2021/06/06/UUID.jpg"
        //构建日期路径
        String timeFolder = new DateTime().toString("yyyy/MM/dd");
        //文件名生成
        // 有包括"."
        fileName = UUID.randomUUID().toString() + fileName.substring(fileName.lastIndexOf("."));
        String key = module + "/" + timeFolder + "/" + fileName;
        ossClient.putObject(bucketName2, key, inputStream);

        // 关闭OSSClient。
        ossClient.shutdown();
        //文件的url地址：
        //https://srb-file-wzc.oss-cn-guangzhou.aliyuncs.com/icon/xxxxxxxxx.PNG
        //https://{bucketname}.{endpoint}/{key}
        return "https://" + bucketName2 + "." + endpoint2 + "/" + key;
        //前端就可以根据这个url地址查看图片
    }

    @Override
    public void removeImg(String url) {
        //文件上传完成后传给前端的url地址
        //https://srb-file-wzc.oss-cn-guangzhou.aliyuncs.com/test/2021/11/29/6e1947b0-d685-40841a331ef8.jpg
        //要用的：test/2021/11/29/6e1947b0-d685-40841a331ef8.jpg
        String host = "https://" + bucketName2 + "." + endpoint2 + "/";
        String objectName = url.substring(host.length());

        // 创建OSSClient实例。
        OSS ossClient = new OSSClientBuilder().build(endpoint2, accessKeyId, accessKeySecret);

        // 删除文件或目录。如果要删除目录，目录必须为空。
        ossClient.deleteObject(bucketName2, objectName);

        // 关闭OSSClient。
        ossClient.shutdown();
    }




}
