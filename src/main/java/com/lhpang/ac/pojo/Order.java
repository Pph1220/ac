package com.lhpang.ac.pojo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class Order implements Serializable {
    private Integer id;

    private Long orderNo;

    private Integer userId;

    private Integer shipping;

    private BigDecimal payment;

    private Integer paymengType;

    private Integer status;

    private Date paymentTime;

    private Date dendTime;

    private Date endTime;

    private Date createTime;

    private Date updateTime;

    private static final long serialVersionUID = 1L;

    public Order(Integer id, Long orderNo, Integer userId, Integer shipping, BigDecimal payment, Integer paymengType, Integer status, Date paymentTime, Date dendTime, Date endTime, Date createTime, Date updateTime) {
        this.id = id;
        this.orderNo = orderNo;
        this.userId = userId;
        this.shipping = shipping;
        this.payment = payment;
        this.paymengType = paymengType;
        this.status = status;
        this.paymentTime = paymentTime;
        this.dendTime = dendTime;
        this.endTime = endTime;
        this.createTime = createTime;
        this.updateTime = updateTime;
    }

    public Order() {
        super();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Long getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(Long orderNo) {
        this.orderNo = orderNo;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getShipping() {
        return shipping;
    }

    public void setShipping(Integer shipping) {
        this.shipping = shipping;
    }

    public BigDecimal getPayment() {
        return payment;
    }

    public void setPayment(BigDecimal payment) {
        this.payment = payment;
    }

    public Integer getPaymengType() {
        return paymengType;
    }

    public void setPaymengType(Integer paymengType) {
        this.paymengType = paymengType;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Date getPaymentTime() {
        return paymentTime;
    }

    public void setPaymentTime(Date paymentTime) {
        this.paymentTime = paymentTime;
    }

    public Date getDendTime() {
        return dendTime;
    }

    public void setDendTime(Date dendTime) {
        this.dendTime = dendTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}