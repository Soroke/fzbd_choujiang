package net.faxuan.objectInfo.userObject;

import java.io.File;

/**
 * Created with IntelliJ IDEA.
 * FileName: User.java
 * Author: song
 * Date: 2019/6/14 9:36
 * To change this template use File | Settings | File and Code Templates | Includes | File Header
 * Description:
 */
public class User {

    private String userAccount;
    private String passWord;
    private String sid;

    public User(int index) {
        this.sid="ceshiUserDefultSid";
        String user = "20190613";
//        int r = (int)(1+Math.random()*(1000-1+1));
        if (index<10) {
            user = user + "000" + index;
        } else if (index>=10 && index<100) {
            user = user + "00" + index;
        } else if (index>=100 && index<1000) {
            user = user + "0" + index;
        } else {
            user = user + index;
        }
        this.userAccount = user;
    }


    public String getUserAccount() {
        return userAccount;
    }

    public void setUserAccount(String userAccount) {
        this.userAccount = userAccount;
    }

    public String getPassWord() {
        return passWord;
    }

    public void setPassWord(String passWord) {
        this.passWord = passWord;
    }

    public String getSid() {
        return sid;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }
}
