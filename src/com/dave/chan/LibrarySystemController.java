package com.dave.chan;

/**
 * Program Name: LibrarySystemController.java
 * Purpose: A controller class between our model and view classes that will register user
 * 			interaction and update or retrieve information from the model to display to the view.
 * Coder: Evan Burgess, David Harris
 * Date: July 25th, 2018
 */

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.util.List;


public class LibrarySystemController
{
    //CLASS DATA MEMBERS are objects of the model and view classes
    private LibrarySystemView theView;
    private LibrarySystemModel theModel;

    //constructor will take as arguments objects of the View and the Model
    public LibrarySystemController(LibrarySystemView view, LibrarySystemModel model)
    {
        this.theView = view;
        this.theModel = model;
        
        loadUsers();
        loadBooks();
        loadAuthors();
        loadSubject();
        
        //append a listener object to our view components
        ActionListener listener = new BookListener();
        theView.addListener(listener);
    }//end constructor

    //method to load users from the model 
    private void loadUsers() {
        //names for the column headings
        String[] userColNames = {"ID", "First Name", "Last Name", "E-mail"};
        //get user info from the db
        DefaultTableModel users = theModel.getUsers();
        if(users.getRowCount() > 0)
            theView.setUsersTable(users, userColNames);
        else
            theView.setUsersTable(new DefaultTableModel(), userColNames);
    }
    
    //method to load books info from the model
    private void loadBooks(){
        String[] bookColNames = {"ID", "Title", "ISBN", "Edition", "Subject", "Available"};
        //review book information from model
        DefaultTableModel books = theModel.getBooks();
        if(books.getRowCount() > 0)
            theView.setIndexTable(books, bookColNames);
        else
            theView.setIndexTable(new DefaultTableModel(), bookColNames);
    }
    
    //method to display loan information from the model
    private void loadLoans(){
        String[] loanColNames = {"ID", "Title", "ISBN", "Edition", "Subject", "Comment", "Date Due", "First", "Last"};
        //get our loan information from the model
        DefaultTableModel loans = theModel.getLoans();
        if(loans.getRowCount() > 0)
            theView.setIndexTable(loans, loanColNames);
        else
            theView.setIndexTable(new DefaultTableModel(), loanColNames);
    }
    
    //method for loading our overdue information from the model
    private void loadOverdue(){
        String[] loanColNames = {"ID", "Title", "ISBN", "Edition", "Subject", "Comment", "Date Due", "First", "Last"};
        DefaultTableModel loans = theModel.getOverdue();
        if(loans.getRowCount() > 0)
            theView.setIndexTable(loans, loanColNames);
        else
            theView.setIndexTable(new DefaultTableModel(), loanColNames);
    }

    //we load our combo box with subjects from the model
    public void loadSubject(){
        theView.setSubjectComboBox(theModel.getAllSubjects());
    }
    
    //we load our combo box with authors from the model
    public void loadAuthors(){
        theView.setAuthorComboBox(theModel.getAuthors());
    }
    
    //we update our model using our view jtable information
    public void updateUsers(){
        theModel.updateBorrowers(theView.getUsersTable());
    }
    
    //we pass in the pertinent information to our model it needs to create a new user entry
    public void addNewUser(String first, String last, String email){
        theModel.addNewBorrower(first, last, email);
        theView.addUserDialog.clearFields();
    }

    //method to search through our book index based on the user's view interactions
    public void searchBooks(){
        TableColumnModel tcm = null;
        //if our comboboxes haven't changed from their default choices, we just return an empty table
        if(theView.getAuthorComboBoxSelected().equals("Choose an Author") && theView.getSubjectComboBoxSelected().equals("Choose a Subject")) {
            theView.setSearchTable(new DefaultTableModel());
            return;
        }
        //if the subject combobox is still at it's default value we use the author's name from the other combobox 
        //as the search predicate
        if(theView.getSubjectComboBoxSelected().equals("Choose a Subject")) {
            theView.setSearchTable(theModel.getBooksbyAuthor(theView.getAuthorComboBoxSelected()));
        }
        //otherwise, if the author combobox is still at its default value, we use the subject name as the predicate to search
        else if(theView.getAuthorComboBoxSelected().equals("Choose an Author")) {
            theView.setSearchTable(theModel.getBooksbySubject(theView.getSubjectComboBoxSelected()));
        }
        //if the user has selected values from both comboboxes, we use them in tandem as our search predicates to update
        //our view jtable
        else if(!theView.getAuthorComboBoxSelected().equals("Choose an Author") && !theView.getSubjectComboBoxSelected().equals("Choose a Subject")) {
            theView.setSearchTable(theModel.getBooksByAuthorAndSubject(theView.getAuthorComboBoxSelected(), theView.getSubjectComboBoxSelected()));
        }
    }

    //listener class to what occurs for each component's specific action even in the view
    private class BookListener implements ActionListener
    {
        @Override
        public void actionPerformed(ActionEvent e)
        {
            if(e.getSource().equals(theView.usersSaveButton)){
            	//we use our update and load method to save our new user information and dispaly it
                updateUsers();
                loadUsers();
                theView.removeUsersBorder();
                
                //just so they can't continue to save if they have just saved
                theView.usersSaveButton.setEnabled(false);
                theView.usersNewButton.setEnabled(true);
                
                //reset our table to uneditable while they are not performing an edit
                theView.enableDisableUsersTable(false);
            }
            if(e.getSource().equals(theView.usersUpdateButton)){
            	
            	//we give the user a green border around the jtable to indicate it is editable
                theView.setUsersBorderGreen();
                
                //save button may be used 
                theView.usersSaveButton.setEnabled(true);
                
                //can't add a new user while editing
                theView.usersNewButton.setEnabled(false);
                
                //allow the table to be edited for new user information
                theView.enableDisableUsersTable(true);
            }
            if(e.getSource().equals(theView.usersNewButton)){
            	//we open our user dialog view object
                theView.addUserDialog.setVisible(true);
            }
            if(e.getSource().equals(theView.addUserDialog.addUserDialogButton)){
            	//if our user dialog button is hit, we hid our user form and use our add new user method to create a new entry
            	//reload our user list to display it through the loadUsers() method

                //Validate Email
                if(!theView.addUserDialog.getEmail().matches("^((([!#$%&'*+\\-/=?^_`{|}~\\w])|([!#$%&'*+\\-/=?^_`{|}~\\w][!#$%&'*+\\-/=?^_`{|}~\\.\\w]{0,}[!#$%&'*+\\-/=?^_`{|}~\\w]))[@]\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*)$")){
                    theModel.throwError("Invalid Email! Please enter a valid email address");
                    return;
                }

                theView.addUserDialog.setVisible(false);
                addNewUser(theView.addUserDialog.getFirstName(), theView.addUserDialog.getLastName(), theView.addUserDialog.getEmail());
                loadUsers();
            }
            if(e.getSource().equals(theView.getSubjectBoxObject()) || e.getSource().equals(theView.getAuthorBoxObject())){
            	//if the user interacts with either combobox we search our list of books 
                searchBooks();
            }
            //if the user selects either the check out or in button we go into this block
            if(e.getSource().equals(theView.searchCheckOutBtn) || e.getSource().equals(theView.searchCheckInBtn)){
            	
            	//we get our user and book information from the view and the model
                TableModel books = theView.getSearchTable();
                TableModel users = theModel.getUsers();
                
                //comboboxmodel of strings to hold relevant information
                DefaultComboBoxModel<String> chosenBooks = new DefaultComboBoxModel<String>();
                DefaultComboBoxModel<String> bookISBNs = new DefaultComboBoxModel<String>();
                DefaultComboBoxModel<String> borrowers = new DefaultComboBoxModel<String>();
                DefaultComboBoxModel<String> borrowersIds = new DefaultComboBoxModel<String>();
                
                //parse through our user tablemodel to populate our combobox models with the user information
                for(int i = 0; i < users.getRowCount(); i++){
                    borrowersIds.addElement(users.getValueAt(i, 0).toString());
                    borrowers.addElement(users.getValueAt(i, 1).toString());
                }
                
                //if we selected the checkout button we go into this block
                if(e.getSource().equals(theView.searchCheckOutBtn)) {
                	//loop through the books model and determine whether the book is available for checkout or not
                    for(int i = 0; i < books.getRowCount(); i++){
                        if(books.getValueAt(i, 5).toString().equals("Yes")) {
                            bookISBNs.addElement(books.getValueAt(i, 2).toString());
                            chosenBooks.addElement(books.getValueAt(i, 1).toString());
                        }
                    }
                    if(chosenBooks.getSize() == 0)
                        theModel.throwError("There are no books available for you to check out with your search criteria");
                    else
                        theView.loanDialog.openLoanDialog(true, chosenBooks, theView.getSearchTable(), bookISBNs, borrowers, borrowersIds);
                }else {
                	//for books not abailable to be checked out we enter this block
                    for(int i = 0; i < books.getRowCount(); i++){
                        if(books.getValueAt(i, 5).toString().equals("No")) {
                            bookISBNs.addElement(books.getValueAt(i, 2).toString());
                            chosenBooks.addElement(books.getValueAt(i, 1).toString());
                        }
                    }

                    borrowers = new DefaultComboBoxModel<String>();
                    borrowersIds = new DefaultComboBoxModel<String>();

                    if(chosenBooks.getSize() == 0) {
                        theModel.throwError("There are no books available for you to check in with your search criteria");
                    }else {
                        String[] first = theModel.getBorrowersByISBN(bookISBNs.getElementAt(0), chosenBooks.getElementAt(0));
                        if(first != null) {
                            borrowers.addElement(first[0]);
                            borrowersIds.addElement(first[1]);
                        }
                        theView.loanDialog.openLoanDialog(false, chosenBooks, theView.getSearchTable(), bookISBNs, borrowers, borrowersIds);
                    }
                }
            }
            if(e.getSource().equals(theView.loanDialog.booksToChooseFrom) && !theView.loanDialog.isLoaning){
                DefaultComboBoxModel<String> borrowers = new DefaultComboBoxModel<String>();
                DefaultComboBoxModel<String> borrowersIds = new DefaultComboBoxModel<String>();
                String[] first = theModel.getBorrowersByISBN(theView.loanDialog.getISBNfromBook(theView.loanDialog.getSelectedBookID()), theView.loanDialog.getSelectedBookTitle());
                if(first != null) {
                    borrowers.addElement(first[0]);
                    borrowersIds.addElement(first[1]);
                }
                theView.loanDialog.openLoanDialog(false, theView.loanDialog.getBooksModel(), theView.getSearchTable(), theView.loanDialog.getISBNsModel(), borrowers, borrowersIds);
            }
            if(e.getSource().equals(theView.loanDialog.acceptButton)){
            	//when we hit the accept button determine our sleected book and switch their flags to -1(false)
                int selectedBook = -1;
                selectedBook = theView.loanDialog.getSelectedBookID();
                int selectedBorrower = -1;
                selectedBorrower = theView.loanDialog.getSelectedBorrowerID();
                selectedBorrower = theView.loanDialog.getDatabaseBorrowerID(selectedBorrower);
                String selectedISBN = theView.loanDialog.getISBNfromBook(selectedBook);
                int loanPeriod = -1;

                if(theView.loanDialog.getSelectedLoanPeriod() == 0)
                    loanPeriod = 7;
                else if(theView.loanDialog.getSelectedLoanPeriod() == 1)
                    loanPeriod = 14;
                else if(theView.loanDialog.getSelectedLoanPeriod() == 2)
                    loanPeriod = 21;


                //we then use our model method to update our database with the newly checked in or out books 
                theModel.checkABookInorOut(theView.loanDialog.isLoaning, selectedISBN, selectedBorrower, loanPeriod);
                searchBooks();
                
                //hide our dialog window
                theView.loanDialog.setVisible(false);
            }
            
            //on our retrieval tab if the user selects one of these three buttons we call the
            //relevant method to display the information from the model to the view jtable
            if(e.getSource().equals(theView.retrievalBooksButton)) {
            	loadBooks();
            }
            if(e.getSource().equals(theView.retrievalBooksOnLoanButton)) {
            	loadLoans();
            }
            if(e.getSource().equals(theView.retrievalOverdueButton)) {
            	loadOverdue();
            }
            //when the user selects the add book button
            //we populate a comboboxmodel with authors and remove the 0th element 
            //we then populate the view with this information and open the book dialog window
            if(e.getSource().equals(theView.addBookBtn)) {
                ComboBoxModel<String> authors = theModel.getAuthors();
                theView.addBookDialog.setAddBookAuthorList((DefaultComboBoxModel<String>)authors);
            	theView.addBookDialog.setVisible(true);
            }
            if(e.getSource().equals(theView.addBookDialog.addAuthorBtn)) {
            	//if our textfield doesn't have the placeholder text, or is not empty we enter this block
            	if(theView.addBookDialog.getAddAuthorText() != "Dickens, Charles" || theView.addBookDialog.addAuthorTxtFld.getText().isEmpty())
            	{
            		//get the text from the field and split it based on the comma so we get the first and last name of the author in 
            		//separate variables
            		String author = theView.addBookDialog.getAddAuthorText();
                	String name[] = author.split("\\s*,\\s*");
                    String first = "";
                    String last = "";

                    //make sure the author's name is in the correct format based on the string split above this comment
                    if(name.length == 2) {
                        first = name[1];
                        last = name[0];
                    }else{
                        theModel.throwError("Invalid Author Name.\nName must be Last and First separated by a comma");
                        return;
                    }
                    
                    //update our model with the new author
                    theModel.addNewAuthor(first, last);
                    
                    //we update our combobox author list with this new author so the user may select them 
                    ComboBoxModel<String> authors = theModel.getAuthors();
                    theView.addBookDialog.setAddBookAuthorList((DefaultComboBoxModel<String>)authors);
            	}
            }
            //when the user hits the add book button we enter this block
            if(e.getSource().equals(theView.addBookDialog.addBookButton))
            {
            	//populate our variables with the user's input from the view
            	String title = theView.addBookDialog.addBookTitle.getText();
            	String isbn = theView.addBookDialog.addBookISBN.getText();
            	String sEdition = theView.addBookDialog.getBookEdition();
            	int edition = -1;
            	String subject = theView.addBookDialog.getSubject();
            	List<String> authors = theView.addBookDialog.getAuthorsSelectedList();

            	//Error checking

                //ISBN must be 13 chars long
            	if(isbn.length() != 13){
            	    theModel.throwError("Invalid ISBN! Must be a 13 digit long number.\nYour ISBN has " + isbn.length() + " characters.");
            	    return;
                }

                //ISBN must be only numbers
                if(!isbn.matches("\\d+")){
                    theModel.throwError("Invalid ISBN! Must be a 13 digit long number.\nYour ISBN contains invalid characters.");
                    return;
                }

                //Edition must be integer parseable
                try {
                    edition = Integer.parseInt(sEdition);
                } catch (NumberFormatException ex) {
                    theModel.throwError("Invalid Edition! Must be an integer");
                    return;
                }
            	
            	//call our models add new book method and bass in these variables
            	theModel.addNewBook(title,  isbn, edition, subject, authors);
            	
            	//reload our view with the model's new information
            	loadBooks();
            	loadAuthors();
            	loadSubject();

            	theView.addBookDialog.clearFields();
            	//hide our book dialog as it's task has been performed
            	theView.addBookDialog.setVisible(false);
            }
            //close the application if we select the exit option
            if(e.getSource().equals(theView.exit)){
                theView.dispatchEvent(new WindowEvent(theView, WindowEvent.WINDOW_CLOSING));
            }
            //call our model help method relative to our view's current tabbed pane to guide the user
            if(e.getSource().equals(theView.help)){
                theModel.displayHelp(theView.getOpenTab());
            }
            
        }

    }//end inner class

}//end class
