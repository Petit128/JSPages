package com.parking.model;

import java.io.Serializable;
import java.util.Date;

public class Tariff implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private int id;
    private String tariffType;
    private double hourlyRate;
    private Double dailyRate;
    private Double weeklyRate;
    private Double monthlyRate;
    private double vipDiscount;
    private double subscriberDiscount;
    private Date lastUpdated;
    
    public Tariff() {}
    
    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    
    public String getTariffType() { return tariffType; }
    public void setTariffType(String tariffType) { this.tariffType = tariffType; }
    
    public double getHourlyRate() { return hourlyRate; }
    public void setHourlyRate(double hourlyRate) { this.hourlyRate = hourlyRate; }
    
    public Double getDailyRate() { return dailyRate; }
    public void setDailyRate(Double dailyRate) { this.dailyRate = dailyRate; }
    
    public Double getWeeklyRate() { return weeklyRate; }
    public void setWeeklyRate(Double weeklyRate) { this.weeklyRate = weeklyRate; }
    
    public Double getMonthlyRate() { return monthlyRate; }
    public void setMonthlyRate(Double monthlyRate) { this.monthlyRate = monthlyRate; }
    
    public double getVipDiscount() { return vipDiscount; }
    public void setVipDiscount(double vipDiscount) { this.vipDiscount = vipDiscount; }
    
    public double getSubscriberDiscount() { return subscriberDiscount; }
    public void setSubscriberDiscount(double subscriberDiscount) { this.subscriberDiscount = subscriberDiscount; }
    
    public Date getLastUpdated() { return lastUpdated; }
    public void setLastUpdated(Date lastUpdated) { this.lastUpdated = lastUpdated; }
}