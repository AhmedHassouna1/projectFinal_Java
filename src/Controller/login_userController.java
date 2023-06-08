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
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.DB;

/**
 * FXML Controller class
 *
 * @author Home Tech
 */
public class login_userController implements Initializable {

    /**
     * Initializes the controller class.
     */
    @FXML
    private TextField txtUserName;

    @FXML
    private PasswordField txtPassword;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    @FXML
private void btn_login(ActionEvent event) {
    String username = txtUserName.getText();
    String password = txtPassword.getText();

    try {
        Connection connection = DB.getInstance().getConnection();
        PreparedStatement statement = connection.prepareStatement("SELECT * FROM users WHERE username = ? AND password = ?");
        statement.setString(1, username);
        statement.setString(2, password);

        ResultSet resultSet = statement.executeQuery();
        if (resultSet.next()) {
             System.out.println("Login successful");
            String role = resultSet.getString("role");
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
             System.out.println("Invalid credentials");
         }
        resultSet.close();
        statement.close();
        connection.close();
    } catch (SQLException | ClassNotFoundException | IOException e) {
        e.printStackTrace();
     }
}


    @FXML
    private void btn_register(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/View/regesster.fxml"));
        Scene scene = new Scene(root);

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
        // System.out.println("Run");
    }

    @FXML
    private void btn_backe(ActionEvent event) throws IOException {
         Parent previousPage = FXMLLoader.load(getClass().getResource("/View/Applecation.fxml"));
        Scene scene = new Scene(previousPage);
        Stage currentWindow = (Stage) ((Node) event.getSource()).getScene().getWindow();
        currentWindow.setScene(scene);
        currentWindow.show();
    }

}
