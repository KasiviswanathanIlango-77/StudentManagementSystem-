
package SignUp;


public class SignupModel {
    private String fullname;
    private String emailaddress;
    private String mobile_number;
    private String role;
    private String password;
    private String confirmpassword;

    public SignupModel() {
    }

    public SignupModel(String fullname, String emailaddress, String mobile_number, String role, String password, String confirmpassword) {
        this.fullname = fullname;
        this.emailaddress = emailaddress;
        this.mobile_number = mobile_number;
        this.role = role;
        this.password = password;
        this.confirmpassword = confirmpassword;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getEmailaddress() {
        return emailaddress;
    }

    public void setEmailaddress(String emailaddress) {
        this.emailaddress = emailaddress;
    }

    public String getMobile_number() {
        return mobile_number;
    }

    public void setMobile_number(String mobile_number) {
        this.mobile_number = mobile_number;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getConfirmpassword() {
        return confirmpassword;
    }

    public void setConfirmpassword(String confirmpassword) {
        this.confirmpassword = confirmpassword;
    }
    
}
