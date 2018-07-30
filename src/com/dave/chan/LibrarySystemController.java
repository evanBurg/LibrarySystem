package com.dave.chan;

/**
 * Program Name: LibrarySystemController.java
 * Purpose: this class acts as the controller between the GUI view and the back
 *         end model that stores the data. It will gather the user input from
 *         the view object, send the data to the model object for calculation, and then
 *         take the returned result from the model and send it back to the view object
 *         to display the result.
 * Coder: Bill Pulling for Sec02,ADAPTED from Derek Carnas' online Youtube video
 *        on MVC.
 * Date: Jul 12, 2016
 */
import java.awt.event.*;

public class LibrarySystemController
{
    //CLASS DATA MEMBERS are objects of the model and view classes
    private LibrarySystemView theView;
    private LibrarySystemModel theModel;

    //constructor will take as arguments objects of the View and the Model
    public LibrarySystemController(LibrarySystemView view, LibrarySystemModel model)
    {
        this.theView = view;
        this.theModel = model;
    }//end constructor

    //PUT INNER CLASS HERE
    private class BookListener implements ActionListener
    {
        @Override
        public void actionPerformed(ActionEvent e)
        {

        }

    }//end inner class

}//end class
