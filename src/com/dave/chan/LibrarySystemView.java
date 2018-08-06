package com.dave.chan;

/**
 * Program Name: LibrarySystemView.java
 * Purpose: A GUI that the user will interact with in order to operate our library system. The View section of the
 * 			MVC architecture.
 * Coder: David Harris & Evan Burgess
 * Date: July 30, 2018
 */

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;
import java.util.List;

public class LibrarySystemView extends JFrame
{
	//class-wide
    private JTabbedPane libraryTabbedPane;
	
	private JComboBox authorComboBox, subjectComboBox;

    private JPanel basePanel, usersPanel, usersButtonPanel, retrievalPanel, hubBtnPanel, retrievalBtnPanel, searchPanel,
				searchUIPanel, searchButtonPanel, addBookPanel;

    private JTable usersTable, retrievalTable, searchTable;

    private JScrollPane userTableScrollPane, retrievalTableScrollPane, searchTableScrollPane;

    private JLabel searchInfoLabel;

    public JButton usersSaveButton, usersUpdateButton, usersNewButton,
            searchCheckOutBtn, searchCheckInBtn, retrievalOverdueButton,
            retrievalBooksOnLoanButton, retrievalBooksButton, addBookBtn;

    public JMenuBar menubar;
    public JMenu file;
    public JMenuItem exit, help;

    public Border greenBorder, emptyBorder;

    public AddUserDialog addUserDialog;

    public AddBookDialog addBookDialog;

    public LoanDialog loanDialog;
	
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
        loadIndexView();
        loadSearchView();

        //Create the menu bar
        menubar = new JMenuBar();
        file = new JMenu("File");
        exit = new JMenuItem("Exit");
        help = new JMenuItem("Help");
        file.add(help);
        file.add(exit);
        menubar.add(file);
        this.setJMenuBar(menubar);

        //Instantiate the borders
        greenBorder = BorderFactory.createLineBorder(Color.green);
        emptyBorder = BorderFactory.createEmptyBorder();

        //display it
        this.setVisible(true);

    }//end constructor
    
    //method for the index portion of our app
    private void loadIndexView()
    {
    	//Gui setup
    	 retrievalPanel = new JPanel(new BorderLayout());
    	 
    	 //we have a dialog object for one of the buttons when the user wants to add a new book
    	 addBookDialog = new AddBookDialog();
    	 
         hubBtnPanel = new JPanel(new BorderLayout());
         
         retrievalBtnPanel = new JPanel(new GridLayout(1,4,3,3));
         addBookPanel = new JPanel();
         
         retrievalBooksButton = new JButton("Book Index");
         retrievalOverdueButton = new JButton("Overdue Index");
         retrievalBooksOnLoanButton = new JButton("Checked Out Index");

         addBookBtn = new JButton("Add New Book");
         addBookPanel = new JPanel();
         
         retrievalBtnPanel.add(retrievalBooksButton);
         retrievalBtnPanel.add(retrievalOverdueButton);
         retrievalBtnPanel.add(retrievalBooksOnLoanButton);
       
         addBookPanel.add(addBookBtn);
         
         hubBtnPanel.add(retrievalBtnPanel, BorderLayout.WEST);
         hubBtnPanel.add(addBookPanel, BorderLayout.EAST);
         
         //jtable we use to display data from the various buttons
         retrievalTable = new JTable();
         retrievalTable.setEnabled(false);
         retrievalTableScrollPane = new JScrollPane(retrievalTable);
         
         retrievalPanel.add(retrievalTableScrollPane, BorderLayout.CENTER); 
         retrievalPanel.add(hubBtnPanel, BorderLayout.SOUTH);

         libraryTabbedPane.addTab("Index", retrievalPanel);
    }
    
    private void loadUserView()
    {
    	//gui setup
    	usersPanel = new JPanel(new BorderLayout());
     	usersTable = new JTable();
     	usersTable.setEnabled(false);
     	
     	//to make jtable end editing and clear current selected rows when it loses focus
        usersTable.putClientProperty("terminateEditOnFocusLost", Boolean.TRUE);
     	userTableScrollPane = new JScrollPane(usersTable);
     	usersPanel.add(userTableScrollPane, BorderLayout.CENTER);
     	
     	usersButtonPanel = new JPanel(new GridLayout(1, 3, 3, 3));
     	
     	usersSaveButton = new JButton("Save");
     	usersSaveButton.setEnabled(false);
     	usersUpdateButton = new JButton("Unlock Table for Editing");
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
    	//gui setup
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
        
        //we dont allow the users to edit our combobox, and we give them default values so the user
        //understands what the comboboxes are for
        authorComboBox.setPrototypeDisplayValue("Choose an Author");
        authorComboBox.setEditable(false);
        searchUIPanel.add(authorComboBox);

        subjectComboBox.setPrototypeDisplayValue("Choose a Subject");
        subjectComboBox.setEditable(false);
        searchUIPanel.add(subjectComboBox);
        
        searchPanel.add(searchUIPanel, BorderLayout.NORTH);
        
        //jtable to display db query results
        searchTable = new JTable();
        searchTable.setEnabled(false);
        searchTableScrollPane = new JScrollPane(searchTable);
        
        //lets us scroll through the data if it exceeds the fram
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
		addUserDialog.addUserDialogButton.addActionListener(generalListener);
	    
        //Index Tab Button Listeners
        retrievalBooksButton.addActionListener(generalListener);
        retrievalBooksOnLoanButton.addActionListener(generalListener);
        retrievalOverdueButton.addActionListener(generalListener);
        addBookBtn.addActionListener(generalListener);
        addBookDialog.addAuthorBtn.addActionListener(generalListener);
        addBookDialog.addBookButton.addActionListener(generalListener);
        
        //Search Tab Button Listeners
        subjectComboBox.addActionListener(generalListener);
        authorComboBox.addActionListener(generalListener);
        loanDialog.acceptButton.addActionListener(generalListener);
        searchCheckInBtn.addActionListener(generalListener);
        searchCheckOutBtn.addActionListener(generalListener);

        //Menubar listeners
        exit.addActionListener(generalListener);
        help.addActionListener(generalListener);
	}
	
	//this dialog window pops up when we use the add book jbutton
	public class AddBookDialog extends JFrame{
		//variable declarations in this class's scope
	    JLabel addBookTitleLabel, addBookSubjectLabel, addBookEditionLabel, addBookAuthorLabel, addBookISBNLabel;
        JTextField addBookTitle, addBookSubject, addBookEdition, addBookISBN, addAuthorTxtFld;
        JPanel basePanel, inputPanel, authorPanel, buttonPanel, subPanel;
        JList<String> addBookAuthorList;
        JScrollPane scrollPane;
        JButton addBookButton, addAuthorBtn;
        //constructor
        public AddBookDialog(){
        	//boilerplate
            super("Add Book");
            this.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
            this.setLayout(new BorderLayout() );//ANONYMOUS layout object
            this.setSize(250,350);
            this.setLocationRelativeTo(null);
            
            //gui
            basePanel = new JPanel(new BorderLayout());

            addBookTitleLabel = new JLabel("Title:");
            addBookSubjectLabel = new JLabel("Subject:");
            addBookEditionLabel = new JLabel("Edition:");
            addBookISBNLabel = new JLabel("ISBN:");
            addBookTitleLabel.setHorizontalAlignment(JLabel.CENTER);
            addBookSubjectLabel.setHorizontalAlignment(JLabel.CENTER);
            addBookEditionLabel.setHorizontalAlignment(JLabel.CENTER);
            addBookISBNLabel.setHorizontalAlignment(JLabel.CENTER);
            
            //we use a jlist to display our list of authors
            addBookAuthorList = new JList<String>();
            scrollPane = new JScrollPane();
            //we set this visible count to 5 so the scroll pane comes into play and we can
            //use it to see the rest of the list
            addBookAuthorList.setVisibleRowCount(5);
            scrollPane.setViewportView(addBookAuthorList);
        
            addBookTitle = new JTextField();
            addBookSubject = new JTextField();
            addBookEdition = new JTextField();
            addBookISBN = new JTextField();
            addAuthorTxtFld = new JTextField();
            
            //this provides a filler text for our jtextfield
            //the goal is to show the user the intended format for a new author entry
            //and when they leave the scope of the textfield if there was no user input 
            //it reappears, pretty lit.
        	Font italicFont = new Font("Sans-Serif",Font.ITALIC, 11);
        	Font stdFont = new Font("Sans-Serif", Font.PLAIN, 11);
            addAuthorTxtFld.setFont(italicFont);
          	addAuthorTxtFld.setForeground(Color.GRAY);
           	addAuthorTxtFld.setText("Dickens, Charles");
            addAuthorTxtFld.addFocusListener(new FocusListener() {
                @Override
                public void focusGained(FocusEvent e) {
                    if (addAuthorTxtFld.getText().equals("Dickens, Charles")) {
                    	addAuthorTxtFld.setText("");
                    	addAuthorTxtFld.setFont(stdFont);
                    	addAuthorTxtFld.setForeground(Color.BLACK);
                    }
                }
                @Override
                public void focusLost(FocusEvent e) {
                    if (addAuthorTxtFld.getText().isEmpty()) {
                    	addAuthorTxtFld.setForeground(Color.GRAY);
                    	addAuthorTxtFld.setFont(italicFont);
                    	addAuthorTxtFld.setText("Dickens, Charles");
                    }
                }
            });

            //more gui
            addAuthorBtn = new JButton("Add Author");

            inputPanel = new JPanel(new GridLayout(5, 2, 30, 10));
            basePanel.add(inputPanel, BorderLayout.CENTER);

            inputPanel.add(addBookTitleLabel);
            inputPanel.add(addBookTitle);
            inputPanel.add(addBookSubjectLabel);
            inputPanel.add(addBookSubject);
            inputPanel.add(addBookISBNLabel);
            inputPanel.add(addBookISBN);
            inputPanel.add(addBookEditionLabel);
            inputPanel.add(addBookEdition);
            
            buttonPanel = new JPanel(new GridLayout(2,2, 5,5));
            addBookButton = new JButton("Add Book");
            buttonPanel.add(addAuthorBtn);
            buttonPanel.add(addAuthorTxtFld);
            buttonPanel.add(addBookButton);

            authorPanel = new JPanel(new BorderLayout());
            subPanel = new JPanel();
            subPanel.add(scrollPane);
            authorPanel.add(subPanel, BorderLayout.CENTER);
            addBookAuthorLabel = new JLabel("Author(s)");
            addBookAuthorLabel.setHorizontalAlignment(JLabel.CENTER);
            authorPanel.add(addBookAuthorLabel, BorderLayout.NORTH);
            basePanel.add(authorPanel, BorderLayout.SOUTH);

            this.add(basePanel, BorderLayout.CENTER);
            this.add(buttonPanel, BorderLayout.SOUTH);
        }

        //GETTERS AND SETTERS

        //Set the author list model
        public void setAddBookAuthorList(DefaultComboBoxModel<String> model){
            model.removeElementAt(0);
            addBookAuthorList.setModel(model);
        }

        //Get the author field text
        public String getAddAuthorText(){
            return addAuthorTxtFld.getText();
        }

        //Get the book field text
        public String getBookTitle(){
            return addBookTitle.getText();
        }

        //Get the ISBN field text
        public String getBookISBN(){
            return addBookISBN.getText();
        }

        //Get the edition field text
        public int getBookEdition(){
            return Integer.parseInt(addBookEdition.getText());
        }

        //Get the subject field text
        public String getSubject(){
            return addBookSubject.getText();
        }

        //Get the selected authors
        public List<String> getAuthorsSelectedList(){
            return addBookAuthorList.getSelectedValuesList();
        }

        //Clear all the fields
        public void clearFields(){
            addBookTitle.setText("");
            addBookSubject.setText("");
            addBookEdition.setText("");
            addBookISBN.setText("");
            addAuthorTxtFld.setText("");
        }
    }
	
	//dialog window that pops up to provide a new registration form for a user
	public class AddUserDialog extends JFrame{
		//class scope variable declaration
	    JLabel addUserFirstNameLabel, addUserLastNameLabel, addUserEmailLabel;
        JTextField addUserFirstName, addUserLastName, addUserEmail;
        JPanel inputPanel, buttonPanel;
        JButton addUserDialogButton;
        //constructor
        public AddUserDialog(){
        	//boilerplate
            super("Add User");
            this.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
            this.setLayout(new BorderLayout() );//ANONYMOUS layout object
            this.setSize(250,160);
            this.setLocationRelativeTo(null);

            //gui stuff
            addUserFirstNameLabel = new JLabel("First Name:");
            addUserLastNameLabel = new JLabel("Last Name:");
            addUserEmailLabel = new JLabel("Email:");
            addUserFirstNameLabel.setHorizontalAlignment(JLabel.CENTER);
            addUserLastNameLabel.setHorizontalAlignment(JLabel.CENTER);
            addUserEmailLabel.setHorizontalAlignment(JLabel.CENTER);

            addUserFirstName = new JTextField();
            addUserLastName = new JTextField();
            addUserEmail = new JTextField();

            addUserDialogButton = new JButton("Add User");

            inputPanel = new JPanel(new GridLayout(3, 2, 30, 10));
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

        //GETTERS AND SETTERS

        //Get the first name text
        public String getFirstName(){
            return addUserFirstName.getText();
        }

        //Get the last name text
        public String getLastName(){
            return addUserLastName.getText();
        }

        //Get the email text
        public String getEmail(){
            return addUserEmail.getText();
        }

        //Clear all the fields
        public void clearFields(){
            addUserFirstName.setText("");
            addUserLastName.setText("");
            addUserEmail.setText("");
        }
    }
	
	//dialog box that opens when a user is checking out or in a new book
	//two BIRDS, ONE STONE
	public class LoanDialog extends JFrame{
		//class scope variable declaration
	    JLabel bookLabel;
	    JComboBox<String> booksToChooseFrom, ISBNs, borrowers, borrowersIds;
	    boolean isLoaning;
        JButton acceptButton;
        //constructor
        public LoanDialog(){
        	//boilerplate
            super("New Loan");
            this.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
            this.setLayout(new GridLayout(4,1, 3, 3) );//ANONYMOUS layout object
            this.setSize(500,200);
            this.setLocationRelativeTo(null);
            
            //we use this boolean to determine whether or not the user will be opening a new loan
            //or closing a currently open loan on a book
            this.isLoaning = isLoaning;
            bookLabel = new JLabel("Choose a Book");
            
            //combobox list of the books available to choose from to check in out check out
            booksToChooseFrom = new JComboBox<String>();
            acceptButton = new JButton();
            //combobox list of users to use for the check in our out process
            borrowers = new JComboBox<String>();
            borrowersIds = new JComboBox<String>();
            ISBNs = new JComboBox<String>();

            this.add(bookLabel);
            this.add(booksToChooseFrom);
            this.add(borrowers);
            this.add(acceptButton);
        }
        
        //we use this method in the controller to determine the paramaters for the user to either
        //check in, or be checking out a book
        public void openLoanDialog(boolean isLoaning, DefaultComboBoxModel<String> bookStrings, TableModel books, DefaultComboBoxModel<String> ISBNs, DefaultComboBoxModel<String> borrowers, DefaultComboBoxModel<String> borrowersIds){
            this.isLoaning = isLoaning;
            booksToChooseFrom.setModel(bookStrings);
            this.ISBNs.setModel(ISBNs);
            this.borrowers.setModel(borrowers);
            this.borrowersIds.setModel(borrowersIds);
            
            //modularity on our accept button depending on the user's action based on the boolean isLoaning
            if(isLoaning) {
                acceptButton.setText("Check Out this Book");
            }else{
                acceptButton.setText("Check In this Book");
            }
            this.setVisible(true);
        }

        //Get the ID of the selected book in the jcombobox
        public int getSelectedBookID(){
            return booksToChooseFrom.getSelectedIndex();
        }

        //Get the ID of the selected borrower in the jcombobox
        public int getSelectedBorrowerID(){
            return borrowers.getSelectedIndex();
        }

        //Get the ID of the selected borrower in the database
        public int getDatabaseBorrowerID(int selectedBorrower){
            return Integer.parseInt(borrowersIds.getItemAt(selectedBorrower));
        }

        //Get the ISBN of the selected book in the database
        public String getISBNfromBook(int selectedBook){
            return ISBNs.getItemAt(selectedBook);
        }
    }

    //GETTERS AND SETTERS

    //Get the list of authors from the jcombobox
    public ComboBoxModel<String> getAuthorComboList(){
        return authorComboBox.getModel();
    }

    //Get the users in the users table
    public DefaultTableModel getUsersTable(){
        return (DefaultTableModel)usersTable.getModel();
    }

    //Set the users in the users table
    public void setUsersTable(DefaultTableModel tbl, String[] columnHeaders){
        //Set the headers
        tbl.setColumnIdentifiers(columnHeaders);
        usersTable.setModel(tbl);

        //Remove the ID column
        if(tbl.getRowCount() > 0) {
            TableColumnModel tcm = usersTable.getColumnModel();
            tcm.removeColumn(tcm.getColumn(0));
        }
    }

    //Set the index table
    public void setIndexTable(DefaultTableModel tbl, String[] columnHeaders){
        //Set the headers
        tbl.setColumnIdentifiers(columnHeaders);
        retrievalTable.setModel(tbl);

        //Remove the ID column
        if(tbl.getRowCount() > 0) {
            TableColumnModel tcm = retrievalTable.getColumnModel();
            tcm.removeColumn(tcm.getColumn(0));
        }
    }

    //Set the search table
    public void setSearchTable(DefaultTableModel tbl){
        searchTable.setModel(tbl);
        //Remove the ID column
        if(tbl.getRowCount() > 0) {
            TableColumnModel tcm = searchTable.getColumnModel();
            tcm.removeColumn(tcm.getColumn(0));
        }
    }

    //Get the search table
    public DefaultTableModel getSearchTable(){
        return (DefaultTableModel)searchTable.getModel();
    }

    //Set the subject combobox
    public void setSubjectComboBox(ComboBoxModel<String> model){
        subjectComboBox.setModel(model);
    }

    //Get the subject combo box model
    public ComboBoxModel<String> getSubjectComboBox(){
        return subjectComboBox.getModel();
    }

    //Get the selected item from the subject combo box
    public String getSubjectComboBoxSelected(){
        return subjectComboBox.getSelectedItem().toString();
    }

    //Get the subject combobox itself
    public JComboBox getSubjectBoxObject(){
        return subjectComboBox;
    }

    //Get the author combobox itself
    public JComboBox getAuthorBoxObject(){
        return authorComboBox;
    }

    //Set the author combobox
    public void setAuthorComboBox(ComboBoxModel<String> model){
        authorComboBox.setModel(model);
    }

    //Get the author combobox model
    public ComboBoxModel<String> getAuthorComboBox(){
        return authorComboBox.getModel();
    }

    //Get the selected item in the author combobox
    public String getAuthorComboBoxSelected(){
        return authorComboBox.getSelectedItem().toString();
    }

    //Set the users table border green for when editing
    public void setUsersBorderGreen(){
        userTableScrollPane.setBorder(greenBorder);
    }

    //Enable or disable the users table for editing
    public void enableDisableUsersTable(boolean enable){
        usersTable.setEnabled(enable);
    }

    //Remove the green border
    public void removeUsersBorder(){
        userTableScrollPane.setBorder(emptyBorder);
    }

    //Return the open tab for the help dialog
    public int getOpenTab(){
        return libraryTabbedPane.getSelectedIndex();
    }

}//end class
