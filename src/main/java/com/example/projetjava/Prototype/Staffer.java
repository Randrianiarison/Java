package com.example.projetjava.Prototype;

import java.time.LocalDate;

public class Staffer {
    private int id;
    private String name;
    private String username;
    private String poste;
    private String password;
    private LocalDate dateEmb;

    public Staffer(int id, String name, String username, String poste, String password, LocalDate dateEmb) {
        this.id = id;
        this.name = name;
        this.username = username;
        this.poste = poste;
        this.password = password;
        this.dateEmb = dateEmb;
    }

    public Staffer() {

    }

    public Staffer(int id, String nom, String prenom, String poste) {

    }

    public Staffer(String idEmp, String pointage, String salaire) {
    }

    public Staffer(int id, int idEmp, int pointage, LocalDate dateEmbauche) {
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getUsername() {
        return username;
    }

    public String getPoste() {
        return poste;
    }

    public String getPassword() {
        return password;
    }

    public LocalDate getDateEmb() {
        return dateEmb;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPoste(String poste) {
        this.poste = poste;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setDateEmb(LocalDate dateEmb) {
        this.dateEmb = dateEmb;
    }
}
