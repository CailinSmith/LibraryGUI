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

/**
 *
 * @author caili
 */
public class Books
{

    private String author;
    private String title;
    private String year;
    private String isbn;
    private String genre;
    private String output;
    Connect connect = new Connect();

    /**
     * Receives book details as String and instantiates them
     *
     * @param a
     * @param t
     * @param y
     * @param i
     */
    Books(String a, String t, String y, String i)
    {
        author = a;
        title = t;
        year = y;
        isbn = i;
    }

    /**
     * Receives genre as String and instantiates it
     *
     * @param g
     */
    Books(String g)
    {
        genre = g;
    }

    public String bookDetails()
    {
        return title + " by " + author;
    }

    /**
     * Searches through Books table for matches to sql select query 
     * Returns Boolean: true if book is found
     *
     * @return
     */
    public boolean bookInfo()
    {
        // Runs through every possible combination of input from user
        String sql = "SELECT * FROM Books WHERE ";
        boolean valid = false;
        if (author.equals("") && title.equals("") && year.equals(""))
            sql += "ISBN = " + isbn + ";";
        else if (author.equals("") && title.equals("") && isbn.equals(""))
            sql += "YEAR(YearPublished) = " + year + ";";
        else if (author.equals("") && year.equals("") && isbn.equals(""))
            sql += "Title = '" + title + "';";
        else if (title.equals("") && year.equals("") && isbn.equals(""))
            sql += "Author = '" + author + "';";
        else if (author.equals("") && title.equals(""))
            sql += "ISBN = " + isbn + " AND YEAR(YearPublished) = " + year + ";";
        else if (author.equals("") && year.equals(""))
            sql += "ISBN = " + isbn + " AND Title = '" + title + "';";
        else if (author.equals("") && isbn.equals(""))
            sql += "ISBN = " + isbn + " AND YEAR(YearPublished) = " + year + ";";
        else if (title.equals("") && year.equals(""))
            sql += "ISBN = " + isbn + " AND Author = '" + author + "';";
        else if (title.equals("") && isbn.equals(""))
            sql += "YEAR(YearPublished) = " + year + " AND Author = '" + author + "';";
        else if (year.equals("") && isbn.equals(""))
            sql += "Title = '" + title + "' AND Author = '" + author + "';";
        else if (author.equals(""))
            sql += "ISBN = " + isbn + " AND YEAR(YearPublished) = " + year + " AND Title = '" + title + "';";
        else if (title.equals(""))
            sql += "ISBN = " + isbn + " AND YEAR(YearPublished) = " + year + " AND Author = '" + author + "';";
        else if (year.equals(""))
            sql += "Title = '" + title + "' AND Author = '" + author + "' AND ISBN = " + isbn + ";";
        else if (isbn.equals(""))
            sql += "Title = '" + title + "' AND Author = '" + author + "' AND YEAR(YearPublished) = " + year + ";";
        else
            sql += "Title = '" + title + "' AND Author = '" + author + "' AND YEAR(YearPublished) = " + year + " AND ISBN = " + isbn + ";";

        try
        {
            output = "ISBN:\t\tTitle:\t\tAuthor:\n";
            ResultSet rs = connect.query(sql);
            while (rs.next())
            {
                // Sets title and author from result
                title = rs.getString("Title");
                author = rs.getString("Author");
                valid = true;
                if (title.length() < 14) // formatting output
                    output += rs.getBigDecimal("ISBN") + "\t" + title + "\t\t" + author + "\n";
                else
                    output += rs.getBigDecimal("ISBN") + "\t" + rs.getString("Title") + "\t" + rs.getString("Author") + "\n";
            }

        } catch (SQLException ex)
        {
            Logger.getLogger(Library.class.getName()).log(Level.SEVERE, null, ex);
        }
        return valid;
    }

    /**
     * Returns String output from search query
     *
     * @return
     */
    public String getOutput()
    {
        return output;
    }

    /**
     * Removes book from database
     */
    public void deleteBook()
    {
        String sql = "DELETE * FROM Books "
                + "WHERE ISBN = "
                + isbn + ";";
        int rs = connect.update(sql);
        sql = "DELETE * FROM MemberBook WHERE ISBN = " + isbn + ";";
        rs = connect.update(sql);
    }

    /**
     * Receives genre as a String parameter and inserts book into Book database
     *
     * @param genre
     */
    public void insertBook(String genre)
    {
        // Converts year into LocalDate so it can be inserted in sql query
        LocalDate date = LocalDate.of(Integer.parseInt(year), 1, 1);

        String sql = "INSERT INTO Books (ISBN, Title, Author, YearPublished, Genre) "
                + "VALUES (" + isbn + ",'"
                + title + "','" + author + "',#"
                + date + "#, '" + genre + "');";
        int rs = connect.update(sql);
    }

    /**
     * Uses ISBN to search for book in Books table 
     * Returns Boolean: true if book is found
     *
     * @return
     */
    public boolean checkISBN()
    {
        /**
         * Validation: Check if the ISBN number already exists
         */
        boolean valid = false;
        if (!isbn.equals(""))
        {
            String sql = "SELECT * FROM Books WHERE ISBN = " + isbn + ";";
            ResultSet rs = connect.query(sql);
            try
            {
                while (rs.next())
                {
                    valid = true;
                }
            } catch (SQLException ex)
            {
                Logger.getLogger(Books.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return valid;
    }

    /**
     * Receives String input as parameter and checks if input contains any
     * letters or special characters Returns Boolean: true if the input only
     * contains digits = valid
     *
     * @param input
     * @return
     */
    public boolean validate(String input)
    {
        boolean valid = true;
        if (input.length() != 0)
        {
            int i = 0;
            do
            {
                if (!Character.isDigit(input.charAt(i)))
                    valid = false;
                i++;
            } while (i < input.length() && valid == true);
        }
        return valid;
    }

    /**
     * Use genre to search for books that match 
     * Return String: results of search query
     *
     * @return
     */
    public String searchGenre()
    {
        String searchResults = "ISBN:\t\tTitle:\t\tAuthor:\n";
        String sql = "SELECT * FROM Books WHERE Genre = '" + genre + "'";
        try
        {
            ResultSet rs = connect.query(sql);
            while (rs.next())
            {
                if (rs.getString("Title").length() <= 15) // formatting output
                    searchResults += rs.getBigDecimal("ISBN") + "\t" + rs.getString("Title") + "\t\t" + rs.getString("Author") + "\n";
                else
                    searchResults += rs.getBigDecimal("ISBN") + "\t" + rs.getString("Title") + "\t" + rs.getString("Author") + "\n";
            }

        } catch (SQLException ex)
        {
            Logger.getLogger(Library.class.getName()).log(Level.SEVERE, null, ex);
        }
        return searchResults;
    }
}
