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
import java.sql.SQLException;
import java.time.LocalDate;
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
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.DB;

/**
 * FXML Controller class
 *
 * @author Home Tech
 */
public class AppointmentsController implements Initializable {

    @FXML
    private DatePicker dataID;
    @FXML
    private TextField dayID;
    @FXML
    private TextField timeID;
    @FXML
    private TextField statID;

    private Appointment selectedAppointment;
    @FXML
    private TextField id_id;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    @FXML
    private void btn_AdminPage(ActionEvent event) throws IOException {
        Parent previousPage = FXMLLoader.load(getClass().getResource("/View/admin_dashboard.fxml"));
        Scene scene = new Scene(previousPage);
        Stage currentWindow = (Stage) ((Node) event.getSource()).getScene().getWindow();
        currentWindow.setScene(scene);
        currentWindow.show();
    }

    @FXML
    private void btn_Appo(ActionEvent event) throws IOException {
        Parent previousPage = FXMLLoader.load(getClass().getResource("/View/appointment.fxml"));
        Scene scene = new Scene(previousPage);
        Stage currentWindow = (Stage) ((Node) event.getSource()).getScene().getWindow();
        currentWindow.setScene(scene);
        currentWindow.show();
    }

    @FXML
    private void btn_Save(ActionEvent event) throws SQLException {
         LocalDate appointment_date = dataID.getValue();
        String appointment_day = dayID.getText();
        String appointment_time = timeID.getText();
        String status = statID.getText();
 
         if (appointment_date == null || appointment_day.isEmpty() || appointment_time.isEmpty() || status.isEmpty()) {
             Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Input Error");
            alert.setHeaderText("Please fill in all fields");
            alert.showAndWait();
            return;
        }
        try {
             Connection con = DB.getInstance().getConnection();
             String query = "INSERT INTO appointments (appointment_date, appointment_day, appointment_time, status) VALUES (?, ?, ?, ?)";
            PreparedStatement statement = con.prepareStatement(query);
             statement.setDate(1, java.sql.Date.valueOf(appointment_date));
            statement.setString(2, appointment_day);
            statement.setString(3, appointment_time);
            statement.setString(4, status);
             statement.executeUpdate();
             statement.close();
            con.close();
             dataID.setValue(null);
            dayID.clear();
            timeID.clear();
            statID.clear();
             Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Success");
            alert.setHeaderText("Appointment saved successfully");
            alert.showAndWait();
        } catch (ClassNotFoundException | SQLException e) {
             e.printStackTrace();
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Database Error");
            alert.setHeaderText("An error occurred while saving the appointment");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }

    @FXML
    private void btn_Cancel(ActionEvent event) {
         dataID.setValue(null);
        dayID.clear();
        timeID.clear();
        statID.clear();
    }

    public void setAppointmentData(Appointment appointment) {
        selectedAppointment = appointment;
        id_id.setText(appointment.getId());
        dataID.setValue(LocalDate.parse(appointment.getDate()));
        dayID.setText(appointment.getDay());
        timeID.setText(appointment.getTime());
        statID.setText(appointment.getStatus());
    }

    @FXML
    private void Update(ActionEvent event) throws SQLException {
         LocalDate appointment_date = dataID.getValue();
        String appointment_day = dayID.getText();
        String appointment_time = timeID.getText();
        String status = statID.getText();

         if (selectedAppointment == null || appointment_date == null || appointment_day.isEmpty() || appointment_time.isEmpty() || status.isEmpty()) {
             Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Input Error");
            alert.setHeaderText("Please select an appointment and fill in all fields");
            alert.showAndWait();
            return;
        }

        try {
             Connection con = DB.getInstance().getConnection();

             String query = "UPDATE appointments SET appointment_date = ?, appointment_day = ?, appointment_time = ?, status = ? WHERE id = ?";
            PreparedStatement statement = con.prepareStatement(query);
             statement.setDate(1, java.sql.Date.valueOf(appointment_date));
            statement.setString(2, appointment_day);
            statement.setString(3, appointment_time);
            statement.setString(4, status);
            statement.setString(5, selectedAppointment.getId());

             statement.executeUpdate();

             statement.close();
            con.close();

             id_id.clear();
            dataID.setValue(null);
            dayID.clear();
            timeID.clear();
            statID.clear();

             Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Success");
            alert.setHeaderText("Appointment updated successfully");
            alert.showAndWait();
        } catch (ClassNotFoundException | SQLException e) {
             e.printStackTrace();
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Database Error");
            alert.setHeaderText("An error occurred while updating the appointment");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }

    @FXML
    private void btn_booked(ActionEvent event) {
             try {
            Parent appointmentPage = FXMLLoader.load(getClass().getResource("/View/bookedpage.fxml"));
            Scene scene = new Scene(appointmentPage);
            Stage currentWindow = (Stage) ((Node) event.getSource()).getScene().getWindow();
            currentWindow.setScene(scene);
            currentWindow.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
