
package LoginPage;

import java.io.IOException;
import java.sql.*;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import smsjavafxapplication.Session;
public class LoginController implements Initializable {
    @FXML private ComboBox<String> loginTypeComboBox;
    @FXML private TextField emailField;
    @FXML private PasswordField passwordField;
    @FXML private Button loginButton;
    @FXML private Button signupButton;
    @FXML private Hyperlink forgotPasswordLink;
    @FXML private Label message;
    


    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Optional: pre-select first item
        // loginTypeComboBox.getSelectionModel().selectFirst();
    }
     
    @FXML
    private void handleLoginTypeSelection() {
        String selected = loginTypeComboBox.getValue();
        System.out.println("Login type: " + selected);
    }
/*
    @FXML
    private void handleLogin() {
        String email = emailField.getText();
        String password = passwordField.getText();
        String role = loginTypeComboBox.getValue();
        System.out.println("Logging in as: " + role + " | " + email);
    }

    @FXML
    private void handleSignup() {
        System.out.println("Navigate to Sign Up page");
    }
*/

    @FXML
    private void loginButton(ActionEvent event)
            throws IOException , ClassNotFoundException, SQLException
    {
        String email = emailField.getText();
        String pass = passwordField.getText();
        
        LoginModel lm =new LoginModel(email, pass);
        
        LoginService ls = new LoginService();
        int res = ls.login(lm);
        
        if(res == 1)
        {  
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Login Successful");
            alert.setHeaderText("Login was Successful");
            alert.showAndWait();
            Session.emailAddress = emailField.getText();
            Session.password = passwordField.getText();
            Parent root = FXMLLoader.load(getClass().getResource("/DashboardPage/Dashboard.fxml"));

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            stage.setScene(new Scene(root));
            stage.setMaximized(true);
            stage.show();          
        }
        else if(res == 0)
        {
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Invalid Username or password!");
            alert.setHeaderText("incorrect username or password");
            alert.showAndWait();
        }
        else if(res == -2)
        {
            Alert alert = new Alert(AlertType.INFORMATION);
             alert.setTitle("Enter the Details!");
             alert.setHeaderText("Enter the email address and password");
             alert.showAndWait();
        }
    }
    @FXML
    private void signupButton(ActionEvent event)
            throws IOException
    {
        Parent root =
        FXMLLoader.load(
        getClass().getResource("/SignUp/SignUp.fxml"));

        Stage stage =
        (Stage)((Node)event.getSource())
        .getScene()
        .getWindow();

        stage.setScene(new Scene(root));
        stage.show();
    }
    @FXML
    private void forgotPassword(ActionEvent event)
            throws IOException
    {
        Parent root =
        FXMLLoader.load(
        getClass().getResource("/ForgotPasswordPage/ForgotPassword.fxml"));

        Stage stage =
        (Stage)((Node)event.getSource())
        .getScene()
        .getWindow();

        stage.setScene(new Scene(root));
        stage.show();
    }
}


