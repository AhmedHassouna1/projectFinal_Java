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
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.DB;

/**
 * FXML Controller class
 *
 * @author Home Tech
 */
public class PatientPageController implements Initializable {

    @FXML
    private TableView<Appointment> tabel_Appo;

    @FXML
    private TableColumn<?, ?> id;
    @FXML
    private TableColumn<?, ?> appointment_date;
    @FXML
    private TableColumn<?, ?> appointment_day;
    @FXML
    private TableColumn<?, ?> appointment_time;
    @FXML
    private TableColumn<?, ?> status;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    public void initializeTable() {
         id.setCellValueFactory(new PropertyValueFactory<>("id"));
        appointment_date.setCellValueFactory(new PropertyValueFactory<>("date"));  
        appointment_day.setCellValueFactory(new PropertyValueFactory<>("day"));  
        appointment_time.setCellValueFactory(new PropertyValueFactory<>("time"));  
        status.setCellValueFactory(new PropertyValueFactory<>("status"));
    }


    @FXML
    private void btn_Logout(ActionEvent event) {
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
    private void btn_ShowApp(ActionEvent event) {
        try {
            List<Appointment> appointmentList = retrieveAppointmentsFromDatabase("free");
             ObservableList<Appointment> data = FXCollections.observableArrayList(appointmentList);

             tabel_Appo.setItems(data);
        } catch (Exception e) {
            e.printStackTrace();
        }
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
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @FXML
    private void btn_book(ActionEvent event) {
         Appointment selectedAppointment = tabel_Appo.getSelectionModel().getSelectedItem();
        if (selectedAppointment != null) {
            try {
                 DB db = DB.getInstance();
                Connection con = db.getConnection();
                 String query = "UPDATE appointments SET status = ? WHERE id = ?";
                PreparedStatement statement = con.prepareStatement(query);
                statement.setString(1, "booked");
                statement.setString(2, selectedAppointment.getId());
                statement.executeUpdate();

                 selectedAppointment.setStatus("booked");
                tabel_Appo.refresh();

                 statement.close();
                con.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
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
    private void btn_ShowAll(ActionEvent event) {
        try {
            List<Appointment> appointmentList = retrieveAppointmentsFromDatabase("free", "booked");
             ObservableList<Appointment> data = FXCollections.observableArrayList(appointmentList);

             tabel_Appo.setItems(data);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private List<Appointment> retrieveAppointmentsFromDatabase(String... statuses) {
        List<Appointment> appointmentList = new ArrayList<>();

        try {
            DB db = DB.getInstance();
            Connection con = db.getConnection();

             StringBuilder queryBuilder = new StringBuilder("SELECT id, appointment_date, appointment_day, appointment_time, status FROM appointments WHERE status IN (");
            for (int i = 0; i < statuses.length; i++) {
                queryBuilder.append("?");
                if (i != statuses.length - 1) {
                    queryBuilder.append(", ");
                }
            }
            queryBuilder.append(")");

             PreparedStatement statement = con.prepareStatement(queryBuilder.toString());
            for (int i = 0; i < statuses.length; i++) {
                statement.setString(i + 1, statuses[i]);
            }

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

    @FXML
    private void btn_DoctorNotes(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/doctorNotes.fxml"));
            Parent doctorNotesParent = loader.load();
            DoctorNotesController doctorNotesController = loader.getController();
            Scene doctorNotesScene = new Scene(doctorNotesParent);
            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            currentStage.setScene(doctorNotesScene);
            currentStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
