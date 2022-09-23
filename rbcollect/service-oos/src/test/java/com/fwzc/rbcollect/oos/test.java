package com.fwzc.rbcollect.oos;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.OSSObjectSummary;
import com.aliyun.oss.model.ObjectListing;
import org.apache.http.Consts;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


/**
 * @ClassName : test
 * @Description : TODO
 * @Author : James
 * @Date : 2022/4/4 10:52
 * @Version 1.0
 */
public class test {
    private String endpoint="oss-cn-shanghai.aliyuncs.com";
    private String accessKeyId="LTAI5t625494yiV9WQ8cgfqz";
    private String accessKeySecret="MlHHmljnbgj4OkPaUx52LxkiPvaoJp";
    private String bucketName="rbcollect-file-wzc";
    private String APIKEY="fada8dc3ee7f20aea044697464e4cb5a";

    @Test
    public void test(){
        // 创建OSSClient实例。
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);

// ossClient.listObjects返回ObjectListing实例，包含此次listObject请求的返回结果。
        ObjectListing objectListing = ossClient.listObjects(bucketName,"icon/rb");
// objectListing.getObjectSummaries获取所有文件的描述信息。
        for (OSSObjectSummary objectSummary : objectListing.getObjectSummaries()) {
            System.out.println(objectSummary.getKey());
        }

        // 关闭OSSClient。
        ossClient.shutdown();
    }

    @Test
    public void testSort() throws IOException {
        String httpUrl = "http://api.tianapi.com/lajifenlei/index?key=" + APIKEY + "&word=眼镜&mode="+1;

        BufferedReader reader = null;
        String result = null;
//        StringBuffer sbf = new StringBuffer();
        URL url = new URL(httpUrl);
        HttpURLConnection connection = (HttpURLConnection) url
                .openConnection();
        connection.setRequestMethod("GET");
        InputStream is = connection.getInputStream();
        reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
        String strRead = null;
        while ((strRead = reader.readLine()) != null) {
            System.out.println(strRead);
        }
        reader.close();
//        System.out.println(result);
    }
    @Test
    public void test2()  {
        String httpUrl = "http://api.tianapi.com/imglajifenlei/index?key="+APIKEY+"&img=https://rbcollect-image-recognition.oss-cn-shanghai.aliyuncs.com/imgRecognition/2022/04/05/c5fa1896-0b53-4a3c-90c9-2b3571e485f2.png";
        BufferedReader reader = null;
        String result = null;
        StringBuffer sbf = new StringBuffer();

        try {
            URL url = new URL(httpUrl);
            HttpURLConnection connection = (HttpURLConnection) url
                    .openConnection();
            connection.setRequestMethod("GET");
            InputStream is = connection.getInputStream();
            reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
            String strRead = null;
            while ((strRead = reader.readLine()) != null) {
                sbf.append(strRead);
            }
            reader.close();
            result = sbf.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println(result);
    }
    @Test
    public void  test3() throws IOException {
        //新建一个客户对象
CloseableHttpClient client = HttpClients.createDefault();
//新建一个HttpPost请求的对象 --并将uri传给这个对象
HttpPost post = new HttpPost("http://api.tianapi.com/imglajifenlei/index");
//使用NameValuePair  键值对  对参数进行打包
List<NameValuePair> list = new ArrayList<>();
list.add(new BasicNameValuePair("key",APIKEY));
list.add(new BasicNameValuePair("imgurl","https://rbcollect-image-recognition.oss-cn-shanghai.aliyuncs.com/imgRecognition/2022/04/05/c5fa1896-0b53-4a3c-90c9-2b3571e485f2.png"));
//4.对打包好的参数，使用UrlEncodedFormEntity工具类生成实体的类型数据
UrlEncodedFormEntity entity = new UrlEncodedFormEntity(list, Consts.UTF_8);
//5.将含有请求参数的实体对象放到post请求中
post.setEntity(entity);
//6.新建一个响应对象来接收客户端执行post的结果
CloseableHttpResponse response = client.execute(post);
//7.从响应对象中提取需要的结果-->实际结果,与预期结果进行对比
if(response.getStatusLine().getStatusCode() == 200){
System.out.println(EntityUtils.toString(response.getEntity()));
}
 }

    }




