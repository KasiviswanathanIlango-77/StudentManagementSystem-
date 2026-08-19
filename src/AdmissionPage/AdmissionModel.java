
package AdmissionPage;

import java.time.LocalDate;


public class AdmissionModel {
     private int studentId;
    private String rollNumber;
    private String firstName;
    private String lastName;
    private String gender;
    private LocalDate dob;
    private String community;

    private String fatherName;
    private String motherName;
    private String parentOccupation;

    private String studentContact;
    private String parentContact;

    private String address;

    private double tenthMark;
    private double twelfthMark;

    private String ugCollege;
    private double ugMark;
    private int yearPassed;

    // private String photoPath;

    public AdmissionModel() {
    }

    public AdmissionModel(int studentId, String rollNumber, String firstName, String lastName, 
                String gender, LocalDate dob, String community, String fatherName, String motherName, 
                String parentOccupation, String studentContact, String parentContact, String address, 
                double tenthMark, double twelfthMark, String ugCollege, double ugMark, int yearPassed, 
                String photoPath) {
        this.studentId = studentId;
        this.rollNumber = rollNumber;
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
        this.dob = dob;
        this.community = community;
        this.fatherName = fatherName;
        this.motherName = motherName;
        this.parentOccupation = parentOccupation;
        this.studentContact = studentContact;
        this.parentContact = parentContact;
        this.address = address;
        this.tenthMark = tenthMark;
        this.twelfthMark = twelfthMark;
        this.ugCollege = ugCollege;
        this.ugMark = ugMark;
        this.yearPassed = yearPassed;
       // this.photoPath = photoPath;
    }

    public int getStudentId() {
        return studentId;
    }

    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }

    public String getRollNumber() {
        return rollNumber;
    }

    public void setRollNumber(String rollNumber) {
        this.rollNumber = rollNumber;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public LocalDate getDob() {
        return dob;
    }

    public void setDob(LocalDate dob) {
        this.dob = dob;
    }

    public String getCommunity() {
        return community;
    }

    public void setCommunity(String community) {
        this.community = community;
    }

    public String getFatherName() {
        return fatherName;
    }

    public void setFatherName(String fatherName) {
        this.fatherName = fatherName;
    }

    public String getMotherName() {
        return motherName;
    }

    public void setMotherName(String motherName) {
        this.motherName = motherName;
    }

    public String getParentOccupation() {
        return parentOccupation;
    }

    public void setParentOccupation(String parentOccupation) {
        this.parentOccupation = parentOccupation;
    }

    public String getStudentContact() {
        return studentContact;
    }

    public void setStudentContact(String studentContact) {
        this.studentContact = studentContact;
    }

    public String getParentContact() {
        return parentContact;
    }

    public void setParentContact(String parentContact) {
        this.parentContact = parentContact;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public double getTenthMark() {
        return tenthMark;
    }

    public void setTenthMark(double tenthMark) {
        this.tenthMark = tenthMark;
    }

    public double getTwelfthMark() {
        return twelfthMark;
    }

    public void setTwelfthMark(double twelfthMark) {
        this.twelfthMark = twelfthMark;
    }

    public String getUgCollege() {
        return ugCollege;
    }

    public void setUgCollege(String ugCollege) {
        this.ugCollege = ugCollege;
    }

    public double getUgMark() {
        return ugMark;
    }

    public void setUgMark(double ugMark) {
        this.ugMark = ugMark;
    }

    public int getYearPassed() {
        return yearPassed;
    }

    public void setYearPassed(int yearPassed) {
        this.yearPassed = yearPassed;
    }
   /*
    public String getPhotoPath() {
        return photoPath;
    }

    public void setPhotoPath(String photoPath) {
        this.photoPath = photoPath;
    }
   */

}
