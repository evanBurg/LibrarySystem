package com.dave.chan;

/**
 * Program Name: LibrarySystemView.java
 * Purpose: A GUI that the user will interact with in order to operate our library system. The View section of the
 * 			MVC architecture.
 * Coder: David Harrs & Evan Burgess
 * Date: July 30, 2018
 */

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class LibrarySystemView extends JFrame
{
	//class-wide
	JTabbedPane libraryTabbedPane;
	JFrame libraryFrame;
	JPanel usersPanel, booksPanel, loansPanel, retrievalPanel;
    public LibrarySystemView()
    {
        //boilerplate
        super("Library System");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLayout(new FlowLayout());
        this.setSize(500,500);
        this.setLocationRelativeTo(null);
        
        libraryFrame = new JFrame();
        libraryTabbedPane = new JTabbedPane();
        libraryFrame.add(libraryTabbedPane);
        
        //Users Section
        usersPanel = new JPanel();
        
        libraryTabbedPane.add("Users", usersPanel);
        
        
        //Books Section
        booksPanel = new JPanel();
        
        libraryTabbedPane.addTab("Books", booksPanel);
        
        //Loans Section
        loansPanel = new JPanel();
        
        libraryTabbedPane.addTab("Loans", loansPanel);
        
        //Retrieval Section
        retrievalPanel = new JPanel();
        
        libraryTabbedPane.addTab("Retrieval", retrievalPanel);
        
        //display it
        libraryFrame.setVisible(true);
        
    }//end constructor
    
    
    
    public static void main (String[] args) {
    	new LibrarySystemView();
    }

}//end class
