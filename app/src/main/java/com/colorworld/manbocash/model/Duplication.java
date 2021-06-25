package com.colorworld.manbocash.model;

public class Duplication {

    public String result;
    private String email;
    private String nickname;
    private String photourl;
    private String refercode;
    private String referee;
    private String registeredby;
    private String uid;
    private String height;
    private String weight;
    private String creationTimestamp;
    private String lastSignInTimestamp;



    public Duplication(String result, String email, String nickName, String photoUrl, String referCode, String referee, String registeredBy, String uid, String height, String weight, String creationTimestamp, String lastSignInTimestamp) {
        this.result = result;
        this.email = email;
        this.nickname = nickName;
        this.photourl = photoUrl;
        this.refercode = referCode;
        this.referee = referee;
        this.registeredby = registeredBy;
        this.uid = uid;
        this.height = height;
        this.weight = weight;
        this.creationTimestamp = creationTimestamp;
        this.lastSignInTimestamp = lastSignInTimestamp;
    }


    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNickName() {
        return nickname;
    }

    public void setNickName(String nickName) {
        this.nickname = nickName;
    }

    public String getPhotoUrl() {
        return photourl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photourl = photoUrl;
    }

    public String getReferCode() {
        return refercode;
    }

    public void setReferCode(String referCode) {
        this.refercode = referCode;
    }

    public String getReferee() {
        return referee;
    }

    public void setReferee(String referee) {
        this.referee = referee;
    }

    public String getRegisteredBy() {
        return registeredby;
    }

    public void setRegisteredBy(String registeredBy) {
        this.registeredby = registeredBy;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getCreationTimestamp() {
        return creationTimestamp;
    }

    public void setCreationTimestamp(String creationTimestamp) {
        this.creationTimestamp = creationTimestamp;
    }

    public String getLastSignInTimestamp() {
        return lastSignInTimestamp;
    }

    public void setLastSignInTimestamp(String lastSignInTimestamp) {
        this.lastSignInTimestamp = lastSignInTimestamp;
    }


}
