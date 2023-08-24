package com.example.projetjava;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class HelloController {

    private Connexion connexion;

    @FXML
    private TextField txtemail;
    @FXML
    private PasswordField txtpassw;

    @FXML
    void BtnCrud(ActionEvent event) {
        String email = txtemail.getText();
        String password = txtpassw.getText();

        if (isValidLogin(email, password)) {
            openCrudScene(event);
        } else {
            showAlert("Invalid Credentials", "Please enter valid email and password.");
        }
    }

    @FXML
    void initialize() {
        connexion = new Connexion();
    }

    private void openCrudScene(ActionEvent event) {
        Node source = (Node) event.getSource();
        Stage currentStage = (Stage) source.getScene().getWindow();

        try {
            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("Employeur.fxml"));
            Parent root = fxmlLoader.load();
            Stage newStage = new Stage();
            newStage.setScene(new Scene(root));

            currentStage.close();

            newStage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private boolean isValidLogin(String email, String password) {
        try {
            Connection connection = connexion.dbConnexion();

            if (connection == null) {
                System.out.println("Connection is null");
                return false;
            }

            try (PreparedStatement statement = connection.prepareStatement("SELECT * FROM admin WHERE email = ? AND password = ?")) {
                statement.setString(1, email);
                statement.setString(2, password);

                try (ResultSet resultSet = statement.executeQuery()) {
                    return resultSet.next();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    void onClose() {
        connexion.dbDeconnexion();
    }
}
