package com.ihm.mymuseum;

import java.io.Serializable;

/**
 * Created by Julian on 15/11/2016.
 */

public class Oeuvre implements Serializable {

    private String nom;
    private String description;
    private String dateCreation;
    private String artiste;
    private String infoArtiste;
    private String audiodescription;

    public Oeuvre(){
        nom = "";
        description = "";
        dateCreation = "";
        artiste = "";
        infoArtiste = "";
        audiodescription = "";
    }

    public Oeuvre(String nom, String description, String creation, String artiste, String infoArtiste, String audiodescription){
        this.nom = nom;
        this.description = description;
        this.dateCreation = creation;
        this.artiste = artiste;
        this.infoArtiste = infoArtiste;
        this.audiodescription = audiodescription;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getDateCreation() {
        return dateCreation;
    }

    public void setDateCreation(String dateCreation) {
        this.dateCreation = dateCreation;
    }

    public String getArtiste() {
        return artiste;
    }

    public void setArtiste(String artiste) {
        this.artiste = artiste;
    }

    public String getInfoArtiste() { return infoArtiste; }

    public void setInfoArtiste(String info) {
        this.infoArtiste = info;
    }

    public String getAudiodescription() { return audiodescription; }

    public void setAudiodescription(String audio) {
        this.audiodescription = audio;
    }

    public String toString(){
        return "Nom de l'oeuvre: \n" + this.nom + "\n\n"
                + "Artiste: \n" + this.artiste + "\n\n"
                + "Date de création: \n" + this.dateCreation + "\n\n";
    }


}
