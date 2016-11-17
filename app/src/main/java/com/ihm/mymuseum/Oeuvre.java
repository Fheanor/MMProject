package com.ihm.mymuseum;

/**
 * Created by Julian on 15/11/2016.
 */

public class Oeuvre {

    private String nom;
    private String description;
    private String dateCreation;
    private String artiste;

    public Oeuvre(){
        nom = "";
        description = "";
        dateCreation = "";
        artiste = "";
    }

    public Oeuvre(String nom, String description, String creation, String artiste){
        this.nom = nom;
        this.description = description;
        this.dateCreation = creation;
        this.artiste = artiste;
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

    public String toString(){
        return "Nom de l'oeuvre: \n" + this.nom + "\n\n"
                + "Artiste: \n" + this.artiste + "\n\n"
                + "Date de création: \n" + this.dateCreation + "\n\n"
                + "Description: \n" + this.description + "\n\n";
    }


}
