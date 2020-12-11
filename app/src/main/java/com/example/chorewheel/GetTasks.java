package com.example.chorewheel;

import com.example.chorewheel.models.Members;
import com.parse.ParseUser;

public interface GetTasks {
    public void queryUserTasks(ParseUser user);
    public void queryAllMemberTasks();
}
