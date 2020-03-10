package com.example.firebaesactivity;

import android.net.Uri;

public class Data {
    private String email,password,name;
    private Uri resultUri;

    public Data(){}
    public Data(String email, String password, String name, Uri resultUri) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.resultUri = resultUri;
    }
    public Uri getresultUri(){
       return resultUri;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getName() {
        return name;
    }
}
