/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package Records;

import AdmissionPage.AdmissionModel;
import AdmissionPage.DBConnection;
import java.sql.*;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import javafx.stage.FileChooser;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.ss.usermodel.Workbook;

/**
 * FXML Controller class
 *
 * @author kamui
 */
public class StudentRecordsController implements Initializable {

    /**
     * Initializes the controller class.
     */
    @FXML
    private TableView<AdmissionModel> studentTable;

    @FXML
    private TableColumn<AdmissionModel, String> colRollNumber;
    @FXML
    private TableColumn<AdmissionModel, String> colFirstName;
    @FXML
    private TableColumn<AdmissionModel, String> colLastName;
    @FXML
    private TableColumn<AdmissionModel, String> colGender;
    @FXML
    private TableColumn<AdmissionModel, LocalDate> colDob;
    @FXML
    private TableColumn<AdmissionModel, String> colCommunity;
    @FXML
    private TableColumn<AdmissionModel, String> colFatherName;
    @FXML
    private TableColumn<AdmissionModel, String> colMotherName;
    @FXML
    private TableColumn<AdmissionModel, String> colParentOccupation;
    @FXML
    private TableColumn<AdmissionModel, String> colStudentContact;
    @FXML
    private TableColumn<AdmissionModel, String> colParentContact;
    @FXML
    private TableColumn<AdmissionModel, String> colAddress;
    @FXML
    private TableColumn<AdmissionModel, Double> colTenthMark;
    @FXML
    private TableColumn<AdmissionModel, Double> colTwelfthMark;
    @FXML
    private TableColumn<AdmissionModel, String> colUgCollege;
    @FXML
    private TableColumn<AdmissionModel, Double> colUgMark;
    @FXML
    private TableColumn<AdmissionModel, Integer> colYearPassed;

    @FXML
    private TextField searchField;

    @FXML
    private Label recordCountLabel;

    private ObservableList<AdmissionModel> studentList = FXCollections.observableArrayList();
    private FilteredList<AdmissionModel> filteredList;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        setupTableColumns();
        loadStudentData();
        setupSearch();
    }

    private void setupTableColumns() {
        colRollNumber.setCellValueFactory(new PropertyValueFactory<>("rollNumber"));
        colFirstName.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        colLastName.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        colGender.setCellValueFactory(new PropertyValueFactory<>("gender"));
        colDob.setCellValueFactory(new PropertyValueFactory<>("dob"));
        colCommunity.setCellValueFactory(new PropertyValueFactory<>("community"));
        colFatherName.setCellValueFactory(new PropertyValueFactory<>("fatherName"));
        colMotherName.setCellValueFactory(new PropertyValueFactory<>("motherName"));
        colParentOccupation.setCellValueFactory(new PropertyValueFactory<>("parentOccupation"));
        colStudentContact.setCellValueFactory(new PropertyValueFactory<>("studentContact"));
        colParentContact.setCellValueFactory(new PropertyValueFactory<>("parentContact"));
        colAddress.setCellValueFactory(new PropertyValueFactory<>("address"));
        colTenthMark.setCellValueFactory(new PropertyValueFactory<>("tenthMark"));
        colTwelfthMark.setCellValueFactory(new PropertyValueFactory<>("twelfthMark"));
        colUgCollege.setCellValueFactory(new PropertyValueFactory<>("ugCollege"));
        colUgMark.setCellValueFactory(new PropertyValueFactory<>("ugMark"));
        colYearPassed.setCellValueFactory(new PropertyValueFactory<>("yearPassed"));
    }

    private void loadStudentData() {
        studentList.clear();
        String sql = "SELECT * FROM admission ORDER BY student_id DESC";

        try (Connection con = DBConnection.getConnection(); java.sql.Statement stmt = con.createStatement(); // ✅ Use java.sql.Statement
                 ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                AdmissionModel student = new AdmissionModel();
                student.setStudentId(rs.getInt("student_id"));
                student.setRollNumber(rs.getString("roll_number"));
                student.setFirstName(rs.getString("first_name"));
                student.setLastName(rs.getString("last_name"));
                student.setGender(rs.getString("gender"));
                student.setDob(rs.getDate("date_of_birth").toLocalDate());
                student.setCommunity(rs.getString("community"));
                student.setFatherName(rs.getString("father_name"));
                student.setMotherName(rs.getString("mother_name"));
                student.setParentOccupation(rs.getString("parent_occupation"));
                student.setStudentContact(rs.getString("student_contact"));
                student.setParentContact(rs.getString("parent_contact"));
                student.setAddress(rs.getString("address"));
                student.setTenthMark(rs.getDouble("tenth_mark"));
                student.setTwelfthMark(rs.getDouble("twelfth_mark"));
                student.setUgCollege(rs.getString("ug_college"));
                student.setUgMark(rs.getDouble("ug_mark"));
                student.setYearPassed(rs.getInt("year_passed"));

                studentList.add(student);
            }

            filteredList = new FilteredList<>(studentList, p -> true);
            studentTable.setItems(filteredList);
            updateRecordCount();

        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Database Error", "Failed to load student data: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void setupSearch() {
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredList.setPredicate(student -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                String lowerCaseFilter = newValue.toLowerCase();
                return student.getRollNumber().toLowerCase().contains(lowerCaseFilter)
                        || student.getFirstName().toLowerCase().contains(lowerCaseFilter)
                        || student.getLastName().toLowerCase().contains(lowerCaseFilter)
                        || student.getStudentContact().toLowerCase().contains(lowerCaseFilter);
            });
            updateRecordCount();
        });
    }

    @FXML
    private void searchStudents() {
        // Search is handled by the listener, but this method is for the Search button
        setupSearch();
    }

    @FXML
    private void refreshTable() {
        loadStudentData();
        searchField.clear();
        showAlert(Alert.AlertType.INFORMATION, "Success", "Table refreshed successfully!");
    }

    @FXML
    private void exportToExcel() {

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save Excel File");

        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter(
                        "Excel Files (*.xlsx)", "*.xlsx"));

        fileChooser.setInitialFileName(
                "StudentRecords.xlsx");

        File file = fileChooser.showSaveDialog(
                studentTable.getScene().getWindow());

        if (file == null) {
            return;
        }

        try (Workbook workbook = new XSSFWorkbook()) {

            Sheet sheet = workbook.createSheet("Students");

            // Header Row
            Row header = sheet.createRow(0);

            header.createCell(0).setCellValue("Roll Number");
            header.createCell(1).setCellValue("First Name");
            header.createCell(2).setCellValue("Last Name");
            header.createCell(3).setCellValue("Gender");
            header.createCell(4).setCellValue("DOB");
            header.createCell(5).setCellValue("Community");
            header.createCell(6).setCellValue("Father Name");
            header.createCell(7).setCellValue("Mother Name");
            header.createCell(8).setCellValue("Occupation");
            header.createCell(9).setCellValue("Student Contact");
            header.createCell(10).setCellValue("Parent Contact");
            header.createCell(11).setCellValue("Address");
            header.createCell(12).setCellValue("10th Mark");
            header.createCell(13).setCellValue("12th Mark");
            header.createCell(14).setCellValue("UG College");
            header.createCell(15).setCellValue("UG Mark");
            header.createCell(16).setCellValue("Year Passed");

            int rowNum = 1;

            for (AdmissionModel student : studentTable.getItems()) {

                Row row = sheet.createRow(rowNum++);

                row.createCell(0).setCellValue(student.getRollNumber());
                row.createCell(1).setCellValue(student.getFirstName());
                row.createCell(2).setCellValue(student.getLastName());
                row.createCell(3).setCellValue(student.getGender());
                row.createCell(4).setCellValue(student.getDob().toString());
                row.createCell(5).setCellValue(student.getCommunity());
                row.createCell(6).setCellValue(student.getFatherName());
                row.createCell(7).setCellValue(student.getMotherName());
                row.createCell(8).setCellValue(student.getParentOccupation());
                row.createCell(9).setCellValue(student.getStudentContact());
                row.createCell(10).setCellValue(student.getParentContact());
                row.createCell(11).setCellValue(student.getAddress());
                row.createCell(12).setCellValue(student.getTenthMark());
                row.createCell(13).setCellValue(student.getTwelfthMark());
                row.createCell(14).setCellValue(student.getUgCollege());
                row.createCell(15).setCellValue(student.getUgMark());
                row.createCell(16).setCellValue(student.getYearPassed());
            }

            // Auto-size columns
            for (int i = 0; i < 17; i++) {
                sheet.autoSizeColumn(i);
            }

            try (FileOutputStream fos = new FileOutputStream(file)) {
                workbook.write(fos);
            }

            showAlert(Alert.AlertType.INFORMATION,
                    "Success",
                    "Excel file exported successfully!");

        } catch (Exception e) {

            showAlert(Alert.AlertType.ERROR,
                    "Export Failed",
                    e.getMessage());

            e.printStackTrace();
        }
    }

    @FXML
    private void goToAdmission() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/AdmissionPage/Admission.fxml"));
            Stage stage = (Stage) studentTable.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Navigation Error", "Failed to load admission form: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void gotoDashboard(ActionEvent event)
            throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/DashboardPage/Dashboard.fxml"));
        Stage stage
                = (Stage) ((Node) event.getSource())
                        .getScene()
                        .getWindow();
        stage.setScene(new Scene(root));
        stage.show();
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

    private void updateRecordCount() {
        int count = filteredList != null ? filteredList.size() : 0;
        recordCountLabel.setText("Total Records: " + count);
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    private void navigatetoLogin(ActionEvent event)
            throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/LoginPage/Login.fxml"));
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
}
