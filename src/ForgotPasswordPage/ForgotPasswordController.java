package ForgotPasswordPage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import java.sql.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import SignUp.DBConnection;

public class ForgotPasswordController implements Initializable {

    @FXML
    private TextField txtEmailAddress;
    @FXML
    private PasswordField txtNewPassword;
    @FXML
    private PasswordField txtConfirmPassword;
    @FXML
    private Label lblMessage;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    @FXML
    private void resetPassword(ActionEvent event) {
        String emailAddress = txtEmailAddress.getText();
        String newPassword = txtNewPassword.getText();
        String confirmPassword = txtConfirmPassword.getText();

        // Validation 1: Check empty fields
        if (emailAddress.isEmpty() || newPassword.isEmpty() || confirmPassword.isEmpty()) {
            lblMessage.setStyle("-fx-text-fill: red;");
            lblMessage.setText("❌ Fill all fields");
            return;
        }

        // Validation 2: Check password match
        if (!newPassword.equals(confirmPassword)) {
            lblMessage.setStyle("-fx-text-fill: red;");
            lblMessage.setText("❌ Passwords do not match");
            return;
        }

        // Validation 3: Check password length (minimum 6 characters)
        if (newPassword.length() < 6) {
            lblMessage.setStyle("-fx-text-fill: red;");
            lblMessage.setText("❌ Password must be at least 6 characters");
            return;
        }

        Connection con = null;
        PreparedStatement ps = null;

        try {
            con = DBConnection.getConnection();

            // First, check if email exists
            String checkSql = "SELECT emailaddress FROM signup WHERE emailaddress = ?";
            ps = con.prepareStatement(checkSql);
            ps.setString(1, emailAddress);
            ResultSet rs = ps.executeQuery();

            if (!rs.next()) {
                lblMessage.setStyle("-fx-text-fill: red;");
                lblMessage.setText("❌ Email Address Not Found");
                rs.close();
                ps.close();
                return;
            }
            rs.close();
            ps.close();

            // Update password - FIXED: Using proper try-with-resources for update
            String updateSql = "UPDATE signup SET password = ?, confirmpassword = ? WHERE emailaddress = ?";
            ps = con.prepareStatement(updateSql);

            ps.setString(1, newPassword);        // password column
            ps.setString(2, confirmPassword);    // confirmpassword column
            ps.setString(3, emailAddress);       // WHERE condition

            int rows = ps.executeUpdate();

            if (rows > 0) {
                lblMessage.setStyle("-fx-text-fill: green;");
                lblMessage.setText("✅ Password Updated Successfully");

                // Clear fields after successful update
                txtEmailAddress.clear();
                txtNewPassword.clear();
                txtConfirmPassword.clear();

            } else {
                lblMessage.setStyle("-fx-text-fill: red;");
                lblMessage.setText("❌ Failed to update password");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            lblMessage.setStyle("-fx-text-fill: red;");
            lblMessage.setText("❌ Database Error: " + e.getMessage());

        } finally {
            // Properly close resources
            try {
                if (ps != null) {
                    ps.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }

    @FXML
    private void backToLogin(ActionEvent event) throws Exception {
        FXMLLoader loader
                = new FXMLLoader(getClass().getResource("/LoginPage/Login.fxml"));

        Parent root = loader.load();

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

        Scene scene = new Scene(root);

        stage.setScene(scene);
        stage.show();
    }

}
/*
    System.out.println("Password Reset Successfully");

    
      

    @FXML
private void resetPassword(ActionEvent event) {

    String username = txtUsername.getText();
    String newPassword = txtNewPassword.getText();
    String confirmPassword = txtConfirmPassword.getText();

    if (username.isEmpty()
            || newPassword.isEmpty()
            || confirmPassword.isEmpty()) {

        lblMessage.setStyle("-fx-text-fill:red;");
        lblMessage.setText("❌ Fill all fields");
        return;
    }

    if (!newPassword.equals(confirmPassword)) {

        lblMessage.setStyle("-fx-text-fill:red;");
        lblMessage.setText("❌ Passwords do not match");
        return;
    }

    try {

        Connection con = DBConnection.getConnection();

        String sql =
                "UPDATE users SET password=? WHERE username=?";

        PreparedStatement ps =
                con.prepareStatement(sql);

        ps.setString(1, newPassword);
        ps.setString(2, username);

        int rows = ps.executeUpdate();

        if (rows > 0) {

            lblMessage.setStyle("-fx-text-fill:green;");
            lblMessage.setText("✅ Password Updated Successfully");

        } else {

            lblMessage.setStyle("-fx-text-fill:red;");
            lblMessage.setText("❌ Username Not Found");
        }

    } catch (Exception e) {

        e.printStackTrace();

        lblMessage.setStyle("-fx-text-fill:red;");
        lblMessage.setText("❌ Database Error");
    }
}
 */
