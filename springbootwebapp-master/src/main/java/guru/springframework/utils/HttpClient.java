package guru.springframework.utils;


import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;


public class HttpClient {

    public static String getSimple(String url) throws IOException {
       CloseableHttpClient httpclient = HttpClients.createDefault();


       CloseableHttpResponse response = null;
       try {
           // 创建URI对象，并且设置请求参数
           URI uri = new URIBuilder(url).build();
           // 创建http GET请求
           HttpGet httpGet = new HttpGet(uri);
           // 执行请求
           response = httpclient.execute(httpGet);
           // 判断返回状态是否为200
           if (response.getStatusLine().getStatusCode() == 200) {
               // 解析响应数据
               String content = EntityUtils.toString(response.getEntity(), "UTF-8");
               System.out.println(content);
               return content;
           }
       } catch (IOException e) {
           e.printStackTrace();
           httpclient.close();
           return null;
       } catch (URISyntaxException e) {
           e.printStackTrace();
           return null;
       }
       return null;


//           httpclient.close();


   }

}
