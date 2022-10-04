package model;

import com.google.firebase.Timestamp;

public class Entry {
    private String date;
    private String time;
    private double rate;
    private double mann;
    private double sair;
    private double amount;

    private String userId;
    private Timestamp timeAdded;
    private String username;

    public Entry() {
    }

    public Entry(String date, String time, double rate, double mann, double sair, double amount , String userId, Timestamp timeAdded, String username) {
        this.date = date;
        this.time = time;
        this.rate = rate;
        this.mann = mann;
        this.sair = sair;
        this.userId = userId;
        this.timeAdded = timeAdded;
        this.username = username;
        this.amount = amount;
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

    public double getRate() {
        return rate;
    }

    public void setRate(double rate) {
        this.rate = rate;
    }

    public double getMann() {
        return mann;
    }

    public void setMann(double mann) {
        this.mann = mann;
    }

    public double getSair() {
        return sair;
    }

    public void setSair(double sair) {
        this.sair = sair;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Timestamp getTimeAdded() {
        return timeAdded;
    }

    public void setTimeAdded(Timestamp timeAdded) {
        this.timeAdded = timeAdded;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }


}
