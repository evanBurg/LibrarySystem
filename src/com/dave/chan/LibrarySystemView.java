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
	
	JPanel basePanel, usersPanel, usersButtonPanel, booksPanel, loansPanel, retrievalPanel, booksButtonPanel, 
				bookFormPanel, booksTitlePanel, loansButtonPanel;
	
	JTable usersTable, booksListTable, loansTable;
	JScrollPane userTableScrollPane, loansTableScrollPane;
	JButton usersSaveButton, usersUpdateButton, usersNewButton, booksAddBookButton, 
			loansCheckOutBtn, loansCheckInBtn, addUserDialogButton;
	
	JLabel bookTitleLabel, bookEditionLabel, bookSubjectLabel, bookAuthorFNLabel, booksTitleLabel, booksCurrentAuthorsLabel;
	
	JTextArea bookTitleTextArea, bookEditionTextArea, bookSubjectTextArea, bookAuthorFNTextArea;
	
	AddUserDialog addUserDialog;
	
	NewLoanDialog newLoanDialog;

	public class NewLoanDialog extends JFrame{
	    JLabel addUserFirstNameLabel, addUserLastNameLabel, addUserEmailLabel;
        JTextArea addUserFirstName, addUserLastName, addUserEmail;
        public NewLoanDialog(){
            super("Library System");
            this.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
            this.setLayout(new FlowLayout() );//ANONYMOUS layout object
            this.setSize(500,200);
            this.setLocationRelativeTo(null);

            addUserFirstNameLabel = new JLabel("First Name:");
            addUserLastNameLabel = new JLabel("Last Name:");
            addUserEmailLabel = new JLabel("Email:");

            addUserFirstName = new JTextArea(1, 7);
            addUserLastName = new JTextArea(1, 7);
            addUserEmail = new JTextArea(1, 7);

            this.add(addUserFirstNameLabel);
            this.add(addUserFirstName);
            this.add(addUserLastNameLabel);
            this.add(addUserLastName);
            this.add(addUserEmailLabel);
            this.add(addUserEmail);
        }
    }
	
	public class AddUserDialog extends JFrame{
	    JLabel addUserFirstNameLabel, addUserLastNameLabel, addUserEmailLabel;
        JTextField addUserFirstName, addUserLastName, addUserEmail;
        JPanel inputPanel, buttonPanel;
        public AddUserDialog(){
            super("Library System");
            this.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
            this.setLayout(new BorderLayout() );//ANONYMOUS layout object
            this.setSize(300,200);
            this.setLocationRelativeTo(null);

            addUserFirstNameLabel = new JLabel("First Name:");
            addUserLastNameLabel = new JLabel("Last Name:");
            addUserEmailLabel = new JLabel("Email:");

            addUserFirstName = new JTextField();
            addUserLastName = new JTextField();
            addUserEmail = new JTextField();

            addUserDialogButton = new JButton("Add User");

            inputPanel = new JPanel(new GridLayout(3, 2, 5, 35));
            this.add(inputPanel, BorderLayout.CENTER);

            inputPanel.add(addUserFirstNameLabel);
            inputPanel.add(addUserFirstName);
            inputPanel.add(addUserLastNameLabel);
            inputPanel.add(addUserLastName);
            inputPanel.add(addUserEmailLabel);
            inputPanel.add(addUserEmail);
            
            buttonPanel = new JPanel();
            buttonPanel.add(addUserDialogButton);
            this.add(buttonPanel, BorderLayout.SOUTH);
        }
    }

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
        addUserDialog = new AddUserDialog();
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
        //BoxLayout this bitch potentially
        SpringLayout sprlayout = new SpringLayout();
        bookFormPanel = new JPanel(sprlayout);
                
        bookTitleLabel = new JLabel("Title:");
        bookTitleTextArea = new JTextArea();
        
        bookEditionLabel = new JLabel("Edition:");
        bookEditionTextArea = new JTextArea();
        
        bookSubjectLabel = new JLabel("Subject:");
        bookSubjectTextArea = new JTextArea();
        
        bookAuthorFNLabel = new JLabel("Author Full Name:");
        bookAuthorFNTextArea = new JTextArea("Dickens, Charles");
        bookAuthorFNTextArea.setForeground(Color.GRAY);
    	Font italicFont = new Font("Sans-Serif",Font.ITALIC, 12);
    	Font stdFont = new Font("Sans-Serif", Font.PLAIN, 12);
    	bookAuthorFNTextArea.setFont(italicFont);
        bookAuthorFNTextArea.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                if (bookAuthorFNTextArea.getText().equals("Dickens, Charles")) {
                	bookAuthorFNTextArea.setText("");
                	bookAuthorFNTextArea.setFont(stdFont);
                	bookAuthorFNTextArea.setForeground(Color.BLACK);
                }
            }
            @Override
            public void focusLost(FocusEvent e) {
                if (bookAuthorFNTextArea.getText().isEmpty()) {
                	bookAuthorFNTextArea.setForeground(Color.GRAY);
                	bookAuthorFNTextArea.setFont(italicFont);
                	bookAuthorFNTextArea.setText("Dickens, Charles");
                }
            }
            });
             
        bookFormPanel.add(bookTitleLabel);
        bookFormPanel.add(bookTitleTextArea);
        bookFormPanel.add(bookEditionLabel);
        bookFormPanel.add(bookEditionTextArea);
        bookFormPanel.add(bookSubjectLabel);
        bookFormPanel.add(bookSubjectTextArea);
        bookFormPanel.add(bookAuthorFNLabel);
        bookFormPanel.add(bookAuthorFNTextArea);
        
        SpringUtilities.makeCompactGrid(bookFormPanel,
                4, 2, //rows, cols
                25, 25, //initialX, initialY
                25, 25);//xPad, yPad
        
        booksPanel.add(bookFormPanel, BorderLayout.CENTER);
        
        //authors added api
        booksCurrentAuthorsLabel = new JLabel("Number of Authors Added: ");
        
        libraryTabbedPane.addTab("Books", booksPanel);

        //Loans Section
        loansPanel = new JPanel(new BorderLayout());
        loansTable = new JTable();
        loansTable.setEnabled(false);
        loansTableScrollPane = new JScrollPane(loansTable);
        loansPanel.add(loansTableScrollPane, BorderLayout.CENTER);
        
        loansButtonPanel = new JPanel();
        loansCheckOutBtn = new JButton("Check Out Book");
        newLoanDialog = new NewLoanDialog();
        loansCheckInBtn = new JButton("Check In Book");
        loansButtonPanel.add(loansCheckOutBtn);
        loansButtonPanel.add(loansCheckInBtn);
        
        loansPanel.add(loansButtonPanel, BorderLayout.SOUTH);
        
        //booksListTable = new JTable();

        libraryTabbedPane.addTab("Loans", loansPanel);

        //Retrieval Section
        retrievalPanel = new JPanel(new BorderLayout());
        

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
		addUserDialogButton.addActionListener(generalListener);
		booksAddBookButton.addActionListener(generalListener);
		
	}

}//end class
