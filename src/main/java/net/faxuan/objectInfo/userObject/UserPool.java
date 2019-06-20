package net.faxuan.objectInfo.userObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * FileName: UserPool.java
 * Author: song
 * Date: 2019/6/14 9:36
 * To change this template use File | Settings | File and Code Templates | Includes | File Header
 * Description:
 */
public class UserPool {
    private List<User> users = new ArrayList<>();

    public UserPool(int userSum,int drawSum) {
        for (int i=1;i<=userSum;i++) {
            User user = new User(i);
            for (int j=0;j<drawSum;j++) {
                users.add(user);
            }
        }
    }
    public void addOneUser(User user) {
        users.add(user);
    }

    public User getOneUser( List<User> Iusers) {
        int count=Iusers.size();
        int index = (int)(1+Math.random()*(count-2));

        User user = Iusers.get(index);
        Iusers.remove(index);
        return user;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }
}
