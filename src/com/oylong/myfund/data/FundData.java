package com.oylong.myfund.data;

import java.util.Date;

/**
 * 基金数据实体类
 *
 * @author OyLong
 * @date 2021/02/25 12:16
 **/
public class FundData {
    private String fundcode;
    private String name;

    private Date testRq;

    private Date jzrq;
    private double dwjz;

    private double gsz;
    private double gszzl;
    private Date gztime;

    public String getFundcode() {
        return fundcode;
    }

    public void setFundcode(String fundcode) {
        this.fundcode = fundcode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getJzrq() {
        return jzrq;
    }

    public void setJzrq(Date jzrq) {
        this.jzrq = jzrq;
    }

    public double getDwjz() {
        return dwjz;
    }

    public void setDwjz(double dwjz) {
        this.dwjz = dwjz;
    }

    public double getGsz() {
        return gsz;
    }

    public void setGsz(double gsz) {
        this.gsz = gsz;
    }

    public double getGszzl() {
        return gszzl;
    }

    public void setGszzl(double gszzl) {
        this.gszzl = gszzl;
    }

    public Date getGztime() {
        if(name == null) {
            name = null;
        }
        return gztime;
    }

    public void setGztime(Date gztime) {
        this.gztime = gztime;
    }

    public Date getTestRq() {
        return testRq;
    }

    public void setTestRq(Date testRq) {
        this.testRq = testRq;
    }

    @Override
    public String toString() {
        return "FundData{" +
                "fundCode=" + fundcode +
                ", name='" + name + '\'' +
                ", jzrq=" + jzrq +
                ", dwjz=" + dwjz +
                ", gsz=" + gsz +
                ", gzzl=" + gszzl +
                ", gzTime=" + gztime +
                '}';
    }
}
