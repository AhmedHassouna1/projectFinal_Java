/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import java.io.IOException;
import java.net.URL;
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
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Home Tech
 */
public class ApplecationController implements Initializable {
 
    @Override
    public void initialize(URL url, ResourceBundle rb) {
     }

    
    
    @FXML
    private void btn_LogIn(ActionEvent event) throws IOException {
        Parent previousPage = FXMLLoader.load(getClass().getResource("/View/login_user.fxml"));
        Scene scene = new Scene(previousPage);
        Stage currentWindow = (Stage) ((Node) event.getSource()).getScene().getWindow();
        currentWindow.setScene(scene);
        currentWindow.show();
    }

    @FXML
    private void btn_SingIn(ActionEvent event) throws IOException {
         Parent previousPage = FXMLLoader.load(getClass().getResource("/View/regesster.fxml"));
        Scene scene = new Scene(previousPage);
        Stage currentWindow = (Stage) ((Node) event.getSource()).getScene().getWindow();
        currentWindow.setScene(scene);
        currentWindow.show();
    }

    @FXML
    private void btn_exit(ActionEvent event) {
        Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmationAlert.setTitle("Confirmation");
        confirmationAlert.setHeaderText("Exit Application");
        confirmationAlert.setContentText("Are you sure you want to exit the application?");

        Optional<ButtonType> result = confirmationAlert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
             Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.close();
        }
    }

}
