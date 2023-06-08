/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import static java.awt.SystemColor.text;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.PasswordField;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.stage.Stage;
import model.DB;

/**
 * FXML Controller class
 *
 * @author Home Tech
 */
public class RegessterController implements Initializable {

    @FXML
    private TextField txtUserName;
    @FXML
    private TextField txtFirstName;
    @FXML
    private TextField txtLastName;
    @FXML
    private TextField txtAge;
    @FXML
    private TextField txtEmail;
    @FXML
    private TextField txtPhone;
    @FXML
    private RadioButton radioMale;
    @FXML
    private RadioButton radioFemale;
    @FXML
    private RadioButton radioUser;
    @FXML
    private RadioButton radioAdmin;
    private PasswordField txtPassword;

    private Patient patient;
    @FXML
    private TextField  ID__ID;
    @FXML
    private PasswordField PasswordID;
    @FXML
    private ToggleGroup genderGroub;
    @FXML
    private ToggleGroup rolGroup;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    @FXML
    private void btn_backe(ActionEvent event) throws IOException {
        Parent previousPage = FXMLLoader.load(getClass().getResource("/View/login_user.fxml"));
        Scene scene = new Scene(previousPage);
        Stage currentWindow = (Stage) ((Node) event.getSource()).getScene().getWindow();
        currentWindow.setScene(scene);
        currentWindow.show();
    }

    @FXML
    private void btn_Save(ActionEvent event) {
        String username = txtUserName.getText();
        String password = PasswordID.getText();
        String firstname = txtFirstName.getText();
        String lastname = txtLastName.getText();
        int age = Integer.parseInt(txtAge.getText());
        String email = txtEmail.getText();
        String phone = txtPhone.getText();
        String gender = radioMale.isSelected() ? "male" : "female";
        String role = radioUser.isSelected() ? "patient" : "admin";

        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Save Confirmation");
        alert.setHeaderText("Save User");
        alert.setContentText("Are you sure you want to save this user?");

         ButtonType saveButton = new ButtonType("Save");
        ButtonType cancelButton = new ButtonType("Cancel");
        alert.getButtonTypes().setAll(saveButton, cancelButton);

         Optional<ButtonType> result = alert.showAndWait();

        if (result.isPresent() && result.get() == saveButton) {
            try {
                Connection connection = DB.getInstance().getConnection();
                PreparedStatement statement = connection.prepareStatement("INSERT INTO users"
                        + " (username, password, firstname, lastname, age, email, phone, gender, role) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)");
                statement.setString(1, username);
                statement.setString(2, password);
                statement.setString(3, firstname);
                statement.setString(4, lastname);
                statement.setInt(5, age);
                statement.setString(6, email);
                statement.setString(7, phone);
                statement.setString(8, gender);
                statement.setString(9, role);

                int rowsAffected = statement.executeUpdate();
                if (rowsAffected > 0) {
                     Alert successAlert = new Alert(AlertType.INFORMATION);
                    successAlert.setTitle("Success");
                    successAlert.setHeaderText("User Saved");
                    successAlert.setContentText("The user has been saved successfully.");
                    successAlert.showAndWait();

                    if (role.equals("admin")) {
                         FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/admin_dashboard.fxml"));
                        Parent adminDashboardPage = loader.load();
                        Admin_dashboardController adminController = loader.getController();

                        Scene scene = new Scene(adminDashboardPage);
                        Stage currentWindow = (Stage) ((Node) event.getSource()).getScene().getWindow();
                        currentWindow.setScene(scene);
                        currentWindow.show();
                    } else if (role.equals("patient")) {
                         FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/PatientPage.fxml"));
                        Parent adminDashboardPage = loader.load();
                        PatientPageController adminController = loader.getController();

                        Scene scene = new Scene(adminDashboardPage);
                        Stage currentWindow = (Stage) ((Node) event.getSource()).getScene().getWindow();
                        currentWindow.setScene(scene);
                        currentWindow.show();
                    }
                } else {
                     Alert errorAlert = new Alert(AlertType.ERROR);
                    errorAlert.setTitle("Error");
                    errorAlert.setHeaderText("Save Failed");
                    errorAlert.setContentText("Failed to save user data.");
                    errorAlert.showAndWait();
                }

                statement.close();
                connection.close();
            } catch (SQLException | ClassNotFoundException | IOException e) {
                e.printStackTrace();
             }
        } else {
    
        }
    }

    @FXML
    private void btn_Cancel(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Cancel Registration");
        alert.setHeaderText("Cancel Registration");
        alert.setContentText("Are you sure you want to cancel the registration process?");

         ButtonType cancelButton = new ButtonType("Cancel");
        ButtonType confirmButton = new ButtonType("Confirm");
        alert.getButtonTypes().setAll(cancelButton, confirmButton);

         Optional<ButtonType> result = alert.showAndWait();

        if (result.isPresent() && result.get() == confirmButton) {
             txtUserName.setText("");
            txtPassword.setText("");
            txtFirstName.setText("");
            txtLastName.setText("");
            txtAge.setText("");
            txtEmail.setText("");
            txtPhone.setText("");

             radioMale.setSelected(false);
            radioFemale.setSelected(false);
            radioUser.setSelected(false);
            radioAdmin.setSelected(false);
        } else {
     
        }
    }

    public void setPatientData(Patient patient) {
        this.patient = patient;

        txtUserName.setText(patient.getUsername());

        PasswordID.setText(patient.getPassword());
        txtFirstName.setText(patient.getFirstname());
        txtLastName.setText(patient.getLastname());
        txtAge.setText(String.valueOf(patient.getAge()));
        txtEmail.setText(patient.getEmail());
        txtPhone.setText(patient.getPhone());

         if (patient.getGender().equalsIgnoreCase("male")) {
            radioMale.setSelected(true);
        } else if (patient.getGender().equalsIgnoreCase("female")) {
            radioFemale.setSelected(true);
        }
        ID__ID.setText(String.valueOf(patient.getId()));
        PasswordID.setText(patient.getPassword());

        // Set the role radio button
        //   if (patient.getRole().equalsIgnoreCase("patient")) {
        //     radioUser.setSelected(true);
        // } else if (patient.getRole().equalsIgnoreCase("admin")) {
        radioAdmin.setSelected(true);
    }

    /*
    public void setPatientData(Patient patient) {
        this.patient = patient;

    //   // Populate the text fields with patient data
      //   txtUserName.setText(patient.getUsername());
        //  txtPassword.setText(patient.getPassword());
        txtFirstName.setText(patient.getFirstname());
        txtLastName.setText(patient.getLastname());
        txtAge.setText(String.valueOf(patient.getAge()));
        //txtEmail.setText(patient.getEmail());
        txtPhone.setText(patient.getPhone());

        // Set the gender radio button
        if (patient.getGender().equalsIgnoreCase("male")) {
            radioMale.setSelected(true);
        } else if (patient.getGender().equalsIgnoreCase("female")) {
            radioFemale.setSelected(true);
        }

        // Set the role radio button
        // if (patient.getRole().equalsIgnoreCase("patient")) {
        //  radioUser.setSelected(true);
        //  } else if (patient.getRole().equalsIgnoreCase("admin")) {
        radioAdmin.setSelected(true);
    }
     */
    @FXML
    private void btn_Update(ActionEvent event) {
         String username = txtUserName.getText();
        String password = PasswordID.getText();
        String firstname = txtFirstName.getText();
        String lastname = txtLastName.getText();
        int age = Integer.parseInt(txtAge.getText());
        String email = txtEmail.getText();
        String phone = txtPhone.getText();
        String gender = radioMale.isSelected() ? "male" : "female";
        String role = radioUser.isSelected() ? "patient" : "admin";

        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Update Confirmation");
        alert.setHeaderText("Update User");
        alert.setContentText("Are you sure you want to update this user?");

         ButtonType updateButton = new ButtonType("Update");
        ButtonType cancelButton = new ButtonType("Cancel");
        alert.getButtonTypes().setAll(updateButton, cancelButton);

         Optional<ButtonType> result = alert.showAndWait();

        if (result.isPresent() && result.get() == updateButton) {
            try {
                Connection connection = DB.getInstance().getConnection();
                PreparedStatement statement = connection.prepareStatement("UPDATE users SET "
                        + "username=?, password=?, firstname=?, lastname=?, age=?, email=?, phone=?, gender=?, role=? WHERE id=?");
                statement.setString(1, username);
                statement.setString(2, password);
                statement.setString(3, firstname);
                statement.setString(4, lastname);
                statement.setInt(5, age);
                statement.setString(6, email);
                statement.setString(7, phone);
                statement.setString(8, gender);
                statement.setString(9, role);
                statement.setInt(10, patient.getId());  

                int rowsAffected = statement.executeUpdate();
                if (rowsAffected > 0) {
              
                    Alert successAlert = new Alert(AlertType.INFORMATION);
                    successAlert.setTitle("Success");
                    successAlert.setHeaderText("User Updated");
                    successAlert.setContentText("The user has been updated successfully.");
                    successAlert.showAndWait();

                     FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/admin_dashboard.fxml"));
                    Parent adminDashboardPage = loader.load();
                    Admin_dashboardController adminController = loader.getController();

                    Scene scene = new Scene(adminDashboardPage);
                    Stage currentWindow = (Stage) ((Node) event.getSource()).getScene().getWindow();
                    currentWindow.setScene(scene);
                    currentWindow.show();
                } else {
                     Alert errorAlert = new Alert(AlertType.ERROR);
                    errorAlert.setTitle("Error");
                    errorAlert.setHeaderText("Update Failed");
                    errorAlert.setContentText("Failed to update user data.");
                    errorAlert.showAndWait();
                }

                statement.close();
                connection.close();
            } catch (SQLException | ClassNotFoundException | IOException e) {
                e.printStackTrace();
             }
        } else {
           
        }
    }

}
