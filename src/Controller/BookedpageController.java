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
import javafx.scene.control.Button;
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
public class BookedpageController implements Initializable {

    @FXML
    private Button btn_backe;
    @FXML
    private TableColumn<?, ?> id;
    @FXML
    private TextField sarch_ID;
    @FXML
    private Button btn_backe1;
    @FXML
    private TableColumn<?, ?> appointment_date;
    @FXML
    private TableColumn<?, ?> appointment_day;
    @FXML
    private TableColumn<?, ?> appointment_time;
    @FXML
    private TableColumn<?, ?> status;
    @FXML
    private TableView<Appointment> tabel_Appo;

    /**
     * Initializes the controller class.
     */
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

    private List<Appointment> retrieveAppointmentsFromDatabase(String status) {
        List<Appointment> appointmentList = new ArrayList<>();

        try {
            DB db = DB.getInstance();
            Connection con = db.getConnection();

             String query = "SELECT id, appointment_date, appointment_day, appointment_time, status FROM appointments WHERE status = ?";
            PreparedStatement statement = con.prepareStatement(query);
            statement.setString(1, status);  
            ResultSet resultSet = statement.executeQuery();

             while (resultSet.next()) {
                String id = resultSet.getString("id");
                String date = resultSet.getString("appointment_date");
                String day = resultSet.getString("appointment_day");
                String time = resultSet.getString("appointment_time");
                String appointmentStatus = resultSet.getString("status");

                Appointment appointment = new Appointment(id, date, day, time, appointmentStatus);
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

    void setPatientData(Patient patient) {
        throw new UnsupportedOperationException("Not supported yet.");  
    }

    @FXML
    private void btn_adminPage(ActionEvent event) {

    }

    @FXML
    private void btn_booked(ActionEvent event) {

    }

    @FXML
    private void btn_Sarsh(ActionEvent event) {
        String searchText = sarch_ID.getText();

        if (searchText.isEmpty()) {
             return;
        }

        ObservableList<Appointment> appointments = tabel_Appo.getItems();
        List<Appointment> matchingAppointments = new ArrayList<>();

        for (Appointment appointment : appointments) {
            if (appointment.getId().equals(searchText)) {
                matchingAppointments.add(appointment);
            }
        }

         tabel_Appo.getItems().clear();

         tabel_Appo.getItems().addAll(matchingAppointments);
    }

    @FXML
    private void btn_ShowBooked(ActionEvent event) {
        try {
            List<Appointment> appointmentList = retrieveAppointmentsFromDatabase("booked");
             ObservableList<Appointment> data = FXCollections.observableArrayList(appointmentList);

             tabel_Appo.setItems(data);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void btn_AppPage(ActionEvent event) {
        try {
            Parent appointmentPage = FXMLLoader.load(getClass().getResource("/View/Appointments.fxml"));
            Scene scene = new Scene(appointmentPage);
            Stage currentWindow = (Stage) ((Node) event.getSource()).getScene().getWindow();
            currentWindow.setScene(scene);
            currentWindow.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
