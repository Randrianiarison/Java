package com.example.projetjava.Prototype;

public class EmployerModel {
    public int id;
    public String nom;
    public String poste;
    public String date_embauche;
    public String prenom;
    public String mots_de_passe;

    public EmployerModel(int id, String nom,String prenom , String poste,String mots_de_passe , String date_embauche) {
        this.id = id;
        this.nom = nom;
        this.prenom = prenom;
        this.poste = poste;
        this.date_embauche = date_embauche;
        this.mots_de_passe = mots_de_passe;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPoste() {
        return poste;
    }

    public void setPoste(String poste) {
        this.poste = poste;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getMots_de_passe() {
        return mots_de_passe;
    }

    public void setMots_de_passe(String mots_de_passe) {
        this.mots_de_passe = mots_de_passe;
    }

    public String getDate_embauche() {
        return date_embauche;
    }

    public void setDate_embauche(String date_embauche) {
        this.date_embauche = date_embauche;
    }

    @Override
    public String toString() {
        return "EmployerModel{" +
                "id=" + id +
                ", nom='" + nom + '\'' +
                ",Prenom='"+ prenom + '\''+
                ", poste='" + poste + '\'' +
                ", date_embauche='" + date_embauche + '\'' +
                '}';
    }
}
