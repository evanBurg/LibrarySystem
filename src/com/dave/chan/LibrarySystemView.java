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
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;
import javax.swing.plaf.basic.BasicComboPopup;
import javax.swing.plaf.basic.ComboPopup;
import javax.swing.table.TableModel;

public class LibrarySystemView extends JFrame
{
	//class-wide
	JTabbedPane libraryTabbedPane;
	
	JFrame libraryFrame;
	
	JComboBox authorComboBox, subjectComboBox;
	
	ButtonGroup searchGroup;
	
	JPanel basePanel, usersPanel, usersButtonPanel, booksPanel, retrievalPanel, booksButtonPanel, 
				bookFormPanel, booksTitlePanel, hubBtnPanel, retrievalBtnPanel, searchPanel, searchUIPanel, searchButtonPanel
				, addBookPanel;
	
	JTable usersTable, retrievalTable, searchTable, loansTable;
	
	JRadioButton subjectRadioButton, authorRadioButton;
	
	JScrollPane userTableScrollPane, retrievalTableScrollPane, searchTableScrollPane;
	
	JButton usersSaveButton, usersUpdateButton, usersNewButton, booksAddBookButton, 
			searchCheckOutBtn, searchCheckInBtn, addUserDialogButton, retrievalOverdueButton, retrievalUsersBorrowButton, 
			retrievalBooksOnLoanButton, retrievalBooksButton, addBookBtn;
	
	JLabel bookTitleLabel, bookEditionLabel, bookSubjectLabel, bookAuthorFNLabel, booksTitleLabel, booksCurrentAuthorsLabel,
				searchInfoLabel;
	
	JTextArea bookTitleTextArea, bookEditionTextArea, bookSubjectTextArea, bookAuthorFNTextArea;
	
	AddUserDialog addUserDialog;
	
	LoanDialog loanDialog;
	
    public LibrarySystemView()
    {
        //boilerplate
        super("Library System");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
       // this.setLayout(new BorderLayout() );//ANONYMOUS layout object
        this.setSize(700,550);
        this.setLocationRelativeTo(null);

        basePanel = new JPanel(new BorderLayout());
        this.add(basePanel);

        libraryTabbedPane = new JTabbedPane();
        basePanel.add(libraryTabbedPane);
        
        //method calls to load different view tabs in our system
        loadUserView();
        loadHubView();
        loadSearchView();
                      
        //display it
        this.setVisible(true);

    }//end constructor
    
    private void loadHubView()
    {
    	 retrievalPanel = new JPanel(new BorderLayout());
    	 
         hubBtnPanel = new JPanel(new BorderLayout());
         
         retrievalBtnPanel = new JPanel(new GridLayout(1,4,3,3));
         addBookPanel = new JPanel();
         
         retrievalBooksButton = new JButton("Book Index");
         retrievalOverdueButton = new JButton("Overdue Index");
         retrievalBooksOnLoanButton = new JButton("Checked Out");
         retrievalUsersBorrowButton = new JButton("Borrowing Books");
         
         addBookBtn = new JButton("Add New Book");
         addBookPanel = new JPanel();
         
         retrievalBtnPanel.add(retrievalBooksButton);
         retrievalBtnPanel.add(retrievalOverdueButton);
         retrievalBtnPanel.add(retrievalBooksOnLoanButton);
         retrievalBtnPanel.add(retrievalUsersBorrowButton);
       
         addBookPanel.add(addBookBtn);
         
         hubBtnPanel.add(retrievalBtnPanel, BorderLayout.WEST);
         hubBtnPanel.add(addBookPanel, BorderLayout.EAST);
         
         retrievalTable = new JTable();
         retrievalTable.setEnabled(false);
         retrievalTableScrollPane = new JScrollPane(retrievalTable);
         
         retrievalPanel.add(retrievalTableScrollPane, BorderLayout.CENTER); 
         retrievalPanel.add(hubBtnPanel, BorderLayout.SOUTH);

         libraryTabbedPane.addTab("Retrieval", retrievalPanel);
    }
    
    private void loadUserView()
    {
    	
    	usersPanel = new JPanel(new BorderLayout());
     	usersTable = new JTable();
     	usersTable.setEnabled(false);
        usersTable.putClientProperty("terminateEditOnFocusLost", Boolean.TRUE);
     	userTableScrollPane = new JScrollPane(usersTable);
     	usersPanel.add(userTableScrollPane, BorderLayout.CENTER);
     	
     	usersButtonPanel = new JPanel(new GridLayout(1, 3, 3, 3));
     	
     	usersSaveButton = new JButton("Save");
     	usersUpdateButton = new JButton("Update User Info");
     	usersNewButton = new JButton("Add New User");
     	
     	usersButtonPanel.add(usersNewButton);
        addUserDialog = new AddUserDialog();
     	usersButtonPanel.add(usersUpdateButton);
     	usersButtonPanel.add(usersSaveButton);
     	
     	usersPanel.add(usersButtonPanel, BorderLayout.SOUTH);
       
        libraryTabbedPane.add("Users", usersPanel);
    }
    
    private void loadSearchView()
    {
    	searchPanel = new JPanel(new BorderLayout());
        searchButtonPanel = new JPanel(new GridLayout(1, 3, 3, 3));
        searchUIPanel = new JPanel();
        loanDialog = new LoanDialog();
        
        searchCheckOutBtn = new JButton("Check Out Book");
        searchCheckInBtn = new JButton("Check In Book");
        searchButtonPanel.add(searchCheckInBtn);
        searchButtonPanel.add(searchCheckOutBtn);
        searchPanel.add(searchButtonPanel, BorderLayout.SOUTH);

        searchInfoLabel = new JLabel("Search By:");
          
        searchUIPanel.add(searchInfoLabel);

        authorComboBox = new JComboBox();
        subjectComboBox = new JComboBox();
        
        authorComboBox.setPrototypeDisplayValue("Choose an Author");
        authorComboBox.setEditable(false);
        searchUIPanel.add(authorComboBox);

        subjectComboBox.setPrototypeDisplayValue("Choose a Subject");
        subjectComboBox.setEditable(false);
        searchUIPanel.add(subjectComboBox);
        
        searchPanel.add(searchUIPanel, BorderLayout.NORTH);
        
        searchTable = new JTable();
        searchTable.setEnabled(false);
        searchTableScrollPane = new JScrollPane(searchTable);
        
        searchPanel.add(searchTableScrollPane, BorderLayout.CENTER);    
        
        libraryTabbedPane.addTab("Search", searchPanel);
    }
    
    //When we have a button clicked we fire off a listener to all buttons and in our controller we will
    //parse through and delegate the events properly.
	public void addListener(ActionListener generalListener )
	{
		//User Tab Button Listeners
		usersSaveButton.addActionListener(generalListener);
		usersUpdateButton.addActionListener(generalListener);
		usersNewButton.addActionListener(generalListener);
		addUserDialogButton.addActionListener(generalListener);
		
		//Books Tab Button Listeners
		//booksAddBookButton.addActionListener(generalListener);
		
		//Loans Tab Button Listeners
		//Will have a dialogue loan button listener to put here eventually, I reckon
        searchCheckInBtn.addActionListener(generalListener);
        searchCheckOutBtn.addActionListener(generalListener);
        
        //Retrieval Tab Button Listeners
        retrievalBooksButton.addActionListener(generalListener);
        retrievalBooksOnLoanButton.addActionListener(generalListener);
        retrievalUsersBorrowButton.addActionListener(generalListener);
        retrievalOverdueButton.addActionListener(generalListener);
        
        //Search Tab Button Listeners
        subjectComboBox.addActionListener(generalListener);
        authorComboBox.addActionListener(generalListener);
        loanDialog.acceptButton.addActionListener(generalListener);
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
	
	public class LoanDialog extends JFrame{
	    JLabel bookLabel;
	    JComboBox<String> booksToChooseFrom, ISBNs, borrowers, borrowersIds;
	    boolean isLoaning;
        JButton acceptButton;
        public LoanDialog(){
            super("Library System");
            this.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
            this.setLayout(new GridLayout(4,1, 3, 3) );//ANONYMOUS layout object
            this.setSize(500,200);
            this.setLocationRelativeTo(null);

            this.isLoaning = isLoaning;
            bookLabel = new JLabel("Choose a Book");
            booksToChooseFrom = new JComboBox<String>();
            acceptButton = new JButton();
            borrowers = new JComboBox<String>();
            borrowersIds = new JComboBox<String>();
            ISBNs = new JComboBox<String>();

            this.add(bookLabel);
            this.add(booksToChooseFrom);
            this.add(borrowers);
            this.add(acceptButton);
        }

        public void openLoanDialog(boolean isLoaning, DefaultComboBoxModel<String> bookStrings, TableModel books, DefaultComboBoxModel<String> ISBNs, DefaultComboBoxModel<String> borrowers, DefaultComboBoxModel<String> borrowersIds){
            this.isLoaning = isLoaning;
            booksToChooseFrom.setModel(bookStrings);
            this.ISBNs.setModel(ISBNs);
            this.borrowers.setModel(borrowers);
            this.borrowersIds.setModel(borrowersIds);
            if(isLoaning) {
                acceptButton.setText("Check Out this Book");
            }else{
                acceptButton.setText("Check In this Book");
            }
            this.setVisible(true);
        }
    }

}//end class
