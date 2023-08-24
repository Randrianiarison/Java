package com.example.projetjava;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.example.projetjava.Prototype.Employeur;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

public class Pointage extends Connexion {
    @FXML
    private TableColumn<Employeur, Integer> coloneid;

    @FXML
    private TableColumn<Employeur, String> colonenom;

    @FXML
    private TableColumn<Employeur, Integer> colonesalaire;

    @FXML
    private TableView<Employeur> tableview;

    @FXML
    public void initialize() {
        configuration();
        rafraichir();
        tableview.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
        });
    }

    public void configuration() {
        coloneid.setCellValueFactory(new PropertyValueFactory<>("id"));
        colonenom.setCellValueFactory(new PropertyValueFactory<>("nomEmploye"));
        colonesalaire.setCellValueFactory(new PropertyValueFactory<>("salary"));
    }


    public void rafraichir() {
        ObservableList<Employeur> employes = getEmployes();
        tableview.setItems(employes);
    }

    //fonction affichege ireo information an table presence
    // le id_emp @table presence dia foreign key @table employe
    //10000 par jour ny salaire, rehef manao presece izy dia miajouter ao anaty table presence
    //in 1 isanandro ihna no afaka manao pointage n employer 1. efa misy an'izay any @classe Acceuil
    public ObservableList<Employeur> getEmployes() {
        ObservableList<Employeur> employes = FXCollections.observableArrayList();

        String query = "SELECT p.id_emp, SUM(p.pointage) * 10000 as total_pointage, e.nom, e.prenom " +
                "FROM presence p INNER JOIN employe e ON p.id_emp = e.id " +
                "GROUP BY p.id_emp, e.nom, e.prenom";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                int id_emp = resultSet.getInt("id_emp");
                int totalPointage = resultSet.getInt("total_pointage");
                String nomEmploye = resultSet.getString("nom");

                System.out.println("Employé ID: " + id_emp + ", Nom: " + nomEmploye + ", Total Pointage: " + totalPointage);

                employes.add(new Employeur(id_emp, totalPointage, nomEmploye));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return employes;
    }
    //rehefa mciqué an'ito bouton ito de voafafa le anaran le id selectionena, fandraisana karama
    @FXML
    void btnpayer(ActionEvent event) {
        Employeur selectedEmployeur = tableview.getSelectionModel().getSelectedItem();
        if (selectedEmployeur != null) {
            int selectedEmpId = selectedEmployeur.getId();

            String deleteQuery = "DELETE FROM presence WHERE id_emp = ?";

            try (PreparedStatement deleteStatement = connection.prepareStatement(deleteQuery)) {
                deleteStatement.setInt(1, selectedEmpId);
                deleteStatement.executeUpdate();
                rafraichir();
                showPaymentSuccessAlert(selectedEmpId);

                System.out.println("gagné de salaire l'ID: " + selectedEmpId);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("Aucun employé sélectionné.");
        }
    }

    private void showPaymentSuccessAlert(int empId) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Payment Successful");
        alert.setHeaderText(null);
        alert.setContentText("Le paiement a été traité avec succès pour l'ID de l'employé: " + empId);
        alert.showAndWait();
    }
    @FXML
    void btnCancel(ActionEvent event) {
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
}
