/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package smith;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 *
 * @author caili
 */
public class Help
{

    String[] arr = new String[22];
    int size = 0;

    /**
     * Extracts the HELP messages from the text file and saves them to a String
     * array
     */
    Help()
    {
        try
        {
            Scanner scFile = new Scanner(new File("Help.txt")).useDelimiter("/");

            while (scFile.hasNext())
            {
                arr[size] = scFile.next();
                size++;
            }
            scFile.close();
        } catch (FileNotFoundException ex)
        {
            System.out.println("Cannot find file");
        }
    }

    /**
     * Receives an int as a parameter with the position in the array Returns the
     * String in the array containing the HELP message
     *
     * @param i
     * @return
     */
    public String getInfo(int i)
    {
        return arr[i];
    }
}
