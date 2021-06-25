package com.colorworld.manbocash.model;

import java.io.Serializable;

public class Member implements Serializable {

    private int cash;
    private int steps;
    private String email;
    private String nickName;
    private String password;
    private String photoUrl;
    private String referCode;
    private String referee;
    private String registeredBy;
    private String uid;
    private String creationTimestamp;
    private String lastSignInTimestamp;


    public Member() {

    }


    public Member(int cash, int steps, String email, String nickName, String password, String photoUrl, String referCode, String referee, String registeredBy, String uid, String creationTimestamp, String lastSignInTimestamp) {
        this.cash = cash;
        this.steps = steps;
        this.email = email;
        this.nickName = nickName;
        this.password = password;
        this.photoUrl = photoUrl;
        this.referCode = referCode;
        this.referee = referee;
        this.registeredBy = registeredBy;
        this.uid = uid;
        this.creationTimestamp = creationTimestamp;
        this.lastSignInTimestamp = lastSignInTimestamp;
    }

    public String getLastSignInTimestamp() {
        return lastSignInTimestamp;
    }

    public void setLastSignInTimestamp(String lastSignInTimestamp) {
        this.lastSignInTimestamp = lastSignInTimestamp;
    }


    public String getCreationTimestamp() {
        return creationTimestamp;
    }

    public void setCreationTimestamp(String creationTimestamp) {
        this.creationTimestamp = creationTimestamp;
    }





    public int getCash() {
        return cash;
    }

    public void setCash(int cash) {
        this.cash = cash;
    }

    public int getSteps() {
        return steps;
    }

    public void setSteps(int steps) {
        this.steps = steps;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public String getReferCode() {
        return referCode;
    }

    public void setReferCode(String referCode) {
        this.referCode = referCode;
    }

    public String getReferee() {
        return referee;
    }

    public void setReferee(String referee) {
        this.referee = referee;
    }

    public String getRegisteredBy() {
        return registeredBy;
    }

    public void setRegisteredBy(String registeredBy) {
        this.registeredBy = registeredBy;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }


}
