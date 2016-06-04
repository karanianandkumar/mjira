package com.anandkumar.mjira;

/**
 * Created by Anand on 6/4/2016.
 */
public class DeviceRequest {
    private String fromUser;
    private String toUser;
    private String imei;
    private boolean isAccepted;

    public DeviceRequest() {
       this.fromUser="";
        this.toUser="";
        this.imei="";
        isAccepted=false;
    }

    public String getFromUser() {
        return fromUser;
    }

    public void setFromUser(String fromUser) {
        this.fromUser = fromUser;
    }

    public String getToUser() {
        return toUser;
    }

    public void setToUser(String toUser) {
        this.toUser = toUser;
    }

    public String getImei() {
        return imei;
    }

    public void setImei(String imei) {
        this.imei = imei;
    }

    public boolean isAccepted() {
        return isAccepted;
    }

    public void setAccepted(boolean accepted) {
        isAccepted = accepted;
    }
}
