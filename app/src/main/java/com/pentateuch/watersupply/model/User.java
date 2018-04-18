package com.pentateuch.watersupply.model;

/**
 * Created by varu on 03-04-2018.
 */

public class User {

    private String uid;
    private String token;
    private String name, number, email, address, pinCode;
    private boolean verified;

    public User(String uid, String name, String number, String email) {
        this.uid = uid;
        this.name = name;
        this.number = number;
        this.email = email;
    }

    public User() {

    }

    /**
     * @param name    name of user
     * @param number
     * @param email
     * @param address
     * @param pinCode
     */
    public User(String name, String number, String email, String address, String pinCode) {
        this.name = name;
        this.number = number;
        this.email = email;
        this.address = address;
        this.pinCode = pinCode;

    }

    public User(String name, String number, String email, String address, String pinCode, boolean verified) {
        this.name = name;
        this.number = number;
        this.email = email;
        this.address = address;
        this.pinCode = pinCode;
        this.verified = verified;
    }

    public User(String name, String number, String email, String address, String pinCode, String token, boolean verified) {
        this.name = name;
        this.number = number;
        this.email = email;
        this.address = address;
        this.pinCode = pinCode;
        this.token = token;
        this.verified = verified;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPinCode() {
        return pinCode;
    }

    public void setPinCode(String pinCode) {
        this.pinCode = pinCode;
    }

    public boolean isVerified() {
        return verified;
    }

    public void setVerified(boolean verified) {
        this.verified = verified;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}

