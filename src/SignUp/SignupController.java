
package SignUp;

import java.net.URL;
import java.util.ResourceBundle;
import java.sql.Connection;
import java.sql.PreparedStatement;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;


public class SignupController implements Initializable {

     @FXML
    private TextField fullnameField;

    @FXML
    private TextField emailaddressField;

    @FXML
    private TextField mobile_numberField;

    @FXML
    private ComboBox<String> roleComboBox;
    
    @FXML
    private PasswordField passwordField;
    
    @FXML
    private PasswordField confirmpasswordField;

    @FXML
    private Label messageLabel;
    
     @Override
    public void initialize(URL url, ResourceBundle rb) {
       roleComboBox.setItems(
            FXCollections.observableArrayList(
                "Admin",
                "Faculty",
                "HOD"
            )
        );
    } 
     @FXML
    private void signupUser() {

        try {

            Connection conn =
                    DBConnection.getConnection();

            String sql =
                "INSERT INTO signup(fullname,emailaddress,mobile_number,role,password,confirmpassword)"
                + " VALUES(?,?,?,?,?,?)";

            PreparedStatement pst =
                    conn.prepareStatement(sql);

            pst.setString(
                    1,
                    fullnameField.getText());

            pst.setString(
                    2,
                    emailaddressField.getText());

            pst.setString(
                    3,
                    mobile_numberField.getText());

            pst.setString(
                    4,
                    roleComboBox.getValue());
            pst.setString(
                    5,
                    passwordField.getText());
            pst.setString(
                    6,
                    confirmpasswordField.getText());

            int result =
                    pst.executeUpdate();

            if(result > 0) {

                messageLabel.setText(
                        "Account Created Successfully");
                fullnameField.clear();
                emailaddressField.clear();
                mobile_numberField.clear();
                roleComboBox.setValue(null);
                passwordField.clear();
                confirmpasswordField.clear();

            }

            conn.close();

        }
        catch(Exception e) {

            messageLabel.setText(
                    "Error : " + e.getMessage());

            e.printStackTrace();
        }
    }


    @FXML
    private void handleLoginRedirect(ActionEvent event) throws Exception {
        FXMLLoader loader
                = new FXMLLoader(getClass().getResource("/LoginPage/Login.fxml"));

        Parent root = loader.load();

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

        Scene scene = new Scene(root);

        stage.setScene(scene);
        stage.show();
    }

    
}
