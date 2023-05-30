package com.example.listelemeapp;

public class VeriClass {
    private  String dataBaslik;
    private  String dataTanim;
    private  String dataResim;
    private  String dataTarih;

    private String key;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public VeriClass(String dataBaslik, String dataTanim, String dataResim, String dataTarih) {
        this.dataBaslik = dataBaslik;
        this.dataTanim = dataTanim;
        this.dataResim = dataResim;
        this.dataTarih = dataTarih;
    }

    public String getDataBaslik() {
        return dataBaslik;
    }

    public String getDataTanim() {
        return dataTanim;
    }

    public String getDataResim() {
        return dataResim;
    }

    public String getDataTarih() {
        return dataTarih;
    }

    public VeriClass(){

    }

}
