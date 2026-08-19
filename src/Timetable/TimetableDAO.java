
package Timetable;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import SignUp.DBConnection;
import Timetable.TimetableController.TimetableRow;
public class TimetableDAO {
    // Fetch timetable by semester, section, and academic year
    public List<TimetableRow> getTimetable(String semester, String section, String academicYear) {
        List<TimetableRow> timetable = new ArrayList<>();
        String query = "SELECT day, p1, p2, p3, p4, p5, p6, p7 FROM timetable " +
                      "WHERE semester = ? AND section = ? AND academic_year = ? " +
                      "ORDER BY CASE day " +
                      "WHEN 'Monday' THEN 1 WHEN 'Tuesday' THEN 2 WHEN 'Wednesday' THEN 3 " +
                      "WHEN 'Thursday' THEN 4 WHEN 'Friday' THEN 5 WHEN 'Saturday' THEN 6 END";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setString(1, semester);
            pstmt.setString(2, section);
            pstmt.setString(3, academicYear);
            
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                TimetableRow row = new TimetableRow(
                    rs.getString("day"),
                    rs.getString("p1"),
                    rs.getString("p2"),
                    rs.getString("p3"),
                    rs.getString("p4"),
                    rs.getString("p5"),
                    rs.getString("p6"),
                    rs.getString("p7")
                );
                timetable.add(row);
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return timetable;
    }
    // Save or update timetable
    public boolean saveTimetable(String semester, String section, String academicYear, List<TimetableRow> timetable) {
        String deleteQuery = "DELETE FROM timetable WHERE semester = ? AND section = ? AND academic_year = ?";
        String insertQuery = "INSERT INTO timetable (semester, section, academic_year, day, p1, p2, p3, p4, p5, p6, p7) " +
                            "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DBConnection.getConnection()) {
            conn.setAutoCommit(false);
            
            // Delete existing records
            try (PreparedStatement deleteStmt = conn.prepareStatement(deleteQuery)) {
                deleteStmt.setString(1, semester);
                deleteStmt.setString(2, section);
                deleteStmt.setString(3, academicYear);
                deleteStmt.executeUpdate();
            }
            
            // Insert new records
            try (PreparedStatement insertStmt = conn.prepareStatement(insertQuery)) {
                for (TimetableRow row : timetable) {
                    insertStmt.setString(1, semester);
                    insertStmt.setString(2, section);
                    insertStmt.setString(3, academicYear);
                    insertStmt.setString(4, row.getDay());
                    insertStmt.setString(5, row.getP1());
                    insertStmt.setString(6, row.getP2());
                    insertStmt.setString(7, row.getP3());
                    insertStmt.setString(8, row.getP4());
                    insertStmt.setString(9, row.getP5());
                    insertStmt.setString(10, row.getP6());
                    insertStmt.setString(11, row.getP7());
                    insertStmt.addBatch();
                }
                insertStmt.executeBatch();
            }
            
            conn.commit();
            return true;
            
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    // Check if timetable exists
    public boolean timetableExists(String semester, String section, String academicYear) {
        String query = "SELECT COUNT(*) FROM timetable WHERE semester = ? AND section = ? AND academic_year = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setString(1, semester);
            pstmt.setString(2, section);
            pstmt.setString(3, academicYear);
            
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return false;
    }
}
