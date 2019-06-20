package net.faxuan.objectInfo.drawObject;

import com.alibaba.fastjson.JSONException;
import net.faxuan.choujiang.core.Response;
import net.faxuan.choujiang.util.GetProperties;
import net.faxuan.choujiang.util.JsonHelper;

import java.text.SimpleDateFormat;
import java.util.*;

import static net.faxuan.choujiang.core.Http.post;

/**
 * Created with IntelliJ IDEA.
 * FileName: RequestDraw.java
 * Author: song
 * Date: 2019/6/14 9:47
 * To change this template use File | Settings | File and Code Templates | Includes | File Header
 * Description:
 */
public class RequestDraw {

    //配置文件接口信息
    GetProperties getProperties = new GetProperties().getProperties();
    String url;
    String[] paramsName;

    public void getParam() {
        String drawInterface= getProperties.getDrawUrl();
        String[] dl = drawInterface.split("\\?");
        url = dl[0];
        paramsName = dl[1].split("&");
    }

    private String userAccount;
    private String sid;
    private String prizeName;
    private String time;

    public RequestDraw doRequest(int activateID,String userAccount,String sid) {
        getParam();
        Map<Object,Object> params = new HashMap<Object,Object>();
        params.put(paramsName[0],activateID);
        params.put(paramsName[1],userAccount);
        params.put(paramsName[2],sid);
        Response response = post(url,params);
        String body = "";
        String prizeName = "";
        int requestCount = 0;
        while (requestCount < 3) {
            if (! response.getTestResult() && requestCount != 2) {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                response = post(url,params);
                requestCount ++;
            } else if (! response.getTestResult() && requestCount == 2) {
                prizeName = "接口请求失败";
                requestCount = 4;
            }else if (response.getTestResult()) {
                body = response.getBody();
                String msg = JsonHelper.getValue(body,"msg").toString();
                if (msg.equals("中奖")) {
                    prizeName = JsonHelper.getValue(body,"data.prizeName").toString();
                }
                requestCount = 4;
            } else {
                prizeName = "接口请求失败";
            }
        }

        this.userAccount=userAccount;
        this.prizeName=prizeName;
        this.sid=sid;
        setTime();
        return this;
    }
    public String getUserAccount() {
        return userAccount;
    }

    public void setUserAccount(String userAccount) {
        this.userAccount = userAccount;
    }

    public String getSid() {
        return sid;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }

    public String getPrizeName() {
        return prizeName;
    }

    public void setPrizeName(String prizeName) {
        this.prizeName = prizeName;
    }

    public String getTime() {
        return time;
    }

    public void setTime() {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        this.time = df.format(new Date());
    }

    @Override
    public String toString() {
        return "用户：" + userAccount + "\t奖品名称：" + prizeName + "\t抽奖时间：" + time;
    }
}
