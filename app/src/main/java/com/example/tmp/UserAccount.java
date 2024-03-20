package com.example.tmp;

public class UserAccount {
    private String name;
    private String stdnum;
    private String password;
    private String department;

    public UserAccount() { }

    public String getName() { return name; }

    public String getDepartment() {return department; }

    public String getStdNum() { return stdnum; }

    public String getPassword() { return password; }


    public void setDepartment(String department) {this.department = department; }

    public void setName(String name) { this.name = name; }
    public void setStdNum(String stdnum) {
        this.stdnum = stdnum;
    }    public void setPassword(String password) { this.password = password; }
}