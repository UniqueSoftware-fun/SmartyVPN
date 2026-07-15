package com.smartyvpn.tfsmarty.model;

public class Server {
    private String country;
    private String flagUrl;
    private String ovpn;
    private String ovpnUserName;
    private String ovpnUserPassword;
    private String chkPaid;

    public Server() {
    }

    public Server(String country, String flagUrl, String ovpn) {
        this.country = country;
        this.flagUrl = flagUrl;
        this.ovpn = ovpn;
    }

    public Server(String country, String flagUrl, String ovpn, String ovpnUserName, String ovpnUserPassword, String chkPaid) {
        this.country = country;
        this.flagUrl = flagUrl;
        this.ovpn = ovpn;
        this.ovpnUserName = ovpnUserName;
        this.ovpnUserPassword = ovpnUserPassword;
        this.chkPaid = chkPaid;
    }

    public String getChkPaid() {
        return chkPaid;
    }

    public void setChkPaid(String chkPaid) {
        this.chkPaid = chkPaid;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getFlagUrl() {
        return flagUrl;
    }

    public void setFlagUrl(String flagUrl) {
        this.flagUrl = flagUrl;
    }

    public String getOvpn() {
        return ovpn;
    }

    public void setOvpn(String ovpn) {
        this.ovpn = ovpn;
    }

    public String getOvpnUserName() {
        return ovpnUserName;
    }

    public void setOvpnUserName(String ovpnUserName) {
        this.ovpnUserName = ovpnUserName;
    }

    public String getOvpnUserPassword() {
        return ovpnUserPassword;
    }

    public void setOvpnUserPassword(String ovpnUserPassword) {
        this.ovpnUserPassword = ovpnUserPassword;
    }
}
