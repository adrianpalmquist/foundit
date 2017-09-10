package com.esh.group7.foundit;

/**
 * Created by Adrian on 2017-09-10.
 */

public class DataHandler {

    private String name = "";
    private String address = "";
    private String phone = "";
    private String mail = "";

    private String nameField = "";
    private String addressField = "";
    private String phoneField = "";
    private String mailField = "";

    public void setName(String s){
        name = s;
    }
    public void setAddress(String s){
        address = s;
    }
    public void setPhone(String s){
        phone = s;
    }
    public void setMail(String s){
        mail = s;
    }
    public String getName() {
        return name;
    }
    public String getAddress(){
        return address;
    }
    public String getPhone(){
        return phone;
    }
    public String getMail(){
        return mail;
    }

    public void setNameField(String s){
        nameField = s;
    }
    public void setAddressField(String s){
        addressField = s;
    }
    public void setPhoneField(String s){
        phoneField = s;
    }
    public void setMailField(String s){
        mailField = s;
    }
    public String getNameField() {
        return nameField;
    }
    public String getAddressField(){
        return addressField;
    }
    public String getPhoneField(){
        return phoneField;
    }
    public String getMailField(){
        return mailField;
    }
}
