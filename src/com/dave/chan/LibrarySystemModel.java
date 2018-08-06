package com.dave.chan;

import javax.swing.*;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.sql.*;
import java.util.List;
import java.util.Vector;

/**
 * Program Name: LibrarySystemModel.java
 * Purpose: Our model class, it will house data sent in by the library database as well as
 * 			make alterations / updates to that data via the controller based on the user's
 * 			input in the view.
 * Coder: Evan Burgess, David Harris
 * Date: Jul 12, 2016
 */

public class LibrarySystemModel
{
	//Class wide scope members
    private DefaultTableModel books;
    private DefaultTableModel loans;
    private DefaultTableModel users;
    private DefaultComboBoxModel<String> authors;
    private DefaultTableModel overdueBooks;
    private Connection connection;

    public LibrarySystemModel(){
    	//try to establish our db connection as well as load in our initial data through these methods
        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/info5051_books?useSSL=false&useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=EST5EDT", "root", "password");
            getAuthors();
            getUsers();
            getLoans();
            getBooks();
        } catch (SQLException e){
            throwError(e.getMessage());
            e.printStackTrace();
        }
    }
    
    //public method calls for the controller to implement
    public DefaultComboBoxModel<String> getAuthors(){
        getAllAuthors();
        return authors;
    }

    public DefaultTableModel getUsers(){
        getAllBorrowers();
        return users;
    }
    
    public DefaultTableModel getOverdue()
    {
    	getOverdueBooks();
    	return overdueBooks;
    }

    public DefaultTableModel getLoans(){
        getAllLoanedBooks();
        return loans;
    }

    public DefaultTableModel getBooks(){
        getAllBooks();
        return books;
    }
    
    //how we close our db connection
    public void closeConnection(){
        try {
            if (!connection.isClosed())
                connection.close();
        }catch (SQLException e){
            throwError(e.getMessage());
            e.printStackTrace();
        }
    }
    
    //some generic error handling
    private void throwError(String message){
        JFrame frame = new JFrame();
        JOptionPane.showMessageDialog(frame,
                message,
                "Error",
                JOptionPane.ERROR_MESSAGE);
    }
    
    //displays help for the user in the view
    public void displayHelp(int tabOpen){
        JFrame frame = new JFrame();
        String message = "";
        
        //specific explanations of the application depending on their selected tab
        if(tabOpen == 0){
            message = "On the Users tab you are able to see all the Borrowers in our system!\nIf you would like to update a borrowers information you may click 'Unlock Table for Editing' and the edit the table as required, afterwards hit 'Save'.\nTo add a new Borrower, press the 'Add New User' button.";
        }else if(tabOpen == 1){
            message = "On the Index tab you are able to see our Book index, our Loan index, and our Overdue index.\nIf you would like to add a new book press the 'Add New Book' button.";
        }else if(tabOpen == 2){
            message = "On the Search tab you are able to search our index for a book you might like.\nUse the Author and Subject drop downs to find something to your liking!\nAfter searching for a book you may check it in or out using the associate buttons.";
        }
        //display it
        JOptionPane.showMessageDialog(frame,
                message,
                "Help",
                JOptionPane.INFORMATION_MESSAGE);
    }
    
    //this will take our boolean available column and alter it to more user friendly text
    private DefaultTableModel convertAvailabletoTrueorFalse(DefaultTableModel tableModel){
        for (int i = 0; i < tableModel.getRowCount(); i++){
            Object element = tableModel.getValueAt(i, 5);
            if(element.toString().equals("1"))
                element = "Yes";
            else if(element.toString().equals("0"))
                element = "No";
            tableModel.setValueAt(element, i, 5);
        }

        return tableModel;
    }
    
    //a method that will parse through a resultset object and return a defaultablemodel object
    private DefaultTableModel returnTableModelFromResultSet(ResultSet rs) {
        try {
            ResultSetMetaData metaData = rs.getMetaData();
            int numberOfColumns = metaData.getColumnCount();
            Vector columnNames = new Vector();

            // Get the column names
            for (int column = 0; column < numberOfColumns; column++) {
                columnNames.addElement(metaData.getColumnLabel(column + 1));
            }

            // Get all rows.
            Vector rows = new Vector();

            while (rs.next()) {
                Vector newRow = new Vector();

                for (int i = 1; i <= numberOfColumns; i++) {
                    newRow.addElement(rs.getObject(i));

                }

                rows.addElement(newRow);
            }

            return new DefaultTableModel(rows, columnNames);
        } catch (Exception e) {
            throwError(e.getMessage());
            e.printStackTrace();

            return null;
        }
    }
    
    //method to return all books
    private void getAllBooks(){
        Statement query = null;
        ResultSet booksrs = null;
        
        //sql query to select all books
        try{
            query = connection.createStatement();
            booksrs = query.executeQuery("SELECT * FROM book");
            
            //convert rs to defaulttablemodel
            books = returnTableModelFromResultSet(booksrs);
            
            //take our available column and style it
            books = convertAvailabletoTrueorFalse(books);
            
            //close our resultset and query objects
            if(booksrs != null)
                booksrs.close();
            if(query != null)
                query.close();
        }catch (Exception ex){
            throwError(ex.getMessage());
            System.out.println("Exception: " + ex.getMessage());
            ex.printStackTrace();
        }
    }
    
    //method to get all books currently on loan
    private void getAllLoanedBooks(){
        Statement query = null;
        ResultSet books = null;
        
        //sql query on the db to return books that are 'false' in their availability
        try{
            query = connection.createStatement();
            books = query.executeQuery("SELECT BookID, Title, ISBN, Edition_Number, Subject, Comment, Date_Due, First_Name, Last_Name FROM book bks INNER JOIN book_loan bkln ON bks.BookID = bkln.Book_BookID INNER JOIN borrower brwr ON bkln.Borrower_Borrower_ID = brwr.Borrower_ID WHERE Available = 0 AND date_returned IS NULL ORDER BY Last_Name");
            
            //convert to default table model
            loans = returnTableModelFromResultSet(books);


            if(books != null)
                books.close();
            if(query != null)
                query.close();
        }catch (Exception ex){
            throwError(ex.getMessage());
            System.out.println("Exception: " + ex.getMessage());
            ex.printStackTrace();
        }
    }
    
    //method to get all book subjects from the db
    public DefaultComboBoxModel getAllSubjects(){
        Statement query = null;
        ResultSet books = null;
        
        //we only want unique subjects not every instance of a subject
        try{
            query = connection.createStatement();
            books = query.executeQuery("SELECT DISTINCT subject FROM book");
            
            //combobox model as that what this information is being placed in
            DefaultComboBoxModel theBooks = new DefaultComboBoxModel();
            
            //default initial value
            theBooks.addElement("Choose a Subject");
            
            //parse through the resultset and populate our model
            while(books.next()){
                theBooks.addElement(books.getString("subject"));
            }

            if(books != null)
                books.close();
            if(query != null)
                query.close();

            return theBooks;
        }catch (Exception ex){
            throwError(ex.getMessage());
            System.out.println("Exception: " + ex.getMessage());
            ex.printStackTrace();

            return null;
        }
    }
    
    //method to get all author information
    private void getAllAuthors(){
        Statement query = null;
        ResultSet authorsrs = null;

        try{
            query = connection.createStatement();
            //get all author's names - first and last
            authorsrs = query.executeQuery("SELECT first_name, last_name FROM author");

            DefaultComboBoxModel theAuthors = new DefaultComboBoxModel();
            
            //default value
            theAuthors.addElement("Choose an Author");

            
            //populate model with rs information
            //we concatenate the first and last name together
            while(authorsrs.next()){
                theAuthors.addElement(authorsrs.getString("last_name") + ", " + authorsrs.getString("first_name"));
            }

            if(authorsrs != null)
                authorsrs.close();
            if(query != null)
                query.close();

            authors = theAuthors;
        }catch (Exception ex){
            throwError(ex.getMessage());
            System.out.println("Exception: " + ex.getMessage());
            ex.printStackTrace();
        }
    }
    
    //method to return a select number of books from our db based on the subject argument
    public DefaultTableModel getBooksbySubject(String subject){
        Statement query = null;
        ResultSet books = null;

        try{
        	//query to get our specific set of books
            query = connection.createStatement();
            books = query.executeQuery("SELECT * FROM book WHERE Subject = '" + subject + "'");
            
            //convert to defaulttablemodel and then style it's availablity
            DefaultTableModel theBooks = returnTableModelFromResultSet(books);
            theBooks = convertAvailabletoTrueorFalse(theBooks);

            if(books != null)
                books.close();
            if(query != null)
                query.close();

            return theBooks;
        }catch (Exception ex){
            throwError(ex.getMessage());
            System.out.println("Exception: " + ex.getMessage());
            ex.printStackTrace();

            return null;
        }
    }
    
    //method for when the user in the view wants a book based on the author and the subject
    public DefaultTableModel getBooksByAuthorAndSubject(String author, String subject){
        PreparedStatement query = null;
        ResultSet books = null;

        try{
        	//query to get our specific set of books
            query = connection.prepareStatement("SELECT BookID, Title, ISBN, Edition_Number, Subject, Available FROM book bks INNER JOIN book_author bkath ON bks.BookID = bkath.Book_BookID INNER JOIN author athrs ON bkath.Author_AuthorID = athrs.AuthorID WHERE last_name = ? AND first_name = ? AND subject = ?");
            
            //we split apart the author string passed in, in order to separate the first and last name
            String[] name = author.trim().split("\\s*,\\s*");
            String first = name[1];
            String last = name[0];
            
            //prepared statement parameters
            query.setString(1, last);
            query.setString(2, first);
            query.setString(3, subject);
            books = query.executeQuery();
            
            //convert and style
            DefaultTableModel theBooks = returnTableModelFromResultSet(books);
            theBooks = convertAvailabletoTrueorFalse(theBooks);

            if(books != null)
                books.close();
            if(query != null)
                query.close();

            return theBooks;
        }catch (Exception ex){
            throwError(ex.getMessage());
            System.out.println("Exception: " + ex.getMessage());
            ex.printStackTrace();

            return null;
        }
    }
    
    //method to get a list of books based on an author's name
    public DefaultTableModel getBooksbyAuthor(String author){
        PreparedStatement query = null;
        ResultSet books = null;

        try{
        	//sql query for that specific list used in a prepared statement to account for the author argument
            query = connection.prepareStatement("SELECT BookID, Title, ISBN, Edition_Number, Subject, Available FROM book bks INNER JOIN book_author bkath ON bks.BookID = bkath.Book_BookID INNER JOIN author athrs ON bkath.Author_AuthorID = athrs.AuthorID WHERE last_name = ? AND first_name = ?");
            String[] name = author.trim().split("\\s*,\\s*");
            String first = "";
            String last = "";
            
            //make sure the author's name is in the correct format based on the string split above this comment
            if(name.length == 2) {
                first = name[1];
                last = name[0];
            }else{
                throwError("Invalid Author Name");
            }
            query.setString(1, last);
            query.setString(2, first);
            books = query.executeQuery();

            //convert to dtm object and style
            DefaultTableModel theBooks = returnTableModelFromResultSet(books);
            theBooks = convertAvailabletoTrueorFalse(theBooks);


            if(books != null)
                books.close();
            if(query != null)
                query.close();

            return theBooks;
        }catch (Exception ex){
            throwError(ex.getMessage());
            System.out.println("Exception: " + ex.getMessage());
            ex.printStackTrace();

            return null;
        }
    }
    
    //method for checking a book in or out
    public boolean checkABookInorOut(boolean isCheckingOut, String ISBN, int BorrowerId){
        Statement query = null;
        ResultSet bookID = null;
        try{
        	//query to get our specific book as well as it's id
            query = connection.createStatement();
            bookID = query.executeQuery("SELECT BookID FROM Book WHERE ISBN = '"+ ISBN +"'");
            int BookID = 0;
            if(bookID.next())
                BookID = bookID.getInt(1);
            
            //if we are not checking out and in fact checking in
            if(!isCheckingOut) {
                if (BookID != 0)
                	//update our database with the date returned for this specific book 
                    query.executeUpdate("UPDATE book_loan SET date_returned = CURDATE() WHERE Book_BookID =" + BookID);
                	//set the availability back to availabe or 'true'
                    query.executeUpdate("UPDATE Book SET Available = 1 WHERE ISBN = '"+ ISBN +"'");
            }else{
            	//otherwise we assume they are checking out so set the due date and toggle availability to false
                query.executeUpdate("INSERT INTO book_loan(Book_BookID, Borrower_Borrower_ID, Comment, date_out, date_due) VALUES("+BookID+", "+ BorrowerId +", DATE_FORMAT(CURDATE(), 'Borrowed on %M %e, %Y'), CURDATE(), DATE_ADD(CURDATE(), INTERVAL 14 DAY))");
                query.executeUpdate("UPDATE Book SET Available = 0 WHERE ISBN = '"+ ISBN +"'");
            }

            if(bookID != null)
                bookID.close();
            if(query != null)
                query.close();

            return true;
        }catch (Exception ex){
            throwError(ex.getMessage());
            System.out.println("Exception: " + ex.getMessage());
            ex.printStackTrace();
        }
        return false;
    }
    
    //get all borrowing users from the database
    private void getAllBorrowers(){
        Statement query = null;
        ResultSet borrowers = null;
        try{
            query = connection.createStatement();
            //sql query to get the list of users currently borrowing
            borrowers = query.executeQuery("SELECT borrower_id, first_name, last_name, borrower_email FROM borrower ORDER BY last_name");
            
            //copnvert to dtm
            users = returnTableModelFromResultSet(borrowers);

            if(borrowers != null)
                borrowers.close();
            if(query != null)
                query.close();
        }catch (Exception ex){
            throwError(ex.getMessage());
            System.out.println("Exception: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    //method to update our database with new borrowers
    public boolean updateBorrowers(DefaultTableModel model){
        PreparedStatement query = null;

        try{
        	//prepared statement to handle all the parameters sent in by the model argument
            query = connection.prepareStatement("UPDATE borrower SET first_name = ?, last_name = ?, borrower_email = ? WHERE borrower_id = ?");

            //a vector of the users from the model
            Vector<Vector> users = model.getDataVector();
            
            //loop through our users vector and set the prepared statement parameters to the
            //proper index within the vector and add it to our batch
            for(int i = 0; i < users.size(); i++){
                query.setString(1, (String)users.get(i).get(1));
                query.setString(2, (String)users.get(i).get(2));
                query.setString(3, (String)users.get(i).get(3));
                query.setInt(4, (int)users.get(i).get(0));
                query.addBatch();
            }
            
            //run the the sql statement after it has been prepared in the above for loop
            query.executeBatch();

            if(query != null)
                query.close();

            return true;
        }catch (Exception ex){
            System.out.println("Exception: " + ex.getMessage());
            ex.printStackTrace();
            return false;
        }
    }
    
    //method to update our database with new user information
    public boolean addNewBorrower(String first, String last, String email){
        Statement query = null;
        try{
            query = connection.createStatement();
            
            //query that will update our db with the passed in values for the new borrower 
            query.executeUpdate("INSERT INTO borrower (first_name, last_name, borrower_email) VALUES('"+ first +"', '"+ last +"', '"+ email +"')");

            if(query != null)
                query.close();

            return true;
        }catch (Exception ex){
            throwError(ex.getMessage());
            System.out.println("Exception: " + ex.getMessage());
            ex.printStackTrace();
            return false;
        }
    }
    
    //will return a list of the overdue books from our database
    private void getOverdueBooks(){
        Statement query = null;
        ResultSet books = null;

        try{
            query = connection.createStatement();
            
            //query that will return the list of books where their availability is false(unavailable) and the
            //current date is greater than the due date
            books = query.executeQuery(
                    "SELECT BookID, Title, ISBN, Edition_Number, Subject, Comment, Date_Due, First_Name, Last_Name FROM book bks INNER JOIN book_loan bkln ON bks.BookID = bkln.Book_BookID INNER JOIN borrower brwr ON bkln.Borrower_Borrower_ID = brwr.Borrower_ID WHERE Available = 0 AND CURDATE() > date_due AND date_returned IS NULL ORDER BY Last_Name"
            );
            
            //convert result set to dtm
            overdueBooks = returnTableModelFromResultSet(books);

            if(books != null)
                books.close();
            if(query != null)
                query.close();

        }catch (Exception ex){
            throwError(ex.getMessage());
            System.out.println("Exception: " + ex.getMessage());
            ex.printStackTrace();
        }
    }
    
    //method that will update our db with a new author
    public boolean addNewAuthor(String first_name, String last_name){
        PreparedStatement query = null;

        try{
        	//prepared statement that will take our method arguments and insert them into our sql insert
            query = connection.prepareStatement("INSERT INTO Author (first_name, last_name) VALUES (?, ?)");
            query.setString(1, first_name);
            query.setString(2, last_name);
            query.executeUpdate();

            if(query != null)
                query.close();

            return true;
        }catch (Exception ex){
            System.out.println("Exception: " + ex.getMessage());
            ex.printStackTrace();

            return false;
        }
    }
    
    //method that will update our sql database with a new book listing
    public boolean addNewBook(String title, String isbn, int edition, String subject, List<String> Authors){
        PreparedStatement query = null;
        ResultSet authors = null;
        ResultSet book = null;

        try{
        	//prepared statement that will take our method arguments and insert them into our sql insert
            query = connection.prepareStatement("INSERT INTO BOOK (title, isbn, edition_number, subject, available) VALUES (?, ?, ?, ?, true)");
            query.setString(1, title);
            query.setString(2, isbn);
            query.setInt(3, edition);
            query.setString(4, subject);
            query.executeUpdate();
            
            //we then select that specific book again 
            query = connection.prepareStatement("SELECT BookID FROM Book WHERE title = ? AND ISBN = ?");
            query.setString(1, title);
            query.setString(2, isbn);
            book = query.executeQuery();
            
            //if .next() is true we loop through the list of authors and prepare for them to be inserted into our db
            if(book.next()) {
                for (int i = 0; i < Authors.size(); i++) {
                    String Author = Authors.get(i);
                    String split[] = Author.split("\\s*,\\s*");
                    String first = split[1];
                    String last = split[0];

                    //Get the authors id based on the author's name
                    query = connection.prepareStatement("SELECT AuthorID FROM author WHERE first_name = ? AND last_name = ?");
                    query.setString(1, first);
                    query.setString(2, last);
                    authors = query.executeQuery();

                    //If .next() returns false, there are no existing authors with that name
                    if (!authors.next()) {
                        throwError("Could not find specified author: " + last + ", " + first);
                    }else {
                    	//insert into our database the book and author id's
                        query = connection.prepareStatement("INSERT INTO BOOK_AUTHOR (Book_BookID, Author_AuthorID) VALUES (?, ?)");
                        query.setInt(1, book.getInt("BookID"));
                        query.setInt(2, authors.getInt("AuthorID"));
                        query.executeUpdate();
                    }
                }
                if(authors != null)
                    authors.close();
                if(book != null)
                    book.close();
                if (query != null)
                    query.close();

                return true;
            }else{
                return false;
            }
        }catch (Exception ex){
            throwError(ex.getMessage());
            System.out.println("Exception: " + ex.getMessage());
            ex.printStackTrace();

            return false;
        }
    }
}//end class
