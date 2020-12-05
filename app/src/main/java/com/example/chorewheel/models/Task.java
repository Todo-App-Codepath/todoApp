package com.example.chorewheel.models;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseUser;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

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


    public String getDueDate() { return getDate(KEY_DUE_DATE).toString(); }

    // Gets date in MM/DD/YYYY format
    public String getFormattedDate() {
        DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
        Date currentDate = getDate(KEY_DUE_DATE);
        String currentDateString = dateFormat.format(currentDate);
        return currentDateString;
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
