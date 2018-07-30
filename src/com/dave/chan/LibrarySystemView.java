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
	JTable usersTable;
	JScrollPane userTableScrollPane;

    public LibrarySystemView()
    {
        //boilerplate
        super("Library System");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLayout(new BorderLayout() );//ANONYMOUS layout object
        this.setSize(500,500);
        this.setLocationRelativeTo(null);

        basePanel = new JPanel(new FlowLayout());
        this.add(basePanel, BorderLayout.CENTER);

        libraryTabbedPane = new JTabbedPane();
        basePanel.add(libraryTabbedPane);

        //Users Section
        usersPanel = new JPanel();

        //user table stuff
        String[] userColNames = {"Last Name", "First Name", "E-mail"};
        //dummy data

		   Object[][] dummyArray = {
				{"Pulling", "Bill","CPA", "bpulling@gmail.com"}
		};	
		
    	usersTable = new JTable(dummyArray, userColNames);
    	userTableScrollPane = new JScrollPane(usersTable);
    	usersPanel.add(userTableScrollPane);
      
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
        this.setVisible(true);

    }//end constructor

}//end class
