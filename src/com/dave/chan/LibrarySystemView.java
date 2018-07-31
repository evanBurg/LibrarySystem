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

public class LibrarySystemView extends JFrame
{
	//class-wide
	JTabbedPane libraryTabbedPane;
	
	JFrame libraryFrame;
	
	JComboBox authorComboBox, subjectComboBox;
	
	ButtonGroup searchGroup;
	
	JPanel basePanel, usersPanel, usersButtonPanel, booksPanel, loansPanel, retrievalPanel, booksButtonPanel, 
				bookFormPanel, booksTitlePanel, loansButtonPanel, retrievalButtonPanel, searchPanel, searchUIPanel;
	
	JTable usersTable, loansTable, retrievalTable, searchTable;
	
	JRadioButton subjectRadioButton, authorRadioButton;
	
	JScrollPane userTableScrollPane, loansTableScrollPane, retrievalTableScrollPane, searchTableScrollPane;
	
	JButton usersSaveButton, usersUpdateButton, usersNewButton, booksAddBookButton, 
			loansCheckOutBtn, loansCheckInBtn, addUserDialogButton, retrievalOverdueButton, retrievalUsersBorrowButton, 
			retrievalBooksOnLoanButton, retrievalBooksButton;
	
	JLabel bookTitleLabel, bookEditionLabel, bookSubjectLabel, bookAuthorFNLabel, booksTitleLabel, booksCurrentAuthorsLabel,
				searchInfoLabel;
	
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
        this.setSize(600,550);
        this.setLocationRelativeTo(null);

        basePanel = new JPanel(new FlowLayout());
        this.add(basePanel, BorderLayout.CENTER);

        libraryTabbedPane = new JTabbedPane();
        basePanel.add(libraryTabbedPane);

        //Users Section
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
        
        libraryTabbedPane.addTab("Loans", loansPanel);

        //Retrieval Section
        retrievalPanel = new JPanel(new BorderLayout());
        retrievalButtonPanel = new JPanel(new GridLayout(1, 4, 3, 3));
        
        retrievalBooksButton = new JButton("Book Index");
        retrievalBooksOnLoanButton = new JButton("Checked Out");
        retrievalUsersBorrowButton = new JButton("Borrowing Books");
        retrievalOverdueButton = new JButton("Overdue Index");
        
        retrievalButtonPanel.add(retrievalBooksButton);
        retrievalButtonPanel.add(retrievalBooksOnLoanButton);
        retrievalButtonPanel.add(retrievalUsersBorrowButton);
        retrievalButtonPanel.add(retrievalOverdueButton);
        
        retrievalPanel.add(retrievalButtonPanel, BorderLayout.SOUTH);
        
        retrievalTable = new JTable();
        retrievalTable.setEnabled(false);
        retrievalTableScrollPane = new JScrollPane(retrievalTable);
        
        retrievalPanel.add(retrievalTableScrollPane, BorderLayout.CENTER);    

        libraryTabbedPane.addTab("Retrieval", retrievalPanel);
        
        //Search Section
        searchPanel = new JPanel(new BorderLayout());
        
        searchUIPanel = new JPanel();
        
        searchInfoLabel = new JLabel("Search By:");
        authorRadioButton = new JRadioButton("Author");
        subjectRadioButton = new JRadioButton("Subject");
        
        
        
        //searchGroup = new ButtonGroup();
        //searchGroup.add(subjectRadioButton);
        //searchGroup.add(authorRadioButton);
        
        searchUIPanel.add(searchInfoLabel);
        //searchUIPanel.add(authorRadioButton);
        //searchUIPanel.add(subjectRadioButton);

        //authorComboBox, subjectComboBox
        authorComboBox = new JComboBox();
        subjectComboBox = new JComboBox();
        //unverified if it works yet but hoping it does
        BoundsPopupMenuListener listener = new BoundsPopupMenuListener(true, false);
        authorComboBox.addPopupMenuListener( listener );
        authorComboBox.setPrototypeDisplayValue("Choose an Author");
        authorComboBox.setEditable(false);
        searchUIPanel.add(authorComboBox);

        subjectComboBox.addPopupMenuListener( listener );
        subjectComboBox.setPrototypeDisplayValue("Choose a Subject");
        subjectComboBox.setEditable(false);
        searchUIPanel.add(subjectComboBox);
        
        searchPanel.add(searchUIPanel, BorderLayout.NORTH);
        
        searchTable = new JTable();
        searchTable.setEnabled(false);
        searchTableScrollPane = new JScrollPane(searchTable);
        
        searchPanel.add(searchTableScrollPane, BorderLayout.CENTER);    
        
        libraryTabbedPane.addTab("Search", searchPanel);
        
        //display it
        this.setVisible(true);

    }//end constructor

    //When we have a button clicked we fire off a listener to all buttons and in our controller we will
    //parse through and delegate the events properly.
	public void addListener(ActionListener generalListener )
	{
		//User Tab Button Listeners
		usersSaveButton.addActionListener(generalListener);
		usersUpdateButton.addActionListener(generalListener);
		usersNewButton.addActionListener(generalListener);
		addUserDialogButton.addActionListener(generalListener);
		booksAddBookButton.addActionListener(generalListener);
		
		//Books Tab Button Listeners
		booksAddBookButton.addActionListener(generalListener);
		
		//Loans Tab Button Listeners
		//Will have a dialogue loan button listener to put here eventually, I reckon
        loansCheckInBtn.addActionListener(generalListener);
        loansCheckOutBtn.addActionListener(generalListener);
        
        //Retrieval Tab Button Listeners
        retrievalBooksButton.addActionListener(generalListener);
        retrievalBooksOnLoanButton.addActionListener(generalListener);
        retrievalUsersBorrowButton.addActionListener(generalListener);
        retrievalOverdueButton.addActionListener(generalListener);
        
        //Search Tab Button Listeners
        subjectComboBox.addActionListener(generalListener);
        authorComboBox.addActionListener(generalListener);
        //subjectRadioButton.addActionListener(generalListener);
        //authorRadioButton.addActionListener(generalListener);
	}

}//end class
