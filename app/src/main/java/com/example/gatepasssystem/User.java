package com.example.gatepasssystem;

public class User {

    public String fullname, rollno, department, email;

    public User(){

    }

    public User(String fullname,String rollno, String department, String email){
        this.fullname = fullname;
        this.rollno = rollno;
        this.department = department;
        this.email = email;
    }
}
