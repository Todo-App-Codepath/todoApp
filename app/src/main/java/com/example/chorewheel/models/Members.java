package com.example.chorewheel.models;

import com.parse.Parse;
import com.parse.ParseUser;

import java.util.List;

public class Members {
    List<ParseUser> userList;
    public Members(List<ParseUser> userList) {
        this.userList = userList;
    }
    public void setUserList(List<ParseUser> userList){
        this.userList = userList;
    }
    public List<ParseUser> getUserList(){
        return userList;
    }

}
