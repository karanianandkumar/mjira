package com.anandkumar.mjira;

/**
 * Created by Anand on 6/3/2016.
 */
public class Device {

    private String name;
    private String imei;
    private String owner;

    public Device() {
        this.name = "";
        this.imei = "";
        this.owner = "";
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImei() {
        return imei;
    }

    public void setImei(String imei) {
        this.imei = imei;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }
}
