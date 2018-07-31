package com.dave.chan;

/**
 * Program Name: LibrarySystemController.java
 * Purpose: this class acts as the controller between the GUI view and the back
 *         end model that stores the data. It will gather the user input from
 *         the view object, send the data to the model object for calculation, and then
 *         take the returned result from the model and send it back to the view object
 *         to display the result.
 * Coder: Bill Pulling for Sec02,ADAPTED from Derek Carnas' online Youtube video
 *        on MVC.
 * Date: Jul 12, 2016
 */

import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


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
        loadLoans();
        loadAuthors();
        loadSubject();

        ActionListener listener = new BookListener();
        theView.addListener(listener);
    }//end constructor

    private void loadUsers() {
        // TableModel definition
        String[] userColNames = {"ID", "First Name", "Last Name", "E-mail"};
        DefaultTableModel users = theModel.getAllBorrowers();
        users.setColumnIdentifiers(userColNames);
        theView.usersTable.setModel(users);
        TableColumnModel tcm =  theView.usersTable.getColumnModel();
        tcm.removeColumn( tcm.getColumn(0) );
    }

    private void loadBooks(){
        String[] bookColNames = {"ID", "Title", "ISBN", "Edition", "Subject", "Available"};
        DefaultTableModel books = theModel.getAllBooks();
        books.setColumnIdentifiers(bookColNames);
        theView.retrievalTable.setModel(books);
        TableColumnModel tcm =  theView.retrievalTable.getColumnModel();
        tcm.removeColumn( tcm.getColumn(0) );
    }

    private void loadLoans(){
        String[] loanColNames = {"ID", "Title", "ISBN", "Edition", "Subject", "Available"};
        DefaultTableModel loans = theModel.getAllLoanedBooks();
        loans.setColumnIdentifiers(loanColNames);
        theView.loansTable.setModel(loans);
        TableColumnModel tcm =  theView.loansTable.getColumnModel();
        tcm.removeColumn( tcm.getColumn(0) );
    }

    public void loadSubject(){
        theView.subjectComboBox.setModel(theModel.getAllSubjects());
    }

    public void loadAuthors(){
        theView.authorComboBox.setModel(theModel.getAllAuthors());
    }

    public void updateUsers(){
        theModel.updateBorrowers((DefaultTableModel)theView.usersTable.getModel());
    }

    public void addNewUser(String first, String last, String email){
        theModel.addNewBorrower(first, last, email);
    }

    //PUT INNER CLASS HERE
    private class BookListener implements ActionListener
    {
        @Override
        public void actionPerformed(ActionEvent e)
        {
            if(e.getSource().equals(theView.usersSaveButton)){
                updateUsers();
                loadUsers();
                theView.usersTable.setEnabled(false);
            }
            if(e.getSource().equals(theView.usersUpdateButton)){
                theView.usersTable.setEnabled(true);
            }
            if(e.getSource().equals(theView.usersNewButton)){
                theView.addUserDialog.setVisible(true);
            }
            if(e.getSource().equals(theView.addUserDialogButton)){
                theView.addUserDialog.setVisible(false);
                addNewUser(theView.addUserDialog.addUserFirstName.getText(), theView.addUserDialog.addUserLastName.getText(), theView.addUserDialog.addUserEmail.getText());
                loadUsers();
            }
            if(e.getSource().equals(theView.authorComboBox) || e.getSource().equals(theView.subjectComboBox)){
                if(theView.subjectComboBox.getSelectedItem().toString().equals("Choose a Subject"))
                    theView.searchTable.setModel(theModel.getBooksbyAuthor(theView.authorComboBox.getSelectedItem().toString()));
                else if(theView.authorComboBox.getSelectedItem().toString().equals("Choose an Author"))
                    theView.searchTable.setModel(theModel.getBooksbySubject(theView.subjectComboBox.getSelectedItem().toString()));
                else
                    theView.searchTable.setModel(theModel.getBooksByAuthorAndSubject(theView.authorComboBox.getSelectedItem().toString(), theView.subjectComboBox.getSelectedItem().toString()));
            }
        }

    }//end inner class

}//end class
