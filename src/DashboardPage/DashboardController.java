package DashboardPage;

import AdmissionPage.DBConnection;
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
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class DashboardController implements Initializable {

    @FXML
    private AnchorPane chartContainer;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        createAndLoadAdmissionChart();
    }

    private void createAndLoadAdmissionChart() {
        // Create axes
        CategoryAxis yearAxis = new CategoryAxis();
        yearAxis.setLabel("Year");

        NumberAxis studentCountAxis = new NumberAxis();
        studentCountAxis.setLabel("Total Students");

        // Create BarChart programmatically (avoids FXML instantiation issue)
        BarChart<String, Number> admissionBarChart = new BarChart<>(yearAxis, studentCountAxis);
        admissionBarChart.setTitle("Total Students Admitted (2020-2025)");
        admissionBarChart.setPrefHeight(320.0);
        admissionBarChart.setPrefWidth(700.0);
        admissionBarChart.setStyle("-fx-background-color: white; -fx-background-radius: 8;");
        admissionBarChart.setLegendVisible(false);

        // Create data series
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Students Admitted");

        // Load data from database
        boolean dbSuccess = loadDataFromDatabase(series);

        // If DB fails, use sample data
        if (!dbSuccess) {
            series.getData().add(new XYChart.Data<>("2020", 45));
            series.getData().add(new XYChart.Data<>("2021", 52));
            series.getData().add(new XYChart.Data<>("2022", 48));
            series.getData().add(new XYChart.Data<>("2023", 60));
            series.getData().add(new XYChart.Data<>("2024", 55));
            series.getData().add(new XYChart.Data<>("2025", 42));
        }

        admissionBarChart.getData().add(series);

        // Style the bars
        admissionBarChart.setCategoryGap(20);
        for (XYChart.Data<String, Number> data : series.getData()) {
            if (data.getNode() != null) {
                data.getNode().setStyle("-fx-bar-fill: #1565C0;");
            }
        }

        // Add chart to container
        AnchorPane.setTopAnchor(admissionBarChart, 0.0);
        AnchorPane.setBottomAnchor(admissionBarChart, 0.0);
        AnchorPane.setLeftAnchor(admissionBarChart, 0.0);
        AnchorPane.setRightAnchor(admissionBarChart, 0.0);
        chartContainer.getChildren().add(admissionBarChart);
    }

    private boolean loadDataFromDatabase(XYChart.Series<String, Number> series) {
        String sql = """
            SELECT year_passed, COUNT(*) as total_students
            FROM admission
            WHERE year_passed BETWEEN 2020 AND 2025
            GROUP BY year_passed
            ORDER BY year_passed
            """;

        try (Connection con = DBConnection.getConnection();
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            boolean hasData = false;
            while (rs.next()) {
                hasData = true;
                int year = rs.getInt("year_passed");
                int count = rs.getInt("total_students");
                series.getData().add(new XYChart.Data<>(String.valueOf(year), count));
            }
            return hasData;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("Dashboard.fxml"));
        Scene scene = new Scene(root);
        primaryStage.setMinWidth(1024);
        primaryStage.setMinHeight(600);
        primaryStage.setMaximized(true);
        primaryStage.setTitle("Student Management System - Dashboard");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    @FXML
    private void admissionButton(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/AdmissionPage/Admission.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }

    @FXML
    private void timetableButton(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/Timetable/Timetable.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }

    @FXML
    private void attendanceButton(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/Attendance/Attendance.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }

    @FXML
    private void internalmarksButton(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/InternalMarks/InternalMarks.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }

    @FXML
    private void recordsButton(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/Records/StudentRecords.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }

    @FXML
    private void navigatetoLogin(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/LoginPage/Login.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }
}