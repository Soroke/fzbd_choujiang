package net.faxuan.objectInfo.drawObject;

/**
 * Created with IntelliJ IDEA.
 * FileName: DrawInfo.java
 * Author: song
 * Date: 2019/6/13 10:18
 * To change this template use File | Settings | File and Code Templates | Includes | File Header
 * Description: 中奖信息
 */
public class DrawInfo {
    private String userAccount;
    private String prizeName;
    private String drawTime;

    public String getUserAccount() {
        return userAccount;
    }

    public void setUserAccount(String userAccount) {
        this.userAccount = userAccount;
    }

    public String getPrizeName() {
        return prizeName;
    }

    public void setPrizeName(String prizeName) {
        this.prizeName = prizeName;
    }

    public String getDrawTime() {
        return drawTime;
    }

    public void setDrawTime(String drawTime) {
        this.drawTime = drawTime;
    }

    @Override
    public String toString() {
        return "用户名：" + userAccount + "\t奖品名称：" + prizeName + "\t抽奖时间：" + drawTime;
    }
}
