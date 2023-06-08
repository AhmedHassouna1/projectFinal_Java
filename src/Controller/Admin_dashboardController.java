/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller; 

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.DB;

/**
 * FXML Controller class
 *
 * @author Home Tech
 */
public class Admin_dashboardController implements Initializable {

    @FXML
    private TableColumn<?, ?> firstname;
    @FXML
    private TableColumn<?, ?> lastname;
    @FXML
    private TableColumn<?, ?> age;
    @FXML
    private TableColumn<?, ?> phone;
    @FXML
    private TableColumn<?, ?> gender;
    @FXML
    private TextField searchDT;
    @FXML
    private TableView<Patient> table_patient;
    @FXML
    private TableColumn<?, ?> id_id;
    @FXML
    private TableColumn<?, ?> username;
    @FXML
    private TableColumn<?, ?> email;
    @FXML
    private TableColumn<?, ?> role;
    @FXML
    private TableColumn<?, ?> password;

   
    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }

    private void initializeTable() {
        id_id.setCellValueFactory(new PropertyValueFactory<>("id")); 
        username.setCellValueFactory(new PropertyValueFactory<>("username")); 
        email.setCellValueFactory(new PropertyValueFactory<>("email")); 
        role.setCellValueFactory(new PropertyValueFactory<>("role")); 
        password.setCellValueFactory(new PropertyValueFactory<>("password")); 
        firstname.setCellValueFactory(new PropertyValueFactory<>("firstname"));
        lastname.setCellValueFactory(new PropertyValueFactory<>("lastname"));
        age.setCellValueFactory(new PropertyValueFactory<>("age"));
        phone.setCellValueFactory(new PropertyValueFactory<>("phone"));
        gender.setCellValueFactory(new PropertyValueFactory<>("gender"));
    }

    @FXML
    private void btn_ShwAllPatiant(ActionEvent event) {
        try {
            Connection connection = DB.getInstance().getConnection();
            PreparedStatement statement = connection.prepareStatement("SELECT id, username, email, role, password, firstname, lastname, age, phone, gender FROM users");

            ResultSet resultSet = statement.executeQuery();

            ObservableList<Patient> patients = FXCollections.observableArrayList();
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String username = resultSet.getString("username");
                String email = resultSet.getString("email");
                String role = resultSet.getString("role");
                String password = resultSet.getString("password");
                String firstname = resultSet.getString("firstname");
                String lastname = resultSet.getString("lastname");
                int age = resultSet.getInt("age");
                String phone = resultSet.getString("phone");
                String gender = resultSet.getString("gender");

                Patient patient = new Patient(id, username, email, role, password, firstname, lastname, age, phone, gender);
                patients.add(patient);
            }

            initializeTable();

            table_patient.setItems(patients);

            statement.close();
            connection.close();
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void btn_Search(ActionEvent event) {
        String searchText = searchDT.getText(); 

        ObservableList<Patient> searchResults = FXCollections.observableArrayList();

        for (Patient patient : table_patient.getItems()) {
            if (patient.getFirstname().equalsIgnoreCase(searchText)) {
                searchResults.add(patient);
            }
        }

        if (searchResults.isEmpty()) {
          
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Search Results");
            alert.setHeaderText("No Results Found");
            alert.setContentText("No patients found with the given firstname: " + searchText);
            alert.showAndWait();
        } else {
             table_patient.setItems(searchResults);
        }
    }

    @FXML
    private void btn_newPatient(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/regesster.fxml"));
        Parent registerPage = loader.load();
        RegessterController registerController = loader.getController();

        Scene scene = new Scene(registerPage);
        Stage currentWindow = (Stage) ((Node) event.getSource()).getScene().getWindow();
        currentWindow.setScene(scene);
        currentWindow.show();
    }

    @FXML
    private void btn_UpdatePatiant(ActionEvent event) throws IOException {
         Patient selectedPatient = table_patient.getSelectionModel().getSelectedItem();
        if (selectedPatient != null) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/regesster.fxml"));
            Parent registerPage = loader.load();
            RegessterController registerController = loader.getController();
            registerController.setPatientData(selectedPatient);
            Scene scene = new Scene(registerPage);
            Stage currentWindow = (Stage) ((Node) event.getSource()).getScene().getWindow();
            currentWindow.setScene(scene);
            currentWindow.show();
        }
    }

    @FXML
    private void btn_DeletPatiant(ActionEvent event) {
        Patient selectedPatient = table_patient.getSelectionModel().getSelectedItem(); // Get the selected patient from the table view

        if (selectedPatient != null) {
            Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
            confirmationAlert.setTitle("Confirmation");
            confirmationAlert.setHeaderText("Delete Patient");
            confirmationAlert.setContentText("Are you sure you want to delete the selected patient?");

            Optional<ButtonType> result = confirmationAlert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                try {
                    Connection connection = DB.getInstance().getConnection();
                    PreparedStatement statement = connection.prepareStatement("DELETE FROM users WHERE firstname = ?");

                    statement.setString(1, selectedPatient.getFirstname()); // Delete based on the firstname

                    int rowsDeleted = statement.executeUpdate();

                    if (rowsDeleted > 0) {
                        // Remove the selected patient from the table view
                        table_patient.getItems().remove(selectedPatient);

                        Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
                        successAlert.setTitle("Success");
                        successAlert.setHeaderText("Patient Deleted");
                        successAlert.setContentText("The selected patient has been successfully deleted.");
                        successAlert.showAndWait();
                    }

                    statement.close();
                    connection.close();
                } catch (SQLException | ClassNotFoundException e) {
                    e.printStackTrace();
                 }
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("No Patient Selected");
            alert.setHeaderText("No Patient Selected");
            alert.setContentText("Please select a patient from the table.");
            alert.showAndWait();
        }
    }

    @FXML
    private void btn_logout(ActionEvent event) {
        Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmationAlert.setTitle("Logout Confirmation");
        confirmationAlert.setHeaderText("Logout");
        confirmationAlert.setContentText("Are you sure you want to log out?");

        Optional<ButtonType> result = confirmationAlert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/login_user.fxml"));
                Parent loginPage = loader.load();

                Scene scene = new Scene(loginPage);
                Stage currentWindow = (Stage) ((Node) event.getSource()).getScene().getWindow();
                currentWindow.setScene(scene);
                currentWindow.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    private void btn_AppoSch(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/appointment.fxml"));
            Parent appointmentPage = loader.load();

             AppointmentController appointmentController = loader.getController();

            Scene scene = new Scene(appointmentPage);
            Stage currentWindow = (Stage) ((Node) event.getSource()).getScene().getWindow();
            currentWindow.setScene(scene);
            currentWindow.show();
        } catch (IOException e) {
            e.printStackTrace();
         }
    }

    @FXML
    private void btn_adminPage(ActionEvent event) {
    }

    @FXML
    private void btn_booked(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/bookedpage.fxml"));
            Parent appointmentPage = loader.load();

             BookedpageController appointmentController = loader.getController();

            Scene scene = new Scene(appointmentPage);
            Stage currentWindow = (Stage) ((Node) event.getSource()).getScene().getWindow();
            currentWindow.setScene(scene);
            currentWindow.show();
        } catch (IOException e) {
            e.printStackTrace();
         }
    }

}
