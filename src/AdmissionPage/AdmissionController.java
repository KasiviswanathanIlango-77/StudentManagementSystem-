
package AdmissionPage;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author HP
 */
public class AdmissionController implements Initializable {

    @FXML
    private TextField rollNumber;
    @FXML
    private TextField firstName;
    @FXML
    private TextField lastName;

    @FXML
    private ComboBox<String> gender;
    @FXML
    private ComboBox<String> community;

    @FXML
    private DatePicker dateOfBirth;

    @FXML
    private TextField fatherName;
    @FXML
    private TextField motherName;
    @FXML
    private TextField parentOccupation;

    @FXML
    private TextField studentContact;
    @FXML
    private TextField parentContact;

    @FXML
    private TextArea address;

    @FXML
    private TextField tenthMarkPercentage;
    @FXML
    private TextField twelvethMarkPercentage;

    @FXML
    private TextField ugCollege;
    @FXML
    private TextField ugMarkPercent;
    @FXML
    private TextField yearPassed;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    private void resetForm() {
        rollNumber.clear();
        firstName.clear();
        lastName.clear();

        gender.setValue(null);
        community.setValue(null);

        dateOfBirth.setValue(null);

        fatherName.clear();
        motherName.clear();
        parentOccupation.clear();

        studentContact.clear();
        parentContact.clear();

        address.clear();

        tenthMarkPercentage.clear();
        twelvethMarkPercentage.clear();

        ugCollege.clear();
        ugMarkPercent.clear();
        yearPassed.clear();
    }

    @FXML
    private void resetForm(ActionEvent event) {
        rollNumber.clear();
        firstName.clear();
        lastName.clear();

        gender.setValue(null);
        community.setValue(null);

        dateOfBirth.setValue(null);

        fatherName.clear();
        motherName.clear();
        parentOccupation.clear();

        studentContact.clear();
        parentContact.clear();

        address.clear();

        tenthMarkPercentage.clear();
        twelvethMarkPercentage.clear();

        ugCollege.clear();
        ugMarkPercent.clear();
        yearPassed.clear();
    }

    /**
     * Validates all form fields before saving
     * @return true if all fields are filled, false otherwise
     */
    private boolean validateForm() {
        StringBuilder missingFields = new StringBuilder();

        if (isEmpty(rollNumber)) missingFields.append("Roll Number\n");
        if (isEmpty(firstName)) missingFields.append("First Name\n");
        if (isEmpty(lastName)) missingFields.append("Last Name\n");
        if (gender.getValue() == null || gender.getValue().isEmpty()) missingFields.append("Gender\n");
        if (dateOfBirth.getValue() == null) missingFields.append("Date of Birth\n");
        if (community.getValue() == null || community.getValue().isEmpty()) missingFields.append("Community\n");
        if (isEmpty(fatherName)) missingFields.append("Father Name\n");
        if (isEmpty(motherName)) missingFields.append("Mother Name\n");
        if (isEmpty(parentOccupation)) missingFields.append("Parent Occupation\n");
        if (isEmpty(studentContact)) missingFields.append("Student Contact\n");
        if (isEmpty(parentContact)) missingFields.append("Parent Contact\n");
        if (isEmpty(address)) missingFields.append("Address\n");
        if (isEmpty(tenthMarkPercentage)) missingFields.append("10th Mark %\n");
        if (isEmpty(twelvethMarkPercentage)) missingFields.append("12th Mark %\n");
        if (isEmpty(ugCollege)) missingFields.append("UG College\n");
        if (isEmpty(ugMarkPercent)) missingFields.append("UG Mark %\n");
        if (isEmpty(yearPassed)) missingFields.append("Year Passed\n");

        if (missingFields.length() > 0) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Validation Error");
            alert.setHeaderText("Please fill all required fields:");
            alert.setContentText(missingFields.toString());
            alert.showAndWait();
            return false;
        }

        // Validate numeric fields
        if (!isValidDouble(tenthMarkPercentage.getText())) {
            showErrorAlert("10th Mark % must be a valid number");
            return false;
        }
        if (!isValidDouble(twelvethMarkPercentage.getText())) {
            showErrorAlert("12th Mark % must be a valid number");
            return false;
        }
        if (!isValidDouble(ugMarkPercent.getText())) {
            showErrorAlert("UG Mark % must be a valid number");
            return false;
        }
        if (!isValidInteger(yearPassed.getText())) {
            showErrorAlert("Year Passed must be a valid integer");
            return false;
        }

        // Validate percentage ranges (0-100)
        double tenthMark = Double.parseDouble(tenthMarkPercentage.getText());
        double twelfthMark = Double.parseDouble(twelvethMarkPercentage.getText());
        double ugMark = Double.parseDouble(ugMarkPercent.getText());

        if (tenthMark < 0 || tenthMark > 100) {
            showErrorAlert("10th Mark % must be between 0 and 100");
            return false;
        }
        if (twelfthMark < 0 || twelfthMark > 100) {
            showErrorAlert("12th Mark % must be between 0 and 100");
            return false;
        }
        if (ugMark < 0 || ugMark > 100) {
            showErrorAlert("UG Mark % must be between 0 and 100");
            return false;
        }

        return true;
    }

    private boolean isEmpty(TextField field) {
        return field.getText() == null || field.getText().trim().isEmpty();
    }

    private boolean isEmpty(TextArea field) {
        return field.getText() == null || field.getText().trim().isEmpty();
    }

    private boolean isValidDouble(String value) {
        try {
            Double.parseDouble(value.trim());
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private boolean isValidInteger(String value) {
        try {
            Integer.parseInt(value.trim());
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private void showErrorAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Validation Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    private void saveStudent(ActionEvent event) {

        // Validate form before saving
        if (!validateForm()) {
            return; // Stop if validation fails
        }

        String sql = """
    INSERT INTO admission(
        roll_number,
        first_name,
        last_name,
        gender,
        date_of_birth,
        community,
        father_name,
        mother_name,
        parent_occupation,
        student_contact,
        parent_contact,
        address,
        tenth_mark,
        twelfth_mark,
        ug_college,
        ug_mark,
        year_passed
    )
    VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)
    """;
        try (
                Connection con = DBConnection.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, rollNumber.getText().trim());
            ps.setString(2, firstName.getText().trim());
            ps.setString(3, lastName.getText().trim());
            ps.setString(4, gender.getValue());

            ps.setDate(5, Date.valueOf(dateOfBirth.getValue()));

            ps.setString(6, community.getValue());

            ps.setString(7, fatherName.getText().trim());
            ps.setString(8, motherName.getText().trim());
            ps.setString(9, parentOccupation.getText().trim());

            ps.setString(10, studentContact.getText().trim());
            ps.setString(11, parentContact.getText().trim());

            ps.setString(12, address.getText().trim());

            ps.setDouble(13,
                    Double.parseDouble(
                            tenthMarkPercentage.getText().trim()));

            ps.setDouble(14,
                    Double.parseDouble(
                            twelvethMarkPercentage.getText().trim()));

            ps.setString(15, ugCollege.getText().trim());

            ps.setDouble(16,
                    Double.parseDouble(
                            ugMarkPercent.getText().trim()));

            ps.setInt(17,
                    Integer.parseInt(
                            yearPassed.getText().trim()));

            ps.executeUpdate();

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Success");
            alert.setHeaderText(null);
            alert.setContentText("Student Saved Successfully");
            alert.showAndWait();

            resetForm();

        } catch (Exception e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Database Error");
            alert.setHeaderText(null);
            alert.setContentText("Failed to save student: " + e.getMessage());
            alert.showAndWait();
        }
    }

    @FXML
    private void navigatetoLogin(ActionEvent event) 
            throws IOException{
        Parent root = FXMLLoader.load(getClass().getResource("/LoginPage/Login.fxml"));
        Stage stage
                = (Stage) ((Node) event.getSource())
                        .getScene()
                        .getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }

    @FXML
    private void gotoDashboard(ActionEvent event) 
            throws IOException{
        Parent root = FXMLLoader.load(getClass().getResource("/DashboardPage/Dashboard.fxml"));
        Stage stage
                = (Stage) ((Node) event.getSource())
                        .getScene()
                        .getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }

    @FXML
    private void viewStudents(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/Records/StudentRecords.fxml"));
            Stage stage = (Stage) rollNumber.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Failed to load student records: " + e.getMessage());
            alert.show();
        }
    }

    @FXML
    private void timetableButton(ActionEvent event)
            throws IOException {
        Parent root
                = FXMLLoader.load(
                        getClass().getResource("/Timetable/Timetable.fxml"));

        Stage stage
                = (Stage) ((Node) event.getSource())
                        .getScene()
                        .getWindow();

        stage.setScene(new Scene(root));
        stage.show();
    }

    @FXML
    private void attendanceButton(ActionEvent event)
            throws IOException {
        Parent root
                = FXMLLoader.load(
                        getClass().getResource("/Attendance/Attendance.fxml"));

        Stage stage
                = (Stage) ((Node) event.getSource())
                        .getScene()
                        .getWindow();

        stage.setScene(new Scene(root));
        stage.show();
    }

    @FXML
    private void internalmarksButton(ActionEvent event)
            throws IOException {
        Parent root
                = FXMLLoader.load(
                        getClass().getResource("/InternalMarks/InternalMarks.fxml"));

        Stage stage
                = (Stage) ((Node) event.getSource())
                        .getScene()
                        .getWindow();

        stage.setScene(new Scene(root));
        stage.show();
    }

    @FXML
    private void recordsButton(ActionEvent event) {
        viewStudents(event);
    }
}