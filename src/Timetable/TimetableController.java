package Timetable;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
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
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.stage.Stage;

public class TimetableController implements Initializable {

    // FXML fx:id must match exactly with FXML file
    @FXML private ComboBox<String> semesterCombo;
    @FXML private ComboBox<String> sectionCombo;
    @FXML private ComboBox<String> academicYearComboBox;
    @FXML private Button viewButton;
    
    @FXML private TableView<TimetableRow> timetableTable;
    @FXML private TableColumn<TimetableRow, String> dayColumn;
    @FXML private TableColumn<TimetableRow, String> p1Column;
    @FXML private TableColumn<TimetableRow, String> p2Column;
    @FXML private TableColumn<TimetableRow, String> p3Column;
    @FXML private TableColumn<TimetableRow, String> p4Column;
    @FXML private TableColumn<TimetableRow, String> p5Column;
    @FXML private TableColumn<TimetableRow, String> p6Column;
    @FXML private TableColumn<TimetableRow, String> p7Column;
    
    @FXML private Button saveButton;
    @FXML private Button editButton;
    @FXML private Button logoutButton;  // Added for FXML onAction
    
    private ObservableList<TimetableRow> timetableData;
    private boolean isEditMode = false;
    
    private TimetableDAO timetableDAO;
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        timetableDAO = new TimetableDAO();
        
        initializeComboBoxes();
        initializeTable();
        // Removed button action bindings - using FXML onAction instead
    }
    
    private void initializeComboBoxes() {
        semesterCombo.setItems(FXCollections.observableArrayList("1", "2", "3"));
        sectionCombo.setItems(FXCollections.observableArrayList("A", "B"));
        academicYearComboBox.setItems(FXCollections.observableArrayList("2026-2027", "2025-2026"));
    }
    
    private void initializeTable() {
        dayColumn.setCellValueFactory(new PropertyValueFactory<>("day"));
        p1Column.setCellValueFactory(new PropertyValueFactory<>("p1"));
        p2Column.setCellValueFactory(new PropertyValueFactory<>("p2"));
        p3Column.setCellValueFactory(new PropertyValueFactory<>("p3"));
        p4Column.setCellValueFactory(new PropertyValueFactory<>("p4"));
        p5Column.setCellValueFactory(new PropertyValueFactory<>("p5"));
        p6Column.setCellValueFactory(new PropertyValueFactory<>("p6"));
        p7Column.setCellValueFactory(new PropertyValueFactory<>("p7"));
        
        timetableData = FXCollections.observableArrayList();
        timetableTable.setItems(timetableData);
        timetableTable.setPlaceholder(new Label("No content in table"));
    }
    
    // Must be @FXML because FXML calls it via onAction="#loadTimetable"
    @FXML
    private void loadTimetable() {
        String sem = semesterCombo.getValue();
        String sec = sectionCombo.getValue();
        String year = academicYearComboBox.getValue();
        
        if (sem == null || sec == null || year == null) {
            showAlert(Alert.AlertType.WARNING, "Missing Selection", "Please select Semester, Section and Academic Year!");
            return;
        }
        
        // Fetch from PostgreSQL database
        List<TimetableRow> data = timetableDAO.getTimetable(sem, sec, year);
        
        timetableData.clear();
        if (data != null && !data.isEmpty()) {
            timetableData.addAll(data);
        } else {
            showAlert(Alert.AlertType.INFORMATION, "No Data", "No timetable found for Semester " + sem + " - Section " + sec);
        }
    }
    
    @FXML
    private void handleSave() {
        String sem = semesterCombo.getValue();
        String sec = sectionCombo.getValue();
        String year = academicYearComboBox.getValue();
        
        if (sem == null || sec == null || year == null) {
            showAlert(Alert.AlertType.WARNING, "Warning", "Please select Semester, Section and Academic Year!");
            return;
        }
        
        if (timetableData.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Warning", "No timetable data to save!");
            return;
        }
        
        // Save to PostgreSQL database
        boolean success = timetableDAO.saveTimetable(sem, sec, year, new ArrayList<>(timetableData));
        
        if (success) {
            isEditMode = false;
            timetableTable.setEditable(false);
            setColumnsEditable(false);
            editButton.setText("EDIT");
            showAlert(Alert.AlertType.INFORMATION, "Success", "Timetable saved to database for Semester " + sem + " - Section " + sec);
        } else {
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to save timetable to database!");
        }
    }
    
    @FXML
    private void handleEdit() {
        String sem = semesterCombo.getValue();
        String sec = sectionCombo.getValue();
        String year = academicYearComboBox.getValue();
        
        if (sem == null || sec == null || year == null) {
            showAlert(Alert.AlertType.WARNING, "Warning", "Please select Semester, Section and Academic Year first!");
            return;
        }
        
        if (timetableData.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Warning", "Please view a timetable first before editing!");
            return;
        }
        
        isEditMode = !isEditMode;
        
        if (isEditMode) {
            timetableTable.setEditable(true);
            setColumnsEditable(true);
            editButton.setText("DONE");
            showAlert(Alert.AlertType.INFORMATION, "Edit Mode", "Click on any cell to edit. Click DONE when finished.");
        } else {
            timetableTable.setEditable(false);
            setColumnsEditable(false);
            editButton.setText("EDIT");
        }
    }
    
    private void setColumnsEditable(boolean editable) {
    if (editable) {
        // Set editable cell factories
        p1Column.setCellFactory(TextFieldTableCell.forTableColumn());
        p2Column.setCellFactory(TextFieldTableCell.forTableColumn());
        p3Column.setCellFactory(TextFieldTableCell.forTableColumn());
        p4Column.setCellFactory(TextFieldTableCell.forTableColumn());
        p5Column.setCellFactory(TextFieldTableCell.forTableColumn());
        p6Column.setCellFactory(TextFieldTableCell.forTableColumn());
        p7Column.setCellFactory(TextFieldTableCell.forTableColumn());
        
        // Set edit commit handlers
        p1Column.setOnEditCommit(e -> e.getRowValue().setP1(e.getNewValue()));
        p2Column.setOnEditCommit(e -> e.getRowValue().setP2(e.getNewValue()));
        p3Column.setOnEditCommit(e -> e.getRowValue().setP3(e.getNewValue()));
        p4Column.setOnEditCommit(e -> e.getRowValue().setP4(e.getNewValue()));
        p5Column.setOnEditCommit(e -> e.getRowValue().setP5(e.getNewValue()));
        p6Column.setOnEditCommit(e -> e.getRowValue().setP6(e.getNewValue()));
        p7Column.setOnEditCommit(e -> e.getRowValue().setP7(e.getNewValue()));
    } else {
        // Restore default cell factory instead of null
        p1Column.setCellFactory(column -> new javafx.scene.control.cell.TextFieldTableCell<>());
        p2Column.setCellFactory(column -> new javafx.scene.control.cell.TextFieldTableCell<>());
        p3Column.setCellFactory(column -> new javafx.scene.control.cell.TextFieldTableCell<>());
        p4Column.setCellFactory(column -> new javafx.scene.control.cell.TextFieldTableCell<>());
        p5Column.setCellFactory(column -> new javafx.scene.control.cell.TextFieldTableCell<>());
        p6Column.setCellFactory(column -> new javafx.scene.control.cell.TextFieldTableCell<>());
        p7Column.setCellFactory(column -> new javafx.scene.control.cell.TextFieldTableCell<>());
        
        // Remove edit commit handlers
        p1Column.setOnEditCommit(null);
        p2Column.setOnEditCommit(null);
        p3Column.setOnEditCommit(null);
        p4Column.setOnEditCommit(null);
        p5Column.setOnEditCommit(null);
        p6Column.setOnEditCommit(null);
        p7Column.setOnEditCommit(null);
    }
}
   /* 
    @FXML
    private void handleLogout() throws IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Logout");
        alert.setHeaderText("Confirm Logout");
        alert.setContentText("Are you sure you want to logout?");
        
        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                try {
                    navigatetoLogin();
                    
                } catch (IOException ex) {
                    Logger.getLogger(TimetableController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        
    }
    */
    
    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
    
    // Data Model Class
    public static class TimetableRow {
        private String day;
        private String p1;
        private String p2;
        private String p3;
        private String p4;
        private String p5;
        private String p6;
        private String p7;
        
        public TimetableRow(String day, String p1, String p2, String p3, 
                           String p4, String p5, String p6, String p7) {
            this.day = day;
            this.p1 = p1;
            this.p2 = p2;
            this.p3 = p3;
            this.p4 = p4;
            this.p5 = p5;
            this.p6 = p6;
            this.p7 = p7;
        }
        
        public String getDay() { return day; }
        public void setDay(String day) { this.day = day; }
        
        public String getP1() { return p1; }
        public void setP1(String p1) { this.p1 = p1; }
        
        public String getP2() { return p2; }
        public void setP2(String p2) { this.p2 = p2; }
        
        public String getP3() { return p3; }
        public void setP3(String p3) { this.p3 = p3; }
        
        public String getP4() { return p4; }
        public void setP4(String p4) { this.p4 = p4; }
        
        public String getP5() { return p5; }
        public void setP5(String p5) { this.p5 = p5; }
        
        public String getP6() { return p6; }
        public void setP6(String p6) { this.p6 = p6; }
        
        public String getP7() { return p7; }
        public void setP7(String p7) { this.p7 = p7; }
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