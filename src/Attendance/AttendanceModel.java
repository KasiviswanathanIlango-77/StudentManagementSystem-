
package Attendance;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class AttendanceModel {

    private final SimpleIntegerProperty studentId;
    private final SimpleStringProperty rollNumber;
    private final SimpleStringProperty studentName;
    private final SimpleBooleanProperty p1;
    private final SimpleBooleanProperty p2;
    private final SimpleBooleanProperty p3;
    private final SimpleBooleanProperty p4;
    private final SimpleBooleanProperty p5;
    private final SimpleBooleanProperty p6;
    private final SimpleBooleanProperty p7;

    public AttendanceModel() {
        this.studentId = new SimpleIntegerProperty();
        this.rollNumber = new SimpleStringProperty();
        this.studentName = new SimpleStringProperty();
        this.p1 = new SimpleBooleanProperty(true);
        this.p2 = new SimpleBooleanProperty(true);
        this.p3 = new SimpleBooleanProperty(true);
        this.p4 = new SimpleBooleanProperty(true);
        this.p5 = new SimpleBooleanProperty(true);
        this.p6 = new SimpleBooleanProperty(true);
        this.p7 = new SimpleBooleanProperty(true);
    }

    public AttendanceModel(int studentId, String rollNumber, String studentName) {
        this.studentId = new SimpleIntegerProperty(studentId);
        this.rollNumber = new SimpleStringProperty(rollNumber);
        this.studentName = new SimpleStringProperty(studentName);
        this.p1 = new SimpleBooleanProperty(true);
        this.p2 = new SimpleBooleanProperty(true);
        this.p3 = new SimpleBooleanProperty(true);
        this.p4 = new SimpleBooleanProperty(true);
        this.p5 = new SimpleBooleanProperty(true);
        this.p6 = new SimpleBooleanProperty(true);
        this.p7 = new SimpleBooleanProperty(true);
    }

    // Getters and Setters
    public int getStudentId() { return studentId.get(); }
    public void setStudentId(int value) { studentId.set(value); }
    public SimpleIntegerProperty studentIdProperty() { return studentId; }

    public String getRollNumber() { return rollNumber.get(); }
    public void setRollNumber(String value) { rollNumber.set(value); }
    public SimpleStringProperty rollNumberProperty() { return rollNumber; }

    public String getStudentName() { return studentName.get(); }
    public void setStudentName(String value) { studentName.set(value); }
    public SimpleStringProperty studentNameProperty() { return studentName; }

    public boolean isP1() { return p1.get(); }
    public void setP1(boolean value) { p1.set(value); }
    public SimpleBooleanProperty p1Property() { return p1; }

    public boolean isP2() { return p2.get(); }
    public void setP2(boolean value) { p2.set(value); }
    public SimpleBooleanProperty p2Property() { return p2; }

    public boolean isP3() { return p3.get(); }
    public void setP3(boolean value) { p3.set(value); }
    public SimpleBooleanProperty p3Property() { return p3; }

    public boolean isP4() { return p4.get(); }
    public void setP4(boolean value) { p4.set(value); }
    public SimpleBooleanProperty p4Property() { return p4; }

    public boolean isP5() { return p5.get(); }
    public void setP5(boolean value) { p5.set(value); }
    public SimpleBooleanProperty p5Property() { return p5; }

    public boolean isP6() { return p6.get(); }
    public void setP6(boolean value) { p6.set(value); }
    public SimpleBooleanProperty p6Property() { return p6; }

    public boolean isP7() { return p7.get(); }
    public void setP7(boolean value) { p7.set(value); }
    public SimpleBooleanProperty p7Property() { return p7; }

    // Count present periods for this student
    public int getPresentCount() {
        int count = 0;
        if (isP1()) count++;
        if (isP2()) count++;
        if (isP3()) count++;
        if (isP4()) count++;
        if (isP5()) count++;
        if (isP6()) count++;
        if (isP7()) count++;
        return count;
    }

    // Check if student is fully present (all periods)
    public boolean isFullyPresent() {
        return isP1() && isP2() && isP3() && isP4() && isP5() && isP6() && isP7();
    }

    // Check if student is fully absent (no periods)
    public boolean isFullyAbsent() {
        return !isP1() && !isP2() && !isP3() && !isP4() && !isP5() && !isP6() && !isP7();
    }
}