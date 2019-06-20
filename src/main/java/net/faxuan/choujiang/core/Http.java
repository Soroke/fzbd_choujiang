package net.faxuan.choujiang.core;

import com.alibaba.fastjson.JSONException;
import net.faxuan.choujiang.exception.CheckException;
import net.faxuan.choujiang.util.JsonHelper;
import org.apache.http.*;
import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.*;

import static java.nio.charset.StandardCharsets.UTF_8;

/**
 * Created by song on 2018/12/25.
 */
public class Http {

    /**
     * Cookies
     */
    private static CookieStore cookieStore = null;

    /**
     * 编码默认utf-8
     */
    private static String encode = "utf-8";

    /**
     * log4j打log
     */
    private static Logger log = Logger.getLogger(Http.class);

    /**
     * 请求超时设置时长
     * 单位秒
     */
    private static int timeOut = 5;

    /**
     * 构造httprequest设置
     * 设置请求和传输超时时间
     */
    private static RequestConfig config = RequestConfig.custom().setSocketTimeout(timeOut * 1000).setConnectTimeout(timeOut * 1000).setConnectionRequestTimeout(timeOut * 1000).build();


    /**
     * 发送get请求
     *
     * @return
     *  Request对象
     *  包含：
     *      请求类型
     *      url
     *      返回结果
     *      状态码
     *      响应时间
     */
    public static Response get(String url,Map<Object,Object> ... params) {

        if (cookieStore == null)  cookieStore = new BasicCookieStore();
        HttpClient httpClient =  HttpClientBuilder.create().setDefaultRequestConfig(config).setDefaultCookieStore(cookieStore).build();

        String baseUrl = url;
        //请求返回实体对象
        Response rsp = new Response();
        //用于计算接口请求响应时间
        Long startTime = 0L;
        long runTime = 0L;
        log.info("------------------------开始请求------------------------");
        log.info("接口类型：get");
        log.info("接口URL为：" + url);
        /**
         * create by song
         * 如果有参数
         * 设置get请求参数 并和url进行拼接为完整的请求地址
         * 设置请求参数的编码 格式为utf-8
         */
        Map<Object,Object> pam = new HashMap<Object,Object>();
        if (!(params.length == 0)) {
            pam = params[0];
        }
        if(!pam.isEmpty()) {
            String param = "";
            for (Map.Entry<Object,Object> entry : pam.entrySet()) {
                if(!param.equals("")){
                    param = param + "&";
                }
                try{
                    param += URLEncoder.encode(entry.getKey().toString(),encode)+"="+URLEncoder.encode(entry.getValue().toString(),encode);
                } catch (IOException ioe) {
                    log.error("get请求参数设置utf-8编码时出错");
                    ioe.printStackTrace();
                }
                //打印请求参数信息
                log.info("参数：\"" +entry.getKey() + "\":\"" + entry.getValue() + "\"");
            }
            url = url + "?" + param;
        }
        log.info("测试全量url：" + url);
        HttpGet httpGet = null;
        HttpResponse response = null;
        int count = 0;
        /**
         * 接口测试不通过时，暂停1s继续请求；
         * 如果三次都不通过再判定测试失败
         */
        while(count < 3) {
            httpGet = new HttpGet(url);
            //请求开始时间
            startTime = new Date().getTime();
            try {
                response = httpClient.execute(httpGet);
                rsp.setBody(EntityUtils.toString(response.getEntity(), encode));
                runTime = new Date().getTime() - startTime;
            } catch(IOException ioe) {
                log.error("get请求发送时出错！");
                ioe.printStackTrace();
                return null;
            } catch (NullPointerException npe) {
                log.error("没有设置URL参数！");
                npe.printStackTrace();
                return null;
            }
            if (response.getStatusLine().getStatusCode() != 200) {
                count ++;
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (count == 3) {
                    log.error("接口:" + baseUrl + "请求返回结果为失败；已重试3次，接口测试失败。");
                } else {
                    log.error("接口:" + baseUrl + "请求返回结果为失败；开始第" + count + "次重试");
                }
            } else {
                count = 5;
            }
        }
        log.info("接口响应时间为：" + runTime + "ms");
        log.info("------------------------请求结束------------------------");
        rsp.setUrl(url);
        rsp.setRunTime(runTime);
        rsp.setResponseType(ResponseType.GET);
        rsp.setCookies(cookieStore);
        //保存header
        Header[] headers = response.getAllHeaders();
        Map<Object,Object> hashMap = new HashMap<Object,Object>();
        for (Header header:headers) {
            hashMap.put(header.getName(),header.getValue());
        }
        rsp.setHeaders(hashMap);
        rsp.setStatusCode(response.getStatusLine().getStatusCode());
        log.info(rsp);
        return rsp;
    }

    /**
     * 发送post请求
     *
     * @return
     *  Request对象
     *  包含：
     *      请求类型
     *      url
     *      参数
     *      返回结果
     *      状态码
     *      响应时间
     */
    public static Response post(String url,Map<Object,Object> ... params) {
        if (cookieStore == null)  cookieStore = new BasicCookieStore();
        HttpClient httpClient =  HttpClientBuilder.create().setDefaultRequestConfig(config).setDefaultCookieStore(cookieStore).build();
        //请求返回实体对象
        Response rsp = new Response();
        //用于计算接口请求响应时间
        Long startTime = 0L;
        long runTime = 0L;
        log.info("------------------------开始请求------------------------");
        log.info("接口类型：post");
        log.info("接口URL为：" + url);
        HttpPost httpPost = new HttpPost(url);
//        addHeaderToHttpRequest(httpPost);
        /**
         * 检查参数是否为空
         * 如果存在参数循环添加
         */
        Map<Object,Object> pam = new HashMap<Object,Object>();
        if (!(params.length == 0)) {
            pam = params[0];
        }
        if(!pam.isEmpty()) {
            List<NameValuePair> param = new ArrayList<NameValuePair>();
            for (Map.Entry<Object,Object> entry : pam.entrySet()) {
                log.info("参数：\"" +entry.getKey() + "\":\"" + entry.getValue() + "\"");
                param.add(new BasicNameValuePair(entry.getKey().toString(), entry.getValue().toString()));
            }
            HttpEntity entity = new UrlEncodedFormEntity(param,UTF_8);
            httpPost.setEntity(entity);
        }
        int count = 0;
        HttpResponse response = null;
        /**
         * 接口测试不通过时，暂停1s继续请求；
         * 如果三次都不通过再判定测试失败
         */
        String responseBodyInfo = "";
        while(count < 3 ) {
            //计算请求开始时间
            startTime = new Date().getTime();
            try {
                response = httpClient.execute(httpPost);
                responseBodyInfo = EntityUtils.toString(response.getEntity(), encode);
                rsp.setBody(responseBodyInfo);
                runTime = new Date().getTime() - startTime;
            } catch(IOException ioe) {
                log.error("post请求发送时出错");
                rsp.setTestResult(false);
//                ioe.printStackTrace();
                return rsp;
            }catch (NullPointerException npe) {
                log.error("没有设置URL参数！");
                rsp.setTestResult(false);
//                npe.printStackTrace();
                return rsp;
            }
            log.info("接口响应时间为：" + runTime + "ms");

            log.info("------------------------请求结束------------------------");
            if ( response.getStatusLine().getStatusCode() != 200 ) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                count ++;
                if (count == 3) {
                    throw new CheckException("接口:" + url + "请求返回结果为失败；已重试3次，接口测试失败。");
                } else {
                    log.error("接口:" + url + "请求返回结果为失败；开始第" + count + "次重试");
                }
            } else {
                count = 5;
            }
        }
        log.info("接口响应内容为：" + responseBodyInfo);
        rsp.setRunTime(runTime);
        rsp.setCookies(cookieStore);
        //保存header
        Header[] headers = response.getAllHeaders();
        Map<Object,Object> hashMap= new HashMap<Object,Object>();
        for (Header header:headers) {
            hashMap.put(header.getName(),header.getValue());
        }
        rsp.setHeaders(hashMap);
        rsp.setStatusCode(response.getStatusLine().getStatusCode());
//        log.info(rsp);
        rsp.setTestResult(true);
        return rsp;
    }

}
