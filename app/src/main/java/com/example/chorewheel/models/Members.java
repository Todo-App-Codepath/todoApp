package com.example.chorewheel.models;

import java.util.List;

public class Members {
    public Members(List<User> userList) {
        this.userList = userList;
    }

    List<User> userList;

    public void setUserList(List<User> userList){
        this.userList = userList;
    }
    public List<User> getUserList(){
        return userList;
    }

}
