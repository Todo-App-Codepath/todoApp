package com.example.chorewheel.models;

import com.parse.Parse;
import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseUser;

@ParseClassName("Task")
public class Task extends ParseObject {
    public static final String KEY_OBJECT_ID = "objectId";
    public static final String KEY_CREATED_AT = "createdAt";
    public static final String KEY_UPDATED_AT = "updatedAt";
    public static final String KEY_TASK_NAME = "name";
    public static final String KEY_DESCRIPTION = "description";
    public static final String KEY_DUE_DATE = "dueDate";
    public static final String KEY_USER = "userID";
    public static final String KEY_CHECKED = "checked";

    public String getObjectId(){
        return getString(KEY_OBJECT_ID);
    }

    public  String getTaskName() {
        return getString(KEY_TASK_NAME);
    }

    public void setTaskName(String taskName){
        put(KEY_TASK_NAME, taskName);
    }

    public  String getDESCRIPTION() {
        return getString(KEY_DESCRIPTION);
    }
    public void setDescription(String description){
        put(KEY_DESCRIPTION, description);
    }

    public String getDueDate() {
        return getString(KEY_DUE_DATE);
    }

    public void setDueDate(String dueDate ){
        put(KEY_DUE_DATE, dueDate);

    }
    public ParseUser getUser() {
        return getParseUser(KEY_USER);
    }
    public void setUser(ParseUser user){
        put(KEY_USER, user);
    }
    public void setChecked(Boolean bool){
        put(KEY_CHECKED, bool);
    }
    public boolean getChecked(){
        return getBoolean(KEY_CHECKED);
    }
}
