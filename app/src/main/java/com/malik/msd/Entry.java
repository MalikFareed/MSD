package com.malik.msd;

import java.util.Date;

public class Entry {
    private String date;
    private String time;
    private double rate;
    private double mann;
    private double sair;

    public Entry(String  _date, String _time, double _rate, double _mann, double _sair)
    {
        this.date = _date;
        this.time = _time;
        this.rate = _rate;
        this.mann = _mann;
        this.sair = _sair;

    }

    public String getDate()
    {
        return this.date;
    }

    public String getTime()
    {
        return this.time;
    }
}
