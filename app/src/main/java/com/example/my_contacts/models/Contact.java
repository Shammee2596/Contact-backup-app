package com.example.my_contacts.models;

public class Contact {
    private String first_name;
    private String last_name;
    private String phone;
    private String email;
    private String label;

    public Contact(String first_name, String last_name, String phone, String email, String label){
        this.first_name = first_name;
        this.last_name = last_name;
        this.phone = phone;
        this.email = email;
        this.label = label;
    }
    public Contact(){

    }
    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() { return last_name; }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }


}
