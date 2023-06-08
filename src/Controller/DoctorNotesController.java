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
public class DoctorNotesController implements Initializable {

    @FXML
    private TableView<Note> tabelNotes;
    @FXML
    private TableColumn<Note, String> id;
    @FXML
    private TableColumn<Note, String> ID;
    @FXML
    private TableColumn<Note, String> id_user;
    @FXML
    private TableColumn<Note, String> statusdoctor;
    @FXML
    private TableColumn<Note, String> doctorComment;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    public void initializeTable() {
         id.setCellValueFactory(new PropertyValueFactory<>("id"));
        ID.setCellValueFactory(new PropertyValueFactory<>("appointmentId"));  
        id_user.setCellValueFactory(new PropertyValueFactory<>("userId"));  
        statusdoctor.setCellValueFactory(new PropertyValueFactory<>("doctorStatus"));  
        doctorComment.setCellValueFactory(new PropertyValueFactory<>("doctorComment"));
    }

    private List<Note> retrieveAppointmentsFromDatabase() {
        List<Note> appointmentList = new ArrayList<>();

        try {
            DB db = DB.getInstance();
            Connection con = db.getConnection();

             String query = "SELECT id, appointment_id, user_id, status, doctor_comment FROM booked_appointments";
            PreparedStatement statement = con.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();

             while (resultSet.next()) {
                String id = resultSet.getString("id");
                String ID = resultSet.getString("appointment_id");
                String id_user = resultSet.getString("user_id");
                String statusdoctor = resultSet.getString("status");
                String doctorComment = resultSet.getString("doctor_comment");

                Note note = new Note(id, ID, id_user, statusdoctor, doctorComment);
                appointmentList.add(note);
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
    private void Show_Notes(ActionEvent event) {
        try {
            List<Note> appointmentList = retrieveAppointmentsFromDatabase();
            ObservableList<Note> data = FXCollections.observableArrayList(appointmentList);
            tabelNotes.setItems(data);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void btn_LogOut(ActionEvent event) {
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

}
