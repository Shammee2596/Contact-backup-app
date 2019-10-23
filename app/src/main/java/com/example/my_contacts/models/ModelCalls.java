package com.example.my_contacts.models;

public class ModelCalls {
    private String name, duration, date, number;

    public ModelCalls(String name,String number, String duration, String date) {
        this.name = name;
        this.number = number;
        this.duration = duration;
        this.date = date;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getName() {
        return name;
    }

    public void setname(String name) {
        this.name = name;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
