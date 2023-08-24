package com.example.projetjava.Prototype;

public class Employeur {
    private int id;
    private String nomEmploye;
    private int salary;
    public Employeur(int id, int salary, String nomEmploye) {
        this.id = id;
        this.salary = salary;
        this.nomEmploye = nomEmploye;
    }
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    public int getSalary() {
        return salary;
    }
    public void setSalary(int salary) {
        this.salary = salary;
    }
    public String getNomEmploye() {
        return nomEmploye;
    }

    public void setNomEmploye(String nomEmploye) {
        this.nomEmploye = nomEmploye;
    }
}
