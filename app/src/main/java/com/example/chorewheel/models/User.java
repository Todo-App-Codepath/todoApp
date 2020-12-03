package com.example.chorewheel.models;

import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;

@ParseClassName("User")
public class User extends ParseObject {
    public static final String KEY_ID = "objectId";
    public static final String KEY_USERNAME = "username";
    public static final String KEY_IMAGE = "image";
    public static final String KEY_EMAIL = "email";
    public static final String KEY_FIRST_NAME = "firstName";
    public static final String KEY_LAST_NAME = "lastName";


    public String getID (){
        return getString(KEY_ID);
    }
    public void setImage(ParseFile file){
        put(KEY_IMAGE, file);
    }
    public ParseFile getImage(){
        return getParseFile(KEY_IMAGE);
    }
    public String getUsername(){
        return getString(KEY_USERNAME);
    }
}
