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
import javax.swing.SpringLayout;

public class LibrarySystemView extends JFrame
{
	//class-wide
	JTabbedPane libraryTabbedPane;
	JFrame libraryFrame;
	JPanel basePanel, usersPanel, usersButtonPanel, booksPanel, loansPanel, retrievalPanel, booksButtonPanel, bookFormPanel;
	JTable usersTable, booksListTable;
	JScrollPane userTableScrollPane;
	JButton usersSaveButton, usersUpdateButton, usersNewButton, booksAddBookButton;
	JLabel bookTitleLabel, bookEditionLabel, bookSubjectLabel, bookAuthorFNLabel, bookAuthorLNLabel;
	JTextField bookTitleTextField, bookEditionTextField, bookSubjectTextField, bookAuthorFNTextField, bookAuthorLNTextField;

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
        usersPanel = new JPanel(new BorderLayout());

    	usersTable = new JTable();
    	usersTable.setEnabled(false);
    	userTableScrollPane = new JScrollPane(usersTable);
    	usersPanel.add(userTableScrollPane, BorderLayout.CENTER);
    	
    	usersButtonPanel = new JPanel(new GridLayout(1,3));
    	
    	usersSaveButton = new JButton("Save");
    	usersUpdateButton = new JButton("Update User Info");
    	usersNewButton = new JButton("Add New User");
    	
    	usersButtonPanel.add(usersNewButton);
    	usersButtonPanel.add(usersUpdateButton);
    	usersButtonPanel.add(usersSaveButton);
    	
    	usersPanel.add(usersButtonPanel, BorderLayout.SOUTH);
      
        libraryTabbedPane.add("Users", usersPanel);


        //Books Section
        booksPanel = new JPanel(new BorderLayout());
        
        booksButtonPanel = new JPanel();
        booksAddBookButton = new JButton("Add Book to Archive");
        booksButtonPanel.add(booksAddBookButton);
        
        booksPanel.add(booksButtonPanel, BorderLayout.SOUTH);
        
        //New Book Information
        SpringLayout sprlayout = new SpringLayout();
        bookFormPanel = new JPanel(sprlayout);
        
        SpringUtilities.makeGrid(bookFormPanel,
                2, 4, //rows, cols
                5, 5, //initialX, initialY
                5, 5);//xPad, yPad
        
        bookTitleLabel = new JLabel("Title:");
        bookTitleTextField = new JTextField();
        
        bookEditionLabel = new JLabel("Edition:");
        bookEditionTextField = new JTextField();
        
        bookSubjectLabel = new JLabel("Subject:");
        bookSubjectTextField = new JTextField();
        
        bookAuthorFNLabel = new JLabel("Author First Name:");
        bookAuthorFNTextField = new JTextField();
        
        bookAuthorLNLabel = new JLabel("Author Last Name:");
        bookAuthorLNTextField = new JTextField();
        
        bookFormPanel.add(bookTitleLabel);
        bookFormPanel.add(bookTitleTextField);
        bookFormPanel.add(bookEditionLabel);
        bookFormPanel.add(bookEditionTextField);
        bookFormPanel.add(bookSubjectLabel);
        bookFormPanel.add(bookSubjectTextField);
        bookFormPanel.add(bookAuthorFNLabel);
        bookFormPanel.add(bookAuthorFNTextField);
        bookFormPanel.add(bookAuthorLNLabel);
        bookFormPanel.add(bookAuthorLNTextField);
        
        booksPanel.add(bookFormPanel, BorderLayout.CENTER);

        libraryTabbedPane.addTab("Books", booksPanel);

        //Loans Section
        loansPanel = new JPanel();
        booksListTable = new JTable();

        libraryTabbedPane.addTab("Loans", loansPanel);

        //Retrieval Section
        retrievalPanel = new JPanel();

        libraryTabbedPane.addTab("Retrieval", retrievalPanel);

        //display it
        this.setVisible(true);

    }//end constructor
    
    //When we have a button clicked we fire off a listener to all buttons and in our controller we will
    //parse through and delegate the events properly.
	public void addCalculateListener(ActionListener generalListener )
	{
		usersSaveButton.addActionListener(generalListener);
		usersUpdateButton.addActionListener(generalListener);
		usersNewButton.addActionListener(generalListener);
		
	}

}//end class
