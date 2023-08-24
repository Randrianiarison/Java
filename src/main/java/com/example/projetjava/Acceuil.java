package com.example.projetjava;

import com.example.projetjava.Prototype.Staffer;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Optional;

public class Acceuil extends Connexion {
    private ObservableList<Staffer> staffers = FXCollections.observableArrayList();
    @FXML
    private ComboBox<String> combobox;

    @FXML
    private TextField txtname;

    @FXML
    private TextField txtpassword;

    @FXML
    private TextField txtusername;

    @FXML
    private TableView<Staffer> tableview;

    @FXML
    private TableColumn<Staffer, String> colonenom;

    @FXML
    private TableColumn<Staffer, String> coloneprenom;

    @FXML
    private TableColumn<Staffer, String> coloneposte;

    @FXML
    private TableColumn<Staffer, LocalDate> colonedate;
    @FXML
    private TextField txtrecherche;
    @FXML
    public void initialize() {
        combo();
        configuration();
        rafraichier();
        tableview.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            listeEmployer(newValue);
        });
        txtrecherche.textProperty().addListener((observable, oldValue, newValue) -> chearche(newValue));
    }
    @FXML
    void Quitter(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation");
        alert.setHeaderText("Quitter l'application");
        alert.setContentText("Êtes-vous sûr de vouloir quitter ?");
        ButtonType buttonTypeOui = new ButtonType("Oui");
        ButtonType buttonTypeNon = new ButtonType("Non");
        alert.getButtonTypes().setAll(buttonTypeOui, buttonTypeNon);
        Optional<ButtonType> result = alert.showAndWait();
        if (((Optional<?>) result).isPresent() && result.get() == buttonTypeOui) {
            Platform.exit();
        } else {
            event.consume();
        }
    }

    public void combo() {
        ObservableList<String> postes = FXCollections.observableArrayList(
                "Cuisiné",
                "Vendeur",
                "caisseur"
        );
        combobox.setItems(postes);
    }
    public void configuration() {
        colonenom.setCellValueFactory(new PropertyValueFactory<>("name"));
        coloneprenom.setCellValueFactory(new PropertyValueFactory<>("username"));
        coloneposte.setCellValueFactory(new PropertyValueFactory<>("poste"));
        colonedate.setCellValueFactory(new PropertyValueFactory<>("dateEmb"));
    }

    public void rafraichier() {
        ObservableList<Staffer> staffers = affichageEmployer();
        tableview.setItems(staffers);
    }
    private void showInfoDialog(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
    private void showErrorDialog(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
    private void showWarningDialog(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    public void raffraichir() {
        try {
            String query = "SELECT * FROM employe";
            statement = connection.createStatement();
            resultSet = statement.executeQuery(query);
            staffers.clear();

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String nom = resultSet.getString("nom");
                String prenom = resultSet.getString("prenom");
                String poste = resultSet.getString("poste");

                Staffer admin = new Staffer(id,nom,prenom,poste);
                staffers.add(admin);
            }
            tableview.setItems(staffers);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                resultSet.close();
                statement.close();
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        txtrecherche.textProperty().addListener((observable, oldValue, newValue) -> chearche(newValue));
    }
    public ObservableList<Staffer> affichageEmployer() {
        ObservableList<Staffer> staffers = FXCollections.observableArrayList();

        try (PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM employe");
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("nom");
                String prenom = resultSet.getString("prenom");
                String poste = resultSet.getString("poste");
                String mdp = resultSet.getString("mot_de_passe");
                LocalDate date_embauche = resultSet.getDate("date_emb").toLocalDate();

                System.out.println("ID: " + id + ", Name: " + name + ", Prenom: " + prenom + ", Poste: " + poste + ", MDP: " + mdp + ", Date Embauche: " + date_embauche);

                staffers.add(new Staffer(id, name, prenom, poste, mdp, date_embauche));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return staffers;
    }
    public void AjouterEmployer() {
        try {
            String nom = txtname.getText();
            String prenom = txtusername.getText();
            String poste = combobox.getValue();
            String mdp = txtpassword.getText();
            LocalDate currentDate = LocalDate.now();

            String query = "INSERT INTO employe (nom, prenom, poste, mot_de_passe, date_emb) VALUES (?, ?, ?, ?, ?)";

            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, nom);
            preparedStatement.setString(2, prenom);
            preparedStatement.setString(3, poste);
            preparedStatement.setString(4, mdp);
            preparedStatement.setDate(5, java.sql.Date.valueOf(currentDate));

            int rowsAffected = preparedStatement.executeUpdate();
            preparedStatement.close();

            if (rowsAffected > 0) {
                showAlert(Alert.AlertType.INFORMATION, "Succès", "Employé enregistré avec succès.");
                effaceText();
                rafraichier();
                System.out.println("Données ajoutées avec succès.");
            } else {
                showAlert(Alert.AlertType.ERROR, "Erreur", "Impossible d'enregistrer l'employé.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erreur", "Une erreur s'est produite : " + e.getMessage());
        }
    }

    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private void effaceText() {
        txtname.clear();
        txtusername.clear();
        txtpassword.clear();
        combobox.getSelectionModel().clearSelection();
    }
    @FXML
    void btnAjout(ActionEvent event) {
        AjouterEmployer();
    }
    @FXML
    void btnSuppr(ActionEvent event) {
        Staffer selectedStaffer = tableview.getSelectionModel().getSelectedItem();
        if (selectedStaffer != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmation");
            alert.setHeaderText("Supprimer l'employé");
            alert.setContentText("Êtes-vous sûr de vouloir supprimer cet employé ?");
            ButtonType buttonTypeOui = new ButtonType("Oui");
            ButtonType buttonTypeNon = new ButtonType("Non");
            alert.getButtonTypes().setAll(buttonTypeOui, buttonTypeNon);
            Optional<ButtonType> result = alert.showAndWait();

            if (result.isPresent() && result.get() == buttonTypeOui) {
                if (SupprimerEmployer(selectedStaffer)) {
                    showAlert(Alert.AlertType.INFORMATION, "Succès", "Employé supprimé avec succès.");
                    rafraichier();
                } else {
                    showAlert(Alert.AlertType.ERROR, "Erreur", "Impossible de supprimer l'employé.");
                }
            }
        } else {
            showAlert(Alert.AlertType.WARNING, "Avertissement", "Sélectionnez un employé à supprimer.");
        }
    }
    private boolean SupprimerEmployer(Staffer staffer) {
        try {
            String query = "DELETE FROM employe WHERE id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, staffer.getId());

            int rowsAffected = preparedStatement.executeUpdate();
            preparedStatement.close();

            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    private void listeEmployer(Staffer staffer) {
        if (staffer != null) {
            txtname.setText(staffer.getName());
            txtusername.setText(staffer.getUsername());
            combobox.setValue(staffer.getPoste());
        } else {
            effaceText();
        }
    }
    @FXML
    void btnUpdate(ActionEvent event) {
        Staffer selectedStaffer = tableview.getSelectionModel().getSelectedItem();

        if (selectedStaffer != null) {
            TextInputDialog dialog = new TextInputDialog(selectedStaffer.getName());
            dialog.setTitle("Modification d'employé");
            dialog.setHeaderText("Modifier les informations de l'employé");
            dialog.setContentText("Nouveau nom:");

            Optional<String> result = dialog.showAndWait();

            result.ifPresent(newName -> {
                selectedStaffer.setName(newName);
                updateEmployee(selectedStaffer);
                rafraichier();
            });
        } else {
            showAlert(Alert.AlertType.WARNING, "Avertissement", "Sélectionnez un employé à modifier.");
        }
    }
    // fonction modification
    private void updateEmployee(Staffer staffer) {
        try {
            String query = "UPDATE employe SET nom = ? WHERE id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, staffer.getName());
            preparedStatement.setInt(2, staffer.getId());

            int rowsAffected = preparedStatement.executeUpdate();
            preparedStatement.close();

            if (rowsAffected > 0) {
                showAlert(Alert.AlertType.INFORMATION, "Succès", "Employé mis à jour.");
            } else {
                showAlert(Alert.AlertType.ERROR, "Erreur", "Impossible.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erreur", "Une erreur s'est produite : " + e.getMessage());
        }
    }
    @FXML
    void Deconnection(ActionEvent event) {
        Stage currentStage = (Stage) combobox.getScene().getWindow();
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
            Parent root = fxmlLoader.load();
            Stage newStage = new Stage();
            newStage.setScene(new Scene(root));
            currentStage.close();
            newStage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    //fonction recherceh anaty tableview
    private void chearche(String keyword) {
        ObservableList<Staffer> filtre = FXCollections.observableArrayList();

        if (keyword.isEmpty()) {
            tableview.setItems(staffers);
            return;
        }
        String lowerCaseKeyword = keyword.toLowerCase();
        for (Staffer staffer : staffers) {
            if (staffer.getName().toLowerCase().contains(lowerCaseKeyword) ||
                    staffer.getUsername().toLowerCase().contains(lowerCaseKeyword) ||
                    staffer.getPoste().toLowerCase().contains(lowerCaseKeyword)) {
                filtre.add(staffer);
            }
        }
        tableview.setItems(filtre);
        raffraichir();
    }
    //bouton cliquena mi-ajouter
    @FXML
    void pointage(ActionEvent event) {
        Staffer selectedStaffer = tableview.getSelectionModel().getSelectedItem();
        if (selectedStaffer != null) {
            int id_emp = selectedStaffer.getId();
            LocalDate date = LocalDate.now();
            int pointage = 1;

            if (pointageSalaire(id_emp, date, pointage)) {
                showErrorDialog("Erreur", "Impossible d'enregistrer la présence.");
            } else {
                showInfoDialog("Succès", "Présence le "+date);
            }
        } else {
            showWarningDialog("Avertissement", "Sélectionnez un employé pour enregistrer la présence.");
        }
    }
    //fonction mi-ajouter ao @table presence presence
    public boolean pointageSalaire(int id_emp, LocalDate date, int pointage) {
        try {
            if (presenceExists(id_emp, date)) {
                showErrorDialog("Erreur", "La présence pour cette date a déjà été enregistrée.");
                return false;
            }

            String query = "INSERT INTO presence (id_emp, date, pointage) VALUES (?, ?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, id_emp);
            preparedStatement.setDate(2, java.sql.Date.valueOf(date));
            preparedStatement.setInt(3, pointage);

            int rowsAffected = preparedStatement.executeUpdate();
            preparedStatement.close();

            if (rowsAffected > 0) {
                showInfoDialog("Succès", "Présence enregistrée avec succès.");
                return true;
            } else {
                showErrorDialog("Erreur", "Impossible d'enregistrer la présence.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Une erreur s'est produite : " + e.getMessage());
        }
        return false;
    }

    //fonction mverifier date, 1 isanandro ihan no presence satria isanandro ny karama
    private boolean presenceExists(int id_emp, LocalDate date) throws SQLException {
        String query = "SELECT COUNT(*) FROM presence WHERE id_emp = ? AND date = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setInt(1, id_emp);
        preparedStatement.setDate(2, java.sql.Date.valueOf(date));

        ResultSet resultSet = preparedStatement.executeQuery();
        resultSet.next();
        int count = resultSet.getInt(1);

        resultSet.close();
        preparedStatement.close();

        return count > 0;
    }
    //bouton makany @paiement
    @FXML
        private void Paiement(ActionEvent event) {
            Node source = (Node) event.getSource();
            Stage currentStage = (Stage) source.getScene().getWindow();

            try {
                FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("Pointage.fxml"));
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
