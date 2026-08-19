package LoginPage;

import smsjavafxapplication.Session;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.*;
import SignUp.SignupModel;
public class LoginDAO 
{
    public Connection dbConnection() throws ClassNotFoundException, SQLException
    {  
        Class.forName("org.postgresql.Driver");

        String url = "jdbc:postgresql://localhost:5432/SMS";
        String userName = "postgres";
        String pass = "Anbukasi@97";
        
        Connection con = DriverManager.getConnection(url, userName, pass);
       
       return con;
    }
    
   public int userLogin(LoginModel lm)
        throws ClassNotFoundException, SQLException {

    Connection conn = dbConnection();

    String emailQuery =
    "SELECT * FROM signup "
    + "WHERE emailaddress=?";

    PreparedStatement ps =
            conn.prepareStatement(emailQuery);

    ps.setString(1,
            lm.getemailAddress());

    ResultSet rs =
            ps.executeQuery();

    if(!rs.next()) {

        // Email not found
        return -1;
    }
    
    String dbPassword =
            rs.getString("password");

    if(dbPassword.equals(
            lm.getPassWord())) {

        Session.emailAddress =
                rs.getString("emailaddress");

        Session.fullName =
                rs.getString("fullname");

        Session.role =
                rs.getString("role");

        return 1;
    }

    // Wrong password
    return 0;
}
}
