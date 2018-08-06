package com.dave.chan;

import javax.swing.*;

/**
 * Program Name: MVCLibrarySystemDriver.java
 * Purpose: this is the driver class that gets everything up and running. yeet
 *
 * Coder: Evan Burgess & David Harris
 * Date: August, 4th, 2018
 */

public class MVCLibrarySystemDriver
{
	//Set styling to windows look and feel
    private static void setLookAndFeel() {
    	
        try	{

            UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");

        } catch (Exception e) {

            System.out.println(e.getMessage());

        }

    }

    /**
     * Entry point for the program
     * @param args
     */
    public static void main(String[] args)
    {
        // First, create objects of the view class and the model class
        LibrarySystemView theView = null;
        LibrarySystemModel theModel = null;
        
        try {
            setLookAndFeel();
            theView = new LibrarySystemView();
            theModel = new LibrarySystemModel();

            //Create an object of the Controller class and pass it the view object and the model object
            LibrarySystemController theController = new LibrarySystemController(theView, theModel);
        }catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }

}
