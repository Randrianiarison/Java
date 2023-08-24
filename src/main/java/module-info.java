module com.example.projetjava {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires mysql.connector.j;
    opens com.example.projetjava.Prototype to javafx.base;
    opens com.example.projetjava to javafx.fxml;
    exports com.example.projetjava;
}