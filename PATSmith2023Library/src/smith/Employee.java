/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package smith;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author caili
 */
public class Employee
{

    private String name;
    private String password;
    private int id;
    Connect connect = new Connect();
    /**
     * Receives employee's ID and password as parameters. Instantiates
     * @param i
     * @param p 
     */
    Employee(int i, String p)
    {
        id = i;
        password = p;
    }
    /**
     * Checks Employee database if employee's account exists. 
     * Returns Boolean: true if account is found
     * @return 
     */
    public boolean userInfo()
    {
        boolean valid = false;
        try
        {
            String sql = "SELECT * FROM Employees "
                    + "WHERE EmployeeID = "
                    + id + " AND EmpPassword = '" + password + "';";
            ResultSet rs = connect.query(sql);
            while (rs.next())
            {
                valid = true; 
                name = rs.getString("EmpName");
            }
        } catch (SQLException ex)
        {
            Logger.getLogger(Library.class.getName()).log(Level.SEVERE, null, ex);
        }
        return valid;
    }
    /**
     * Receives employee's new password as a String parameter 
     * Updates employee's password with parameter
     * @param newPas 
     */
    public void updatePassword(String newPas)
    {
        String sql = "UPDATE Employees SET EmpPassword = '" + newPas + "' WHERE EmployeeID = '"
                + id + "' AND EmpPassword = '" + password + "';";
        int rs = connect.update(sql);
    }
    public String getName()
    {
        return name;
    }
    /**
     * Deletes employee's account from Employees table
     */
    public void deleteAccount()
    {
        String sql = "DELETE * FROM Employees "
                + "WHERE EmployeeID = "
                + id + ";";
        int rs = connect.update(sql);
    }    
}
