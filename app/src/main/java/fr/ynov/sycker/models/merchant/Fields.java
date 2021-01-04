package fr.ynov.sycker.models.merchant;

import java.io.Serializable;

public class Fields implements Serializable {
    private String code_postal;
    private String description;
    private String adresse;
    private String telephone;
    private double[] geo_point_2d;
    private String services;
    private String mail;
    private String type_de_commerce;
    private String site_internet;

    public String getCode_postal() {
        return code_postal;
    }

    public void setCode_postal(String code_postal) {
        this.code_postal = code_postal;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public double[] getGeo_point_2d() {
        return geo_point_2d;
    }

    public void setGeo_point_2d(double[] geo_point_2d) {
        this.geo_point_2d = geo_point_2d;
    }

    public String getServices() {
        return services;
    }

    public void setServices(String services) {
        this.services = services;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getType_de_commerce() {
        return type_de_commerce;
    }

    public void setType_de_commerce(String type_de_commerce) {
        this.type_de_commerce = type_de_commerce;
    }

    public String getSite_internet() {
        return site_internet;
    }

    public void setSite_internet(String site_internet) {
        this.site_internet = site_internet;
    }
}
