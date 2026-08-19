package Attendance;

import AdmissionPage.DBConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.time.LocalDate;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class AttendanceController implements Initializable {

    @FXML
    private DatePicker datePicker;

    @FXML
    private ComboBox<String> semesterCombo;

    @FXML
    private ComboBox<String> sectionCombo;

    @FXML
    private Label totalStudentsLabel;

    @FXML
    private Label presentLabel;

    @FXML
    private Label absentLabel;

    @FXML
    private TableView<AttendanceModel> attendanceTable;

    @FXML
    private TableColumn<AttendanceModel, String> colRoll;

    @FXML
    private TableColumn<AttendanceModel, String> colStudentName;

    @FXML
    private TableColumn<AttendanceModel, Boolean> colP1;

    @FXML
    private TableColumn<AttendanceModel, Boolean> colP2;

    @FXML
    private TableColumn<AttendanceModel, Boolean> colP3;

    @FXML
    private TableColumn<AttendanceModel, Boolean> colP4;

    @FXML
    private TableColumn<AttendanceModel, Boolean> colP5;

    @FXML
    private TableColumn<AttendanceModel, Boolean> colP6;

    @FXML
    private TableColumn<AttendanceModel, Boolean> colP7;

    @FXML
    private Label paginationLabel;

    private ObservableList<AttendanceModel> attendanceList = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Set default date to today
        datePicker.setValue(LocalDate.now());

        // Setup table columns
        setupTableColumns();
    }

    private void setupTableColumns() {
        colRoll.setCellValueFactory(new PropertyValueFactory<>("rollNumber"));
        colStudentName.setCellValueFactory(new PropertyValueFactory<>("studentName"));

        // Setup checkbox columns for each period
        setupCheckBoxColumn(colP1, "p1");
        setupCheckBoxColumn(colP2, "p2");
        setupCheckBoxColumn(colP3, "p3");
        setupCheckBoxColumn(colP4, "p4");
        setupCheckBoxColumn(colP5, "p5");
        setupCheckBoxColumn(colP6, "p6");
        setupCheckBoxColumn(colP7, "p7");
    }

    private void setupCheckBoxColumn(TableColumn<AttendanceModel, Boolean> column, String property) {
        column.setCellValueFactory(cellData -> {
            AttendanceModel model = cellData.getValue();
            switch (property) {
                case "p1": return model.p1Property();
                case "p2": return model.p2Property();
                case "p3": return model.p3Property();
                case "p4": return model.p4Property();
                case "p5": return model.p5Property();
                case "p6": return model.p6Property();
                case "p7": return model.p7Property();
                default: return null;
            }
        });
        column.setCellFactory(CheckBoxTableCell.forTableColumn(column));
        column.setEditable(true);
    }

    @FXML
    private void loadStudents() {
        String semester = semesterCombo.getValue();
        String section = sectionCombo.getValue();
        LocalDate date = datePicker.getValue();

        if (semester == null || section == null || date == null) {
            showAlert(Alert.AlertType.WARNING, "Validation Error", "Please select Date, Semester, and Section!");
            return;
        }

        attendanceList.clear();

        // First, try to load existing attendance for this date
        boolean existingAttendance = loadExistingAttendance(date, semester, section);

        if (!existingAttendance) {
            // If no existing attendance, load students from admission table
            loadStudentsFromAdmission(semester, section);
        }

        attendanceTable.setItems(attendanceList);
        attendanceTable.setEditable(true);
        updateStatistics();
        updatePagination();
    }

    private boolean loadExistingAttendance(LocalDate date, String semester, String section) {
        String sql = "SELECT a.*, ad.roll_number, ad.first_name, ad.last_name " +
                     "FROM attendance a " +
                     "JOIN admission ad ON a.student_id = ad.student_id " +
                     "WHERE a.attendance_date = ? AND a.semester = ? AND a.section = ? " +
                     "ORDER BY ad.roll_number";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setDate(1, Date.valueOf(date));
            ps.setString(2, semester);
            ps.setString(3, section);

            ResultSet rs = ps.executeQuery();
            boolean hasData = false;

            while (rs.next()) {
                hasData = true;
                AttendanceModel model = new AttendanceModel();
                model.setStudentId(rs.getInt("student_id"));
                model.setRollNumber(rs.getString("roll_number"));
                model.setStudentName(rs.getString("first_name") + " " + rs.getString("last_name"));
                model.setP1(rs.getBoolean("p1"));
                model.setP2(rs.getBoolean("p2"));
                model.setP3(rs.getBoolean("p3"));
                model.setP4(rs.getBoolean("p4"));
                model.setP5(rs.getBoolean("p5"));
                model.setP6(rs.getBoolean("p6"));
                model.setP7(rs.getBoolean("p7"));
                attendanceList.add(model);
            }

            return hasData;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private void loadStudentsFromAdmission(String semester, String section) {
        // This is a placeholder - adjust based on your actual student data structure
        // You might need to add semester and section columns to your admission table
        // or create a separate students table

        String sql = "SELECT student_id, roll_number, first_name, last_name FROM admission ORDER BY roll_number";

        try (Connection con = DBConnection.getConnection();
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                AttendanceModel model = new AttendanceModel();
                model.setStudentId(rs.getInt("student_id"));
                model.setRollNumber(rs.getString("roll_number"));
                model.setStudentName(rs.getString("first_name") + " " + rs.getString("last_name"));
                // All periods default to true (present)
                attendanceList.add(model);
            }

        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Database Error", "Failed to load students: " + e.getMessage());
            e.printStackTrace();
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
    private void saveAttendance() {
        if (attendanceList.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "No Data", "Please load students first!");
            return;
        }

        String semester = semesterCombo.getValue();
        String section = sectionCombo.getValue();
        LocalDate date = datePicker.getValue();

        String deleteSql = "DELETE FROM attendance WHERE attendance_date = ? AND semester = ? AND section = ?";
        String insertSql = "INSERT INTO attendance (student_id, attendance_date, semester, section, p1, p2, p3, p4, p5, p6, p7) " +
                          "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection con = DBConnection.getConnection()) {
            con.setAutoCommit(false);

            // Delete existing records for this date/semester/section
            try (PreparedStatement deletePs = con.prepareStatement(deleteSql)) {
                deletePs.setDate(1, Date.valueOf(date));
                deletePs.setString(2, semester);
                deletePs.setString(3, section);
                deletePs.executeUpdate();
            }

            // Insert new records
            try (PreparedStatement insertPs = con.prepareStatement(insertSql)) {
                for (AttendanceModel model : attendanceList) {
                    insertPs.setInt(1, model.getStudentId());
                    insertPs.setDate(2, Date.valueOf(date));
                    insertPs.setString(3, semester);
                    insertPs.setString(4, section);
                    insertPs.setBoolean(5, model.isP1());
                    insertPs.setBoolean(6, model.isP2());
                    insertPs.setBoolean(7, model.isP3());
                    insertPs.setBoolean(8, model.isP4());
                    insertPs.setBoolean(9, model.isP5());
                    insertPs.setBoolean(10, model.isP6());
                    insertPs.setBoolean(11, model.isP7());
                    insertPs.addBatch();
                }
                insertPs.executeBatch();
            }

            con.commit();
            showAlert(Alert.AlertType.INFORMATION, "Success", "Attendance saved successfully!");

        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Database Error", "Failed to save attendance: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void exportAttendance() {
        if (attendanceList.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "No Data", "Please load students first!");
            return;
        }

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Export Attendance");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV Files", "*.csv"));
        fileChooser.setInitialFileName("attendance_" + datePicker.getValue() + ".csv");

        File file = fileChooser.showSaveDialog(attendanceTable.getScene().getWindow());
        if (file != null) {
            try (FileWriter writer = new FileWriter(file)) {
                // Header
                writer.write("Roll Number,Student Name,P1,P2,P3,P4,P5,P6,P7,Present Count,Status\n");

                // Data
                for (AttendanceModel model : attendanceList) {
                    writer.write(String.format("%s,%s,%s,%s,%s,%s,%s,%s,%s,%d,%s\n",
                        model.getRollNumber(),
                        model.getStudentName(),
                        model.isP1() ? "P" : "A",
                        model.isP2() ? "P" : "A",
                        model.isP3() ? "P" : "A",
                        model.isP4() ? "P" : "A",
                        model.isP5() ? "P" : "A",
                        model.isP6() ? "P" : "A",
                        model.isP7() ? "P" : "A",
                        model.getPresentCount(),
                        model.isFullyPresent() ? "Present" : (model.isFullyAbsent() ? "Absent" : "Partial")
                    ));
                }

                showAlert(Alert.AlertType.INFORMATION, "Success", "Attendance exported to " + file.getName());
            } catch (IOException e) {
                showAlert(Alert.AlertType.ERROR, "Export Error", "Failed to export: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    private void updateStatistics() {
        int total = attendanceList.size();
        int present = 0;
        int absent = 0;

        for (AttendanceModel model : attendanceList) {
            if (model.isFullyPresent()) {
                present++;
            } else if (model.isFullyAbsent()) {
                absent++;
            }
        }

        totalStudentsLabel.setText(String.valueOf(total));
        presentLabel.setText(String.valueOf(present));
        absentLabel.setText(String.valueOf(absent));
    }

    private void updatePagination() {
        int total = attendanceList.size();
        if (total > 0) {
            paginationLabel.setText("1-" + total + " of " + total);
        } else {
            paginationLabel.setText("");
        }
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
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
    private void recordsButton(ActionEvent event)
            throws IOException {
        Parent root
                = FXMLLoader.load(
                        getClass().getResource("/Records/StudentRecords.fxml"));

        Stage stage
                = (Stage) ((Node) event.getSource())
                        .getScene()
                        .getWindow();

        stage.setScene(new Scene(root));
        stage.show();
    }
    @FXML
    private void admissionButton(ActionEvent event)
            throws IOException {
        Parent root
                = FXMLLoader.load(
                        getClass().getResource("/AdmissionPage/Admission.fxml"));

        Stage stage
                = (Stage) ((Node) event.getSource())
                        .getScene()
                        .getWindow();

        stage.setScene(new Scene(root));
        stage.show();
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
}