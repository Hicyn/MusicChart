package com.example.musicchart;

import java.util.Collections;
import java.util.List;

public class MusicItem {
    private int id;
    private String curName ;
    private String curRate;
    private String curUrl;

    public MusicItem() {
        this.curName = "";
        this.curRate = "";
    }


    public MusicItem(String curName, String curRate) {
        this.curName = curName;
        this.curRate = curRate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCurName() {
        return curName;
    }

    public void setCurName(String curName) {
        this.curName = curName;
    }

    public String getCurRate() {
        return curRate;
    }

    public void setCurRate(String curRate) {
        this.curRate = curRate;
    }

}
