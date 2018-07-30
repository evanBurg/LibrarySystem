package com.dave.chan;

/**
 * Program Name: LibrarySystemView.java
 * Purpose: shows use of the MVC architecture. This class will represent
 *          the VIEW portion, or what the user actually sees. It will do
 *          double duty in that it will act as BOTH the input GUI and the
 *          display GUI.
 * Coder: Bill Pulling for Sec02, ADAPTED from Derek Carnas' online Youtube video
 *        on MVC.
 * Date: Jul 12, 2016
 */
//three wise men
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class LibrarySystemView extends JFrame
{
    //CLASS WIDE SCOPE AREA

    //constructor
    public LibrarySystemView()
    {
        super("Library");
        //boilerplate
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLayout(new FlowLayout() );
        this.setSize(500,200);
        this.setLocationRelativeTo(null);
    }//end constructor

}//end class
