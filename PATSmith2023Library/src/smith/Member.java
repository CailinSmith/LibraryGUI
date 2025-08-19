/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package smith;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 *
 * @author caili
 */
public class Member
{

    private String username;
    private String password;
    private int id;

    Connect connect = new Connect();

    /**
     * Receives and instantiates username and password input
     *
     * @param u
     * @param p
     */
    Member(String u, String p)
    {
        username = u;
        password = p;
    }

    /**
     * Receives and instantiates username and ID input
     *
     * @param u
     * @param i
     */
    Member(String u, int i)
    {
        username = u;
        id = i;
    }

    /**
     * Sends a sql select query to database Returns Boolean: true if member's
     * information exists in database
     *
     * @return
     */
    public boolean userInfo()
    {
        boolean valid = false;
        try
        {
            String sql = "SELECT * FROM Members "
                    + "WHERE MemUsername = '"
                    + username + "' AND MemPassword = '" + password + "';";
            ResultSet rs = connect.query(sql);
            while (rs.next())
            {
                // If account if found, then the input is valid
                valid = true;
                id = rs.getInt("MemberID");
            }
        } catch (SQLException ex)
        {
            Logger.getLogger(Library.class.getName()).log(Level.SEVERE, null, ex);
        }
        return valid;
    }

    /**
     * Sends sql select query to database Returns Boolean: true if member's ID
     * found in database
     *
     * @return
     */
    public boolean userID()
    {
        boolean valid = false;
        try
        {
            String sql = "SELECT * FROM Members "
                    + "WHERE MemberID = "
                    + id;
            ResultSet rs = connect.query(sql);
            while (rs.next())
            {
                // If account if found, then the input is valid
                valid = true;
                // Set username as result
                username = rs.getString("MemUsername");
            }
        } catch (SQLException ex)
        {
            Logger.getLogger(Library.class.getName()).log(Level.SEVERE, null, ex);
        }
        return valid;
    }

    /**
     * Searches database for members that match the information inputted by
     * employee Returns String: member's information that matches sql select
     * query
     *
     * @return
     */
    public String accessMembers()
    {
        boolean valid = false;
        String output = "Username:\t";
        try
        {
            // Check if the member has a book reserved
            String sql = "SELECT * "
                    + "FROM Members, MemberBook, Books "
                    + "WHERE Books.ISBN = MemberBook.ISBN "
                    + "AND Members.MemberID = MemberBook.MemberID "
                    + "AND MemUsername = '" + username + "' "
                    + "AND Members.MemberID = " + id;
            ResultSet rs = connect.query(sql);
            while (rs.next())
            {
                // Returns member's information and book reserved
                valid = true;
                output += rs.getString("MemUsername") + "\n";
                output += "Password:\t" + rs.getString("MemPassword") + "\n";
                output += "Phone:\t" + rs.getString("Phone") + "\n";
                output += "Book:\t" + rs.getString("Title") + " by "
                        + rs.getString("Author");
            }
            if (!valid)
            {
                // If the member can not be found in the MemberBook database, 
                // checks the Members database
                sql = "SELECT * FROM Members WHERE MemUsername = '" + username
                        + "' AND MemberID = " + id;
                rs = connect.query(sql);
                while (rs.next())
                {
                    // Returns member's information
                    valid = true;
                    output += rs.getString("MemUsername") + "\n";
                    output += "Password:\t" + rs.getString("MemPassword") + "\n";
                    output += "Phone:\t" + rs.getString("Phone") + "\n";
                }

            }
        } catch (SQLException ex)
        {
            Logger.getLogger(Library.class.getName()).log(Level.SEVERE, null, ex);
        }
        // Error message if user not found in either table
        if (!valid)
            output = "User not found";

        return output;
    }

    /**
     * Receives phone number as String parameter Inserts member into Members
     * table
     *
     * @param num
     */
    public void insertAccount(String num)
    {
        String sql = "INSERT INTO Members (MemUsername, MemPassword, Phone)"
                + "VALUES ('" + username + "','"
                + password + "','" + num + "')";
        int rs = connect.update(sql);
    }

    /**
     * Inserts member into Members table
     */
    public void insertAccount()
    {
        String sql = "INSERT INTO Members (MemUsername, MemPassword)"
                + "VALUES ('" + username + "','"
                + password + "')";
        int rs = connect.update(sql);
    }

    /**
     * Receives phone number as String and updates member's Phone in Members
     * table
     *
     * @param num
     */
    public void updateDetails(String num)
    {
        String sql = "UPDATE Members SET Phone = '" + num + "' WHERE MemberID = " + id + " ;";
        int rs = connect.update(sql);
    }

    /**
     * Removes member from MemberBook database
     */
    public void returnBook()
    {
        String sql = "DELETE * FROM MemberBook "
                + "WHERE MemberID = " + id + ";";
        int rs = connect.update(sql);
    }

    /**
     * Searches database for member's current reserved book Returns String array
     * containing book's information
     *
     * @return
     */
    public String[] accessCurrentBook()
    {
        String[] results = new String[4];
        try
        {
            String sql = "SELECT * "
                    + "FROM Books, MemberBook "
                    + "WHERE MemberBook.ISBN = Books.ISBN "
                    + "AND MemberID = " + id + ";";
            ResultSet rs = connect.query(sql);
            while (rs.next())
            {
                results[0] = rs.getString("Author");
                results[1] = rs.getString("Title");
                int pos = rs.getString("ReservedDate").indexOf(" ");
                results[2] = rs.getString("ReservedDate").substring(0, pos);
                results[3] = rs.getString("ISBN");
            }
        } catch (SQLException ex)
        {
            Logger.getLogger(Library.class
                    .getName()).log(Level.SEVERE, null, ex);
        }

        return results;
    }

    public int getID()
    {
        return id;
    }

    public String getUsername()
    {
        return username;
    }

    /**
     * Receives int parameter Sets object's ID to parameter
     *
     * @param id
     */
    public void setID(int id)
    {
        this.id = id;
    }

    /**
     * Receives String as parameter and updates member's password to received
     * parameter
     *
     * @param newPassword
     */
    public void updatePassword(String newPassword)
    {
        String sql = "UPDATE Members SET EmpPassword = '" + newPassword + "' WHERE EmployeeID = "
                + id + " AND EmpPassword = '" + password + "';";
        int rs = connect.update(sql);
    }

    /**
     * Removes member's information from entire database
     */
    public void deleteAccount()
    {
        String sql = "DELETE * FROM Members "
                + "WHERE MemberID = "
                + id;
        int rs = connect.update(sql);
        sql = "DELETE * FROM MemberBook "
                + "WHERE MemberID = "
                + id;
        rs = connect.update(sql);
    }

    /**
     * Checks if member has a book reserved Returns Boolean: true if member does
     * have a book reserved
     *
     * @return
     */
    public boolean checkReserved()
    {
        boolean valid = false;
        try
        {
            String sql = "SELECT * "
                    + "FROM MemberBook "
                    + "WHERE MemberID = " + id + ";";
            ResultSet rs = connect.query(sql);
            while (rs.next())
            {
                valid = true;
            }
        } catch (SQLException ex)
        {
            Logger.getLogger(Library.class.getName()).log(Level.SEVERE, null, ex);
        }
        return valid;
    }

    /**
     * Receives String parameter - book's ISBN Inserts member and book's iISBN
     * into MemberBook database to reserve a book
     *
     * @param isbn
     */
    public void insertReserve(String isbn)
    {
        String sql = "INSERT INTO MemberBook (ISBN, MemberID, ReservedDate) "
                + "VALUES (" + isbn + "," + id + ",#" + LocalDate.now() + "#)";
        int rs = connect.update(sql);
    }

    /**
     * Receives input as a String Validates input by checking if it only
     * consists of digits Returns Boolean: true if input only contains digits
     *
     * @param input
     * @return
     */
    public boolean validateInput(String input)
    {
        boolean valid = true;
        int i = 0;
        do
        {
            if (!Character.isDigit(input.charAt(i)))
                valid = false;
            i++;
        } while (i < input.length() && valid == true);

        return valid;
    }
}
