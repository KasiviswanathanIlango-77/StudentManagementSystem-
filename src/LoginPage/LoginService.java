
package LoginPage;

import java.sql.SQLException;


public class LoginService 
{
    public int  login(LoginModel lm) throws ClassNotFoundException, SQLException
    {
        if(lm.getemailAddress().isEmpty() || lm.getPassWord().isEmpty())
        {
            return -2;
        }
        
            LoginDAO ld = new LoginDAO();
             return ld.userLogin(lm);
                                        
    }
}
