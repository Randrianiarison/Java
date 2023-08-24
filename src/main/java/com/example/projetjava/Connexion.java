package com.example.projetjava;

import java.sql.*;

public class Connexion {
    protected Connection connection;
    protected ResultSet resultSet;
    protected Statement statement;

    public Connexion(){
        getConnection();
    }
    public Connection getConnection() {
        //Load the driver class
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException ex) {
            System.out.println("Unable to load the class. Terminating the program");
            System.exit(-1);
        }
        try {
            this.connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/restaurants", "root", "");
            System.out.println("Connexion succefully");
        } catch (SQLException ex) {
            System.out.println("Error getting connection: " + ex.getMessage());
            System.exit(-1);
        } catch (Exception ex) {
            System.out.println("Error: " + ex.getMessage());
            System.exit(-1);
        }
        return null;
    }

    public Connection dbConnexion() {
        return this.connection;
    }

    public void dbDeconnexion() {
        try {
            this.connection.close();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }


}
