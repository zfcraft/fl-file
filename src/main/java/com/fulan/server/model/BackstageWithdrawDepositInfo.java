package com.fulan.server.model;


import com.fulan.server.common.util.ExcelVOAttribute;

public class BackstageWithdrawDepositInfo {

    @ExcelVOAttribute(name = "用户昵称", column = "A")
    private String name;

    @ExcelVOAttribute(name = "用户头像", column = "B")
    private String password;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}