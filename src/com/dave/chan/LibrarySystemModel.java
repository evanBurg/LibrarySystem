package com.dave.chan;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
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

    /**
     * Public constructor
     */
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

    /**
     * Updates the authors member and then returns the data
     * @return authors The authors for a combobox
     */
    public DefaultComboBoxModel<String> getAuthors(){
        getAllAuthors();
        return authors;
    }

    /**
     * Updates the borrowers member and then returns the data
     * @return users The borrowers
     */
    public DefaultTableModel getUsers(){
        getAllBorrowers();
        return users;
    }

    /**
     * Updates the overdue books member and then returns the data
     * @return overdueBooks The overdue books
     */
    public DefaultTableModel getOverdue()
    {
    	getOverdueBooks();
    	return overdueBooks;
    }

    /**
     * Updates the loaned books member and then returns the data
     * @return loans The loaned books
     */
    public DefaultTableModel getLoans(){
        getAllLoanedBooks();
        return loans;
    }

    /**
     * Updates the books member and then returns the data
     * @return books All books in the library
     */
    public DefaultTableModel getBooks(){
        getAllBooks();
        return books;
    }

    /**
     * Method to close the database connection
     */
    public void closeConnection(){
        try {
            if (!connection.isClosed())
                connection.close();
        }catch (SQLException e){
            throwError(e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Error handling
     * @param message The error message
     */
    public void throwError(String message){
        JFrame frame = new JFrame();
        JOptionPane.showMessageDialog(frame,
                message,
                "Error",
                JOptionPane.ERROR_MESSAGE);
    }

    /**
     * Displays a help dialog for a given tab number
     * @param tabOpen Which tab is open
     */
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

    /**
     * Converts the available column from the string "1" or "0" to "Yes" or "No"
     * @param tableModel The table model to convert the strings from
     * @return A table model with the strings converted
     */
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

    /**
     * Converts a result set into a table model
     * @param rs The result set to convert
     * @return A table model representing the result set
     */
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

    /**
     * Gets all books from the database and places them in the member variable
     */
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

    /**
     * Gets all loaned books from the database and places them in the member variable
     */
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

    /**
     * Gets all subjects from the database and places them in the member variable
     */
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

    /**
     * Gets all authors from the database and places them in the member variable
     */
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

    /**
     * Gets all books from the database by a specific subject
     * @param subject The subject to search by
     */
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

    /**
     * Gets all books from the database by a specific subject and author
     * @param author The author to search by
     * @param subject The subject to search by
     */
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

    /**
     * Gets all books from the database by a specific author
     * @param author The subject to search by
     */
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

    /**
     * Method to check in or out a book
     * @param isCheckingOut Whether we are checking in or out the book
     * @param ISBN The book's ISBN (Unique identifier)
     * @param BorrowerId The person to check in or out the book
     * @return Whether or not the operation was successful.
     */
    public boolean checkABookInorOut(boolean isCheckingOut, String ISBN, int BorrowerId){
        Statement query = null;
        ResultSet bookID = null;
        try{
        	//query to get our specific book's ID
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

    /**
     * Get all borrowers from the database and place them in the member variable
     */
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

    /**
     * Method to update the borrowers in the database, since you can edit multiple rows in the table at a time it takes a whole table model
     * @param model The information to put in the database
     * @return Whether the operation was succesful or not
     */
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

    /**
     * Method to add a new borrower to the database
     * @param first The first name
     * @param last The last name
     * @param email The email
     * @return Whether or not the operation was successful
     */
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

    /**
     * Method to get all overdue books and set the member variable
     */
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

    /**
     * Method for adding a new author
     * @param first_name Their first name
     * @param last_name Their last name
     * @return Whether the operation was successful or not
     */
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

    /**
     * Method to add a new book
     * @param title The book title
     * @param isbn The book ISBN
     * @param edition The book edition
     * @param subject The book subject
     * @param Authors The book authors
     * @return Whether or not the operation was successful
     */
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
