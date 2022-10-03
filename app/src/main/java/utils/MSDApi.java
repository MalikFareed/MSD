package utils;

import android.app.Application;

public class MSDApi extends Application {

    private String username;
    private String userId;
    private static MSDApi instance;

    public static MSDApi getInstance() {
        if (instance == null)
            instance = new MSDApi();

        return instance;
    }

    public MSDApi() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
