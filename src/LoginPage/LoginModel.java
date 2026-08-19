
package LoginPage;


public class LoginModel {
    private String emailAddress;
    private String PassWord;
        public LoginModel() {
    }

    public LoginModel(String emailAddress, String PassWord) {
        this.emailAddress = emailAddress;
        this.PassWord = PassWord;
    }
    public String getPassWord() {
        return PassWord;
    }

    public void setPassWord(String PassWord) {
        this.PassWord = PassWord;
    }

    public String getemailAddress() {
        return emailAddress;
    }

    public void setemailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }
}
