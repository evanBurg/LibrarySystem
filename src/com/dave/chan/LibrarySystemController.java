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

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;
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
        loadAuthors();
        loadSubject();

        ActionListener listener = new BookListener();
        theView.addListener(listener);
    }//end constructor

    private void loadUsers() {
        // TableModel definition
        String[] userColNames = {"ID", "First Name", "Last Name", "E-mail"};
        DefaultTableModel users = theModel.getUsers();
        users.setColumnIdentifiers(userColNames);
        theView.usersTable.setModel(users);
        TableColumnModel tcm =  theView.usersTable.getColumnModel();
        tcm.removeColumn( tcm.getColumn(0) );
    }

    private void loadBooks(){
        String[] bookColNames = {"ID", "Title", "ISBN", "Edition", "Subject", "Available"};
        DefaultTableModel books = theModel.getBooks();
        books.setColumnIdentifiers(bookColNames);
        theView.retrievalTable.setModel(books);
        TableColumnModel tcm =  theView.retrievalTable.getColumnModel();
        tcm.removeColumn( tcm.getColumn(0) );
    }

    private void loadLoans(){
        String[] loanColNames = {"ID", "Title", "ISBN", "Edition", "Subject", "Comment", "First", "Last"};
        DefaultTableModel loans = theModel.getLoans();
        loans.setColumnIdentifiers(loanColNames);
        if(loans.getRowCount() > 0) {
            theView.retrievalTable.setModel(loans);
            TableColumnModel tcm = theView.retrievalTable.getColumnModel();
            tcm.removeColumn(tcm.getColumn(0));
        }
    }
    
    private void loadOverdue(){
        String[] loanColNames = {"ID", "Title", "ISBN", "Edition", "Subject", "Comment", "First", "Last"};
        DefaultTableModel loans = theModel.getOverdue();
        loans.setColumnIdentifiers(loanColNames);
        if(loans.getRowCount() > 0) {
            theView.retrievalTable.setModel(loans);
            TableColumnModel tcm = theView.retrievalTable.getColumnModel();
            tcm.removeColumn(tcm.getColumn(0));
        }
    }

    public void loadSubject(){
        theView.subjectComboBox.setModel(theModel.getAllSubjects());
    }

    public void loadAuthors(){
        theView.authorComboBox.setModel(theModel.getAuthors());
    }

    public void updateUsers(){
        theModel.updateBorrowers((DefaultTableModel)theView.usersTable.getModel());
    }

    public void addNewUser(String first, String last, String email){
        theModel.addNewBorrower(first, last, email);
    }

    public void searchBooks(){
        TableColumnModel tcm = null;


        if(theView.authorComboBox.getSelectedItem().toString().equals("Choose an Author") && theView.subjectComboBox.getSelectedItem().toString().equals("Choose a Subject")) {
            theView.searchTable.setModel(new DefaultTableModel());
            return;
        }

        if(theView.subjectComboBox.getSelectedItem().toString().equals("Choose a Subject")) {
            theView.searchTable.setModel(theModel.getBooksbyAuthor(theView.authorComboBox.getSelectedItem().toString()));
            tcm = theView.searchTable.getColumnModel();
            tcm.removeColumn( tcm.getColumn(0) );
        }else if(theView.authorComboBox.getSelectedItem().toString().equals("Choose an Author")) {
            theView.searchTable.setModel(theModel.getBooksbySubject(theView.subjectComboBox.getSelectedItem().toString()));
            tcm = theView.searchTable.getColumnModel();
            tcm.removeColumn( tcm.getColumn(0) );
        }else if(!theView.authorComboBox.getSelectedItem().toString().equals("Choose an Author") && !theView.subjectComboBox.getSelectedItem().toString().equals("Choose a Subject")) {
            theView.searchTable.setModel(theModel.getBooksByAuthorAndSubject(theView.authorComboBox.getSelectedItem().toString(), theView.subjectComboBox.getSelectedItem().toString()));
            tcm = theView.searchTable.getColumnModel();
            tcm.removeColumn( tcm.getColumn(0) );
        }
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
                searchBooks();
            }
            if(e.getSource().equals(theView.searchCheckOutBtn) || e.getSource().equals(theView.searchCheckInBtn)){
                TableModel books = theView.searchTable.getModel();
                TableModel users = theModel.getUsers();
                DefaultComboBoxModel<String> chosenBooks = new DefaultComboBoxModel<String>();
                DefaultComboBoxModel<String> bookISBNs = new DefaultComboBoxModel<String>();
                DefaultComboBoxModel<String> borrowers = new DefaultComboBoxModel<String>();
                DefaultComboBoxModel<String> borrowersIds = new DefaultComboBoxModel<String>();
                for(int i = 0; i < users.getRowCount(); i++){
                    borrowersIds.addElement(users.getValueAt(i, 0).toString());
                    borrowers.addElement(users.getValueAt(i, 1).toString());
                }
                if(e.getSource().equals(theView.searchCheckOutBtn)) {
                    for(int i = 0; i < books.getRowCount(); i++){
                        if(books.getValueAt(i, 5).toString().equals("1")) {
                            bookISBNs.addElement(books.getValueAt(i, 2).toString());
                            chosenBooks.addElement(books.getValueAt(i, 1).toString());
                        }
                    }
                    theView.loanDialog.openLoanDialog(true, chosenBooks, theView.searchTable.getModel(), bookISBNs, borrowers, borrowersIds);
                }else {
                    for(int i = 0; i < books.getRowCount(); i++){
                        if(books.getValueAt(i, 5).toString().equals("0")) {
                            bookISBNs.addElement(books.getValueAt(i, 2).toString());
                            chosenBooks.addElement(books.getValueAt(i, 1).toString());
                        }
                    }
                    theView.loanDialog.openLoanDialog(false, chosenBooks, theView.searchTable.getModel(), bookISBNs, borrowers, borrowersIds);
                }
            }
            if(e.getSource().equals(theView.loanDialog.acceptButton)){
                int selectedBook = -1;
                selectedBook = theView.loanDialog.booksToChooseFrom.getSelectedIndex();
                int selectedBorrower = -1;
                selectedBorrower = theView.loanDialog.borrowers.getSelectedIndex();
                selectedBorrower = Integer.parseInt(theView.loanDialog.borrowersIds.getItemAt(selectedBorrower));
                theModel.checkABookInorOut(theView.loanDialog.isLoaning, theView.loanDialog.ISBNs.getItemAt(selectedBook), selectedBorrower);
                searchBooks();
                theView.loanDialog.setVisible(false);
            }
            if(e.getSource().equals(theView.retrievalBooksButton)) {
            	loadBooks();
            }
            if(e.getSource().equals(theView.retrievalBooksOnLoanButton)) {
            	loadLoans();
            }
            if(e.getSource().equals(theView.retrievalOverdueButton)) {
            	loadOverdue();
            }
            if(e.getSource().equals(theView.addBookBtn)) {
            	theView.addBookDialog.setVisible(true);
            }
            
        }

    }//end inner class

}//end class
