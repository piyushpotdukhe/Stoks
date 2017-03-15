package com.example.piyushpotdukhe.AIS.com.stox.userinfo;

/**
 * Created by piyush.potdukhe on 3/14/2017.
 */

public class AuthenticatedUserInfo {

    //add more data
    String mUserName;
    Boolean isLoggedIn = false;

    private static AuthenticatedUserInfo mAuthUserInfo = null;
    public static AuthenticatedUserInfo getAuthUserObject(){
        if (mAuthUserInfo == null) {
            mAuthUserInfo = new AuthenticatedUserInfo("EMPTY");
        }
        return mAuthUserInfo;
    }

    AuthenticatedUserInfo(String userName){
        this.mUserName = userName;
    }

    public void setUserName(String userName){
        this.mUserName = userName;
    }

    public String getUserName(){
        return this.mUserName;
    }

    public boolean getIsLoggedIn() {
        return this.isLoggedIn;
    }

    public void setIsLoggedIn(boolean loggedIn) {
        this.isLoggedIn = loggedIn;
    }
}
