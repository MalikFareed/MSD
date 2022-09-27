package com.malik.msd;

import java.util.Date;

public class Record {
    private String date;
    private String time;
    private int rate;
    private int mann;
    private int sair;

    public Record(String  _date, String _time, int _rate, int _mann, int _sair)
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
