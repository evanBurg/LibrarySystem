package com.dave.chan;

/**
 * Program Name: LibrarySystemView.java
 * Purpose: A GUI that the user will interact with in order to operate our library system. The View section of the
 * 			MVC architecture.
 * Coder: David Harrs & Evan Burgess
 * Date: July 30, 2018
 */

import javax.swing.*;
import javax.swing.border.EmptyBorder;

import java.awt.*;
import java.awt.event.*;

public class LibrarySystemView extends JFrame
{
	//class-wide
	JTabbedPane libraryTabbedPane;
	JFrame libraryFrame;
	JPanel basePanel, usersPanel, usersButtonPanel, booksPanel, loansPanel, retrievalPanel, booksButtonPanel, bookFormPanel, booksTitlePanel;
	JTable usersTable, booksListTable;
	JScrollPane userTableScrollPane;
	JButton usersSaveButton, usersUpdateButton, usersNewButton, booksAddBookButton;
	JLabel bookTitleLabel, bookEditionLabel, bookSubjectLabel, bookAuthorFNLabel, booksTitleLabel, booksCurrentAuthorsLabel;
	JTextArea bookTitleTextArea, bookEditionTextArea, bookSubjectTextArea, bookAuthorFNTextArea;

    public LibrarySystemView()
    {
        //boilerplate
        super("Library System");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLayout(new BorderLayout() );//ANONYMOUS layout object
        this.setSize(500,550);
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
        
        booksTitlePanel = new JPanel();
        
        booksTitleLabel = new JLabel("Add a New Book");
        
        booksTitlePanel.add(booksTitleLabel);
        
        booksPanel.add(booksTitlePanel, BorderLayout.NORTH);
        
        
        booksButtonPanel = new JPanel();
        BoxLayout booksBoxLayout = new BoxLayout(booksButtonPanel, BoxLayout.Y_AXIS);
        booksButtonPanel.setLayout(booksBoxLayout);
        booksButtonPanel.setBorder(new EmptyBorder(new Insets(20, 150, 20, 15)));
        
        booksCurrentAuthorsLabel = new JLabel("Number of Authors Added:");
        booksButtonPanel.add(booksCurrentAuthorsLabel);
        booksAddBookButton = new JButton("Add Book to Archive");
        booksButtonPanel.add(booksAddBookButton);
        
        booksPanel.add(booksButtonPanel, BorderLayout.SOUTH);
        
        //New Book Information
        SpringLayout sprlayout = new SpringLayout();
        bookFormPanel = new JPanel(sprlayout);
                
        bookTitleLabel = new JLabel("Title:");
        bookTitleTextArea = new JTextArea();
        
        bookEditionLabel = new JLabel("Edition:");
        bookEditionTextArea = new JTextArea();
        
        bookSubjectLabel = new JLabel("Subject:");
        bookSubjectTextArea = new JTextArea();
        
        bookAuthorFNLabel = new JLabel("Author Full Name:");
        bookAuthorFNTextArea = new JTextArea();
        
               
        bookFormPanel.add(bookTitleLabel);
        bookFormPanel.add(bookTitleTextArea);
        bookFormPanel.add(bookEditionLabel);
        bookFormPanel.add(bookEditionTextArea);
        bookFormPanel.add(bookSubjectLabel);
        bookFormPanel.add(bookSubjectTextArea);
        bookFormPanel.add(bookAuthorFNLabel);
        bookFormPanel.add(bookAuthorFNTextArea);
        
        SpringUtilities.makeGrid(bookFormPanel,
                4, 2, //rows, cols
                25, 25, //initialX, initialY
                25, 25);//xPad, yPad
        
        booksPanel.add(bookFormPanel, BorderLayout.CENTER);
        
        //authors added api
        booksCurrentAuthorsLabel = new JLabel("Number of Authors Added: ");

        
        
        
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
	public void addListener(ActionListener generalListener )
	{
		usersSaveButton.addActionListener(generalListener);
		usersUpdateButton.addActionListener(generalListener);
		usersNewButton.addActionListener(generalListener);
		
	}

}//end class
