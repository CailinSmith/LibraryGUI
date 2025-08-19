package smith;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Connect
{

    private Connection conn;

    public Connect()
    {
        try
        {
            Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");
            String filename = (new File("Library.accdb")).getAbsolutePath();
            conn = DriverManager.getConnection("jdbc:ucanaccess://" + filename);
//            System.out.println("Connection successful");
        } catch (Exception e)
        {
            System.out.println("ERROR: " + e);
            e.printStackTrace();
        }
    }

    public ResultSet query(String sql)
    {
        ResultSet rs = null;
        try
        {
            PreparedStatement statement = conn.prepareStatement(sql);
            rs = statement.executeQuery();
        } catch (SQLException ex)
        {
            Logger.getLogger(Connect.class.getName()).log(Level.SEVERE, null, ex);
        }
        return rs;
    }

    public int update(String sql)
    {
        int result = 0;
        try
        {
            PreparedStatement statement = conn.prepareStatement(sql);
            result = statement.executeUpdate();
        } catch (SQLException ex)
        {
            Logger.getLogger(Connect.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

}
