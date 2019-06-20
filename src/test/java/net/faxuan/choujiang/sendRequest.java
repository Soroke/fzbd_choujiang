package net.faxuan.choujiang;

import net.faxuan.choujiang.core.Response;
import net.faxuan.choujiang.exception.CheckException;
import net.faxuan.objectInfo.drawObject.DrawInfo;
import net.faxuan.choujiang.util.JsonHelper;

import static net.faxuan.choujiang.core.Http.get;
import static net.faxuan.choujiang.core.Http.post;

import java.text.SimpleDateFormat;
import java.util.*;

public class sendRequest {

    /**
     * 获取当前系统时间
     * @return 时间格式：2019-06-13 09:35:22
     */
    private static String getData() {
        Date now = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//可以方便地修改日期格式
        return dateFormat.format( now );
    }

    /**
     * 登录账号并获取该用户的sid
     * @param userAccount 用户名
     * @param password  密码
     * @return 用户的sid
     */
    private static String loginAndGetUserSid(String userAccount,String password) {
        Map<Object,Object> params = new HashMap<Object,Object>();
        params.put("userAccount",userAccount);
        params.put("userPassword",password);
        Response response = get("https://fzbd.t.faxuan.net/fzss/service/userService!doLogin.do", params);
        if (response.getStatusCode() == 200) {
            return JsonHelper.getValue(response.getBody(),"data.sid").toString();
        } else {
            new CheckException("用户登录失败");
        }
        return null;
    }


    public void login() {

        for (int index = 38;index<=1000;index++) {
            String user = "20190613";
            if (index<10) {
                user = user + "000" + index;
            } else if (index>=10 && index<100) {
                user = user + "00" + index;
            } else if (index>=100 && index<1000) {
                user = user + "0" + index;
            } else {
                user = user + index;
            }
            loginAndGetUserSid(user,"ceshi123");
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * 执行抽奖操作
     * @param activityId    活动ID
     * @param userAccount   抽奖用户用户名
     * @param passWord      抽奖用户密码
     * @param cjNumber      抽奖次数
     */
    public static void choujiang(int activityId,String userAccount,String passWord,int cjNumber) {
        List<DrawInfo> drawInfos = new ArrayList<>();
        String sid = loginAndGetUserSid(userAccount,passWord);
        Map<Object,Object> params = new HashMap<Object,Object>();
        params.put("activityId",activityId);
        params.put("userAccount",userAccount);
        params.put("sid",sid);
        for (int i=0;i<cjNumber;i++) {
            DrawInfo drawInfo = new DrawInfo();
            drawInfo.setUserAccount(userAccount);
            String body = post("https://fzbd.t.faxuan.net/fzss/service/drawService/doDraw.do",params).getBody();
            String prizeName = "";
            String msg = JsonHelper.getValue(body,"msg").toString();
            if (msg.equals("中奖")) {
                prizeName = JsonHelper.getValue(body,"data.prizeName").toString();
            } else {
                prizeName = msg;
            }
            drawInfo.setPrizeName(prizeName);
            drawInfo.setDrawTime(getData());
            drawInfos.add(drawInfo);
            try {
                Thread.sleep(1000);
            } catch (Exception e) {

            }
        }
        System.out.println("----------------------开始打印结果集----------------------");
        for (int i=0;i<drawInfos.size();i++){
            System.out.println(drawInfos.get(i));
        }
    }



    public void test() {
        choujiang(20,"AK41","ceshi123",10);
    }
}
