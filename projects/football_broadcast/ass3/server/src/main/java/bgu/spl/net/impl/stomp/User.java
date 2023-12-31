package bgu.spl.net.impl.stomp;

import java.util.*;

public class User {
    private String userName;
    private String password;
    private boolean logInStatus;
    private Integer currConnId = -1;

    public User(String userName, String password, int connectionId){
        this.userName = userName;
        this.logInStatus = true;
        this.password = password;
        this.currConnId = connectionId;
        
    }

    public boolean isLoggedIn() {
        return logInStatus;
    }

    public String getPassword() {
        return password;
    }

    public void setStatus(boolean newLogInStatus){
        this.logInStatus = newLogInStatus;
    }
   
    public void setConnId(Integer currConnId) {
        this.currConnId = currConnId;
    }

    public void logIn (Integer connId){
        this.setStatus(true);
        this.setConnId(connId);
    }
}
