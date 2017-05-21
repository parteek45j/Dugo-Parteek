package com.android.parteek.dugo;

import java.io.Serializable;

/**
 * Created by Suraj on 4/25/2017.
 */

public class UserBean implements Serializable {
    int id;
    String name,phone,gender,city,age,blooddGroup,password,date,time;

    public UserBean() {

    }

    public UserBean(int id, String name, String phone, String gender, String city,String blooddGroup,String age, String password, String date, String time) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.gender = gender;
        this.city = city;
        this.age=age;
        this.blooddGroup=blooddGroup;
        this.password = password;

        this.date = date;
        this.time = time;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getBlooddGroup() {
        return blooddGroup;
    }

    public void setBloodGroup(String blooddGroup) {
        this.blooddGroup = blooddGroup;
    }

    @Override
    public String toString() {
        return "\n id=" + id+
                "\n name=" + name +
                "\n phone=" + phone +
                "\n gender=" + gender +
                "\n city=" + city +
                "\n age=" + age +
                "\n bloodGroup=" + blooddGroup +
                "\n password=" + password +
                "\n date=" + date +
                "\n time=" + time
                ;
    }
}
