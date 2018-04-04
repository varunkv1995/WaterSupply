package com.pentateuch.watersupply.model;

/**
 * Created by varu on 03-04-2018.
 */

public class User {

    private String uid;
    private String name, number, email, address, pincode;
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
     * @param pincode
     */
    public User(String name, String number, String email, String address, String pincode) {
        this.name = name;
        this.number = number;
        this.email = email;
        this.address = address;
        this.pincode = pincode;

    }

    public User(String name, String number, String email, String address, String pincode, boolean verified) {
        this.name = name;
        this.number = number;
        this.email = email;
        this.address = address;
        this.pincode = pincode;
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

    public String getPincode() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }

    public boolean isVerified() {
        return verified;
    }

    public String getUid() {
        return uid;
    }
}

