package net.faxuan.objectInfo.drawObject;

import java.util.concurrent.Callable;

/**
 * Created with IntelliJ IDEA.
 * FileName: RequestDrawCallable.java
 * Author: song
 * Date: 2019/6/14 11:09
 * To change this template use File | Settings | File and Code Templates | Includes | File Header
 * Description:
 */
public class RequestDrawCallable implements Callable {
    private String userAccount;
    private String sid;
    private int activateID;
    public RequestDrawCallable(int activateID,String userAccount,String sid) {
        this.userAccount=userAccount;
        this.sid=sid;
        this.activateID=activateID;
    }
    @Override
    public Object call() throws Exception {
        return new RequestDraw().doRequest(activateID,userAccount,sid);
    }

}
