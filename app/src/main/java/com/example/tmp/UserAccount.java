package com.example.tmp;

public class UserAccount {
    private String name;
    private String stdNum;
    private String password;
    private String department;

    public UserAccount() { }

    public String getName() { return name; }
    public String getStdNum() { return stdNum; }
    public String getPassword() { return password; }
    public String getDepartment() { return department; }

    public void setName(String name) { this.name = name; }
    public void setStdNum(String stdNum) { this.stdNum = stdNum; }
    public void setPassword(String password) { this.password = password; }
    public void setDepartment(String department) { this.department = department; }
}
