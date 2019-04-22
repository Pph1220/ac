package com.lhpang.ac.pojo;

import java.io.Serializable;
import java.util.Date;

public class User implements Serializable {

    private static final long serialVersionUID = 2825840496371827625L;

    private Integer id;

    private String name;

    private String logno;

    private String password;

    private String phone;

    private String question;

    private String answer;

    private Integer role;

    private Date creatTime;

    private Date updateTime;

    public User(Integer id, String name, String logno, String password, String phone, String question, String answer, Integer role, Date creatTime, Date updateTime) {
        this.id = id;
        this.name = name;
        this.logno = logno;
        this.password = password;
        this.phone = phone;
        this.question = question;
        this.answer = answer;
        this.role = role;
        this.creatTime = creatTime;
        this.updateTime = updateTime;
    }

    public User() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLogno() {
        return logno;
    }

    public void setLogno(String logno) {
        this.logno = logno;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public Integer getRole() {
        return role;
    }

    public void setRole(Integer role) {
        this.role = role;
    }

    public Date getCreatTime() {
        return creatTime;
    }

    public void setCreatTime(Date creatTime) {
        this.creatTime = creatTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }
}