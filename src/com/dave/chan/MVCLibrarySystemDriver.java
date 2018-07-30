package com.dave.chan;

/**
 * Program Name: MVCLibrarySystemDriver.java
 * Purpose: this is the driver class that gets everything up and running.
 *
 * Coder: Bill Pulling for Sec02,ADAPTED from Derek Carnas' online Youtube video
 *        on MVC.
 * Date: Jul 12, 2016
 */

public class MVCLibrarySystemDriver
{

    public static void main(String[] args)
    {
        // First, create objects of the view class and the model class
        LibrarySystemView theView = new LibrarySystemView();
        LibrarySystemModel theModel = new LibrarySystemModel();

        //NOW, create an object of the Controller class and pass it the view object
        // and the model object
        LibrarySystemController theController = new LibrarySystemController(theView,theModel);
    }

}
