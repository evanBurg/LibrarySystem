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
import java.util.Vector;

/**
 * Program Name: LibrarySystemModel.java
 * Purpose: this class will be the model part of the app.It will manipulate the data
 *          passed to it by the controller and it will also store the result of
 *          the calculation in its private data member.
 * Coder: Bill Pulling for Sec02, ADAPTED from Derek Carnas' online Youtube video
 *        on MVC.
 * Date: Jul 12, 2016
 */

public class LibrarySystemModel
{
    private DefaultTableModel books;
    private DefaultTableModel loans;
    private DefaultTableModel users;
    private DefaultComboBoxModel<String> authors;
    private Connection connection;

    public LibrarySystemModel(){
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

    public DefaultComboBoxModel<String> getAuthors(){
        getAllAuthors();
        return authors;
    }

    public DefaultTableModel getUsers(){
        getAllBorrowers();
        return users;
    }

    public DefaultTableModel getLoans(){
        getAllLoanedBooks();
        return loans;
    }

    public DefaultTableModel getBooks(){
        getAllBooks();
        return books;
    }

    public void closeConnection(){
        try {
            if (!connection.isClosed())
                connection.close();
        }catch (SQLException e){
            throwError(e.getMessage());
            e.printStackTrace();
        }
    }

    private void throwError(String message){
        JFrame frame = new JFrame();
        JOptionPane.showMessageDialog(frame,
                message,
                "Error",
                JOptionPane.ERROR_MESSAGE);
    }

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

    private void getAllBooks(){
        Statement query = null;
        ResultSet booksrs = null;

        try{
            query = connection.createStatement();
            booksrs = query.executeQuery("SELECT * FROM book");

            books = returnTableModelFromResultSet(booksrs);

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

    private void getAllLoanedBooks(){
        Statement query = null;
        ResultSet books = null;

        try{
            query = connection.createStatement();
            books = query.executeQuery("SELECT BookID, Title, ISBN, Edition_Number, Subject, Comment, First_Name, Last_Name FROM book bks INNER JOIN book_loan bkln ON bks.BookID = bkln.Book_BookID INNER JOIN borrower brwr ON bkln.Borrower_Borrower_ID = brwr.Borrower_ID WHERE Available = 0 AND date_returned IS NULL ORDER BY Last_Name");

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

    public DefaultComboBoxModel getAllSubjects(){
        Statement query = null;
        ResultSet books = null;

        try{
            query = connection.createStatement();
            books = query.executeQuery("SELECT DISTINCT subject FROM book");

            DefaultComboBoxModel theBooks = new DefaultComboBoxModel();
            theBooks.addElement("Choose a Subject");

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

    private void getAllAuthors(){
        Statement query = null;
        ResultSet authorsrs = null;

        try{
            query = connection.createStatement();
            authorsrs = query.executeQuery("SELECT first_name, last_name FROM author");

            DefaultComboBoxModel theAuthors = new DefaultComboBoxModel();
            theAuthors.addElement("Choose an Author");


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

    public DefaultTableModel getBooksbySubject(String subject){
        Statement query = null;
        ResultSet books = null;

        try{
            query = connection.createStatement();
            books = query.executeQuery("SELECT * FROM book WHERE Subject = '" + subject + "'");

            DefaultTableModel theBooks = returnTableModelFromResultSet(books);

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

    public DefaultTableModel getBooksByAuthorAndSubject(String author, String subject){
        PreparedStatement query = null;
        ResultSet books = null;

        try{
            query = connection.prepareStatement("SELECT BookID, Title, ISBN, Edition_Number, Subject, Available FROM book bks INNER JOIN book_author bkath ON bks.BookID = bkath.Book_BookID INNER JOIN author athrs ON bkath.Author_AuthorID = athrs.AuthorID WHERE last_name = ? AND first_name = ? AND subject = ?");
            String[] name = author.trim().split("\\s*,\\s*");
            String first = name[1];
            String last = name[0];
            query.setString(1, last);
            query.setString(2, first);
            query.setString(3, subject);
            books = query.executeQuery();

            DefaultTableModel theBooks = returnTableModelFromResultSet(books);

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

    public DefaultTableModel getBooksbyAuthor(String author){
        PreparedStatement query = null;
        ResultSet books = null;

        try{
            query = connection.prepareStatement("SELECT BookID, Title, ISBN, Edition_Number, Subject, Available FROM book bks INNER JOIN book_author bkath ON bks.BookID = bkath.Book_BookID INNER JOIN author athrs ON bkath.Author_AuthorID = athrs.AuthorID WHERE last_name = ? AND first_name = ?");
            String[] name = author.trim().split("\\s*,\\s*");
            String first = "";
            String last = "";
            if(name.length == 2) {
                first = name[1];
                last = name[0];
            }else{
                throwError("Invalid Author Name");
            }
            query.setString(1, last);
            query.setString(2, first);
            books = query.executeQuery();

            DefaultTableModel theBooks = returnTableModelFromResultSet(books);

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

    public boolean checkABookInorOut(boolean isCheckingOut, String ISBN, int BorrowerId){
        Statement query = null;
        ResultSet bookID = null;
        try{
            query = connection.createStatement();
            bookID = query.executeQuery("SELECT BookID FROM Book WHERE ISBN = '"+ ISBN +"'");
            int BookID = 0;
            if(bookID.next())
                BookID = bookID.getInt(1);

            if(!isCheckingOut) {
                if (BookID != 0)
                    query.executeUpdate("UPDATE book_loan SET date_returned = CURDATE() WHERE Book_BookID =" + BookID);
                    query.executeUpdate("UPDATE Book SET Available = 1 WHERE ISBN = '"+ ISBN +"'");
            }else{
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

    private void getAllBorrowers(){
        Statement query = null;
        ResultSet borrowers = null;
        try{
            query = connection.createStatement();
            borrowers = query.executeQuery("SELECT borrower_id, first_name, last_name, borrower_email FROM borrower ORDER BY last_name");

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

    public boolean updateBorrowers(DefaultTableModel model){
        PreparedStatement query = null;

        try{
            query = connection.prepareStatement("UPDATE borrower SET first_name = ?, last_name = ?, borrower_email = ? WHERE borrower_id = ?");


            Vector<Vector> users = model.getDataVector();

            for(int i = 0; i < users.size(); i++){
                query.setString(1, (String)users.get(i).get(1));
                query.setString(2, (String)users.get(i).get(2));
                query.setString(3, (String)users.get(i).get(3));
                query.setInt(4, (int)users.get(i).get(0));
                query.addBatch();
            }

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

    public boolean addNewBorrower(String first, String last, String email){
        Statement query = null;
        try{
            query = connection.createStatement();

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

    public DefaultTableModel getOverdueBooks(){
        Statement query = null;
        ResultSet books = null;

        try{
            books = query.executeQuery(
                    "SELECT * FROM info5051_books.book " +
                        "INNER JOIN info5051_books.book_loan " +
                        "INNER JOIN info5051_books.borrower " +
                        "WHERE CURDATE() > info5051_books.book_loan.date_due AND info5051_books.book_loan.date_returned IS NULL;"
            );

            DefaultTableModel theBooks = returnTableModelFromResultSet(books);

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

    public boolean addNewUser(String first_name, String last_name, String email){
        Statement query = null;

        try{
            query = connection.createStatement();
            query.executeUpdate(
                    "INSERT INTO BORROWER (first_name, last_name, email) " +
                        "VALUES ('" + first_name + "', '" + last_name + "', '"+ email +"')"
            );

            if(query != null)
                query.close();

            return true;
        }catch (Exception ex){
            System.out.println("Exception: " + ex.getMessage());
            ex.printStackTrace();

            return false;
        }
    }

    public boolean addNewBook(String title, String isbn, int edition, String subject, ArrayList<String> Authors){
        Statement query = null;
        ResultSet authors = null;
        ResultSet book = null;

        try{
            query = connection.createStatement();
            query.executeUpdate(
                    "INSERT INTO BOOK (title, isbn, edition_number, subject, available) " +
                            "VALUES ('" + title + "', '" + isbn + "', "+ edition +", " + subject + ", true)"
            );

            book = query.executeQuery("SELECT BookID * FROM Books WHERE title ='" + title + "' AND ISBN ='" + isbn + "'");
            if(book.next()) {
                for (String Author : Authors) {

                    String split[] = Author.split(",");
                    String first = split[0];
                    String last = split[1];

                    authors = query.executeQuery("SELECT * FROM author WHERE first_name ='" + first + "' AND last_name = '" + last + "'");

                    //If .next() returns false, there are no existing authors with that name
                    if (!authors.next())
                        query.executeUpdate(
                                "INSERT INTO author (first_name, last_name) " +
                                        "VALUES ('" + first + "', '" + last + "')"
                        );

                    //Get the authors ID
                    authors = query.executeQuery("SELECT AuthorID FROM author WHERE first_name ='" + first + "' AND last_name = '" + last + "'");

                    if(authors.next()) {
                        query.executeUpdate(
                                "INSERT INTO BOOK_AUTHOR (Book_BookID, Author_AuthorID) " +
                                        "VALUES (" + book.getString("BookID") + ", " + authors.getString("AuthorID") + ")"
                        );
                    }else{
                        return false;
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
