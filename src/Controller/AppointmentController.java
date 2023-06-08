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
import java.util.ArrayList;
import java.util.List;
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
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.DB;

/**
 * FXML Controller class
 *
 * @author Home Tech
 */
public class AppointmentController implements Initializable {

    @FXML
    private TableColumn<Appointment, String> id;
    @FXML
    private TableColumn<Appointment, String> appointment_date;
    @FXML
    private TableColumn<Appointment, String> appointment_day;
    @FXML
    private TableColumn<Appointment, String> appointment_time;
    @FXML
    private TableColumn<Appointment, String> status;
    @FXML
    private TableView<Appointment> tabel_Appo;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    public void initializeTable() {
         id.setCellValueFactory(new PropertyValueFactory<>("id"));
        appointment_date.setCellValueFactory(new PropertyValueFactory<>("date")); // Update with the correct attribute name
        appointment_day.setCellValueFactory(new PropertyValueFactory<>("day")); // Update with the correct attribute name
        appointment_time.setCellValueFactory(new PropertyValueFactory<>("time")); // Update with the correct attribute name
        status.setCellValueFactory(new PropertyValueFactory<>("status"));
    }

    @FXML
    private void btn_adminPage(ActionEvent event) throws IOException {
        Parent previousPage = FXMLLoader.load(getClass().getResource("/View/admin_dashboard.fxml"));
        Scene scene = new Scene(previousPage);
        Stage currentWindow = (Stage) ((Node) event.getSource()).getScene().getWindow();
        currentWindow.setScene(scene);
        currentWindow.show();
    }

    @FXML
    private void btn_AppoPage(ActionEvent event) {
    }

    @FXML
    private void btn_ShowApp(ActionEvent event) {
        try {
            List<Appointment> appointmentList = retrieveAppointmentsFromDatabase();

             ObservableList<Appointment> data = FXCollections.observableArrayList(appointmentList);

             tabel_Appo.setItems(data);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private List<Appointment> retrieveAppointmentsFromDatabase() {
        List<Appointment> appointmentList = new ArrayList<>();

        try {
            DB db = DB.getInstance();
            Connection con = db.getConnection();

             String query = "SELECT id, appointment_date, appointment_day, appointment_time, status FROM appointments";
            PreparedStatement statement = con.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();

             while (resultSet.next()) {
                String id = resultSet.getString("id");
                String date = resultSet.getString("appointment_date");
                String day = resultSet.getString("appointment_day");
                String time = resultSet.getString("appointment_time");
                String status = resultSet.getString("status");

                Appointment appointment = new Appointment(id, date, day, time, status);
                appointmentList.add(appointment);
            }
            initializeTable();

             resultSet.close();
            statement.close();
            con.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return appointmentList;
    }

    @FXML
    private void btn_NewApp(ActionEvent event) throws IOException {
        Parent previousPage = FXMLLoader.load(getClass().getResource("/View/Appointments.fxml"));
        Scene scene = new Scene(previousPage);
        Stage currentWindow = (Stage) ((Node) event.getSource()).getScene().getWindow();
        currentWindow.setScene(scene);
        currentWindow.show();
    }

   @FXML
private void btn_UpdateAppo(ActionEvent event) throws IOException {
    Appointment selectedAppointment = tabel_Appo.getSelectionModel().getSelectedItem();
    if (selectedAppointment == null) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText("No Appointment Selected");
        alert.setContentText("Please select an appointment to update.");
        alert.showAndWait();
        return;
    }
    FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/Appointments.fxml"));
    Parent root = loader.load();
    AppointmentsController appointmentsController = loader.getController();

     appointmentsController.setAppointmentData(selectedAppointment);

     Scene scene = new Scene(root);
    Stage currentWindow = (Stage) ((Node) event.getSource()).getScene().getWindow();
    currentWindow.setScene(scene);
    currentWindow.show();
}


    @FXML
    private void btn_DeleteAppo(ActionEvent event) {
         Appointment selectedAppointment = tabel_Appo.getSelectionModel().getSelectedItem();
        if (selectedAppointment == null) {
             Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("No Appointment Selected");
            alert.setContentText("Please select an appointment to delete.");
            alert.showAndWait();
            return;
        }

         Alert confirmationAlert = new Alert(AlertType.CONFIRMATION);
        confirmationAlert.setTitle("Confirmation");
        confirmationAlert.setHeaderText("Delete Appointment");
        confirmationAlert.setContentText("Are you sure you want to delete the selected appointment?");
        confirmationAlert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                 try {
                     deleteAppointment(selectedAppointment);
                     tabel_Appo.getItems().remove(selectedAppointment);
                      Alert successAlert = new Alert(AlertType.INFORMATION);
                    successAlert.setTitle("Success");
                    successAlert.setHeaderText("Appointment Deleted");
                    successAlert.setContentText("The selected appointment has been deleted.");
                    successAlert.showAndWait();
                } catch (Exception e) {
                     Alert errorAlert = new Alert(AlertType.ERROR);
                    errorAlert.setTitle("Error");
                    errorAlert.setHeaderText("Deletion Failed");
                    errorAlert.setContentText("An error occurred while deleting the appointment. Please try again.");
                    errorAlert.showAndWait();
                    e.printStackTrace();
                }
            }
        });
    }

    private void deleteAppointment(Appointment appointment) throws SQLException, ClassNotFoundException {
        Connection con = null;
        PreparedStatement statement = null;
        try {
             con = DB.getInstance().getConnection();
              String query = "DELETE FROM appointments WHERE id = ?";
            statement = con.prepareStatement(query);
            statement.setString(1, appointment.getId());
             statement.executeUpdate();
        } catch (ClassNotFoundException | SQLException e) {
             throw e;
        } finally {
             if (statement != null) {
                statement.close();
            }
            if (con != null) {
                con.close();
            }
        }
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
