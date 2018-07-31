package com.dave.chan;

import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;
import javax.swing.table.DefaultTableModel;
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
    private ArrayList<Row> itemsArrayList = new ArrayList<Row>();
    private ArrayList<ListDataListener> dataListenerList = new ArrayList<ListDataListener>();

    private DefaultTableModel returnTableModelFromResultSet(ResultSet rs){
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
            e.printStackTrace();

            return null;
        }
    }

    public DefaultTableModel getAllBooks(){
        Connection connection = null;
        Statement query = null;
        ResultSet books = null;

        try{
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/info5051_books?useSSL=false&useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=EST5EDT","root","password");
            query = connection.createStatement();
            books = query.executeQuery("SELECT * FROM book");

            DefaultTableModel theBooks = returnTableModelFromResultSet(books);

            if(books != null)
                books.close();
            if(query != null)
                query.close();
            if(connection != null)
                connection.close();

            return theBooks;
        }catch (Exception ex){
            System.out.println("Exception: " + ex.getMessage());
            ex.printStackTrace();

            return null;
        }
    }

    public DefaultTableModel getAllLoanedBooks(){
        Connection connection = null;
        Statement query = null;
        ResultSet books = null;

        try{
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/info5051_books?useSSL=false&useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=EST5EDT","root","password");
            query = connection.createStatement();
            books = query.executeQuery("SELECT * FROM book WHERE Available = 0");

            DefaultTableModel theBooks = returnTableModelFromResultSet(books);


            if(books != null)
                books.close();
            if(query != null)
                query.close();
            if(connection != null)
                connection.close();

            return theBooks;
        }catch (Exception ex){
            System.out.println("Exception: " + ex.getMessage());
            ex.printStackTrace();

            return null;
        }
    }

    public DefaultTableModel getBooksbySubject(String subject){
        Connection connection = null;
        Statement query = null;
        ResultSet books = null;

        try{
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/info5051_books?useSSL=false&useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=EST5EDT","root","password");
            query = connection.createStatement();
            books = query.executeQuery("SELECT * FROM book WHERE Subject = '" + subject + "'");

            DefaultTableModel theBooks = returnTableModelFromResultSet(books);

            if(books != null)
                books.close();
            if(query != null)
                query.close();
            if(connection != null)
                connection.close();

            return theBooks;
        }catch (Exception ex){
            System.out.println("Exception: " + ex.getMessage());
            ex.printStackTrace();

            return null;
        }
    }

    public DefaultTableModel getBooksbyAuthor(String last_name){
        Connection connection = null;
        Statement query = null;
        ResultSet books = null;

        try{
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/info5051_books?useSSL=false&useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=EST5EDT","root","password");
            query = connection.createStatement();
            books = query.executeQuery(
                    "SELECT * FROM info5051_books.book\n" +
                    "INNER JOIN info5051_books.author\n" +
                    "WHERE last_name = '" + last_name + "'");

            DefaultTableModel theBooks = returnTableModelFromResultSet(books);

            if(books != null)
                books.close();
            if(query != null)
                query.close();
            if(connection != null)
                connection.close();

            return theBooks;
        }catch (Exception ex){
            System.out.println("Exception: " + ex.getMessage());
            ex.printStackTrace();

            return null;
        }
    }

    public DefaultTableModel getAllBorrowers(){
        Connection connection = null;
        Statement query = null;
        ResultSet borrowers = null;

        try{
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/info5051_books?useSSL=false&useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=EST5EDT","root","password");
            query = connection.createStatement();
            borrowers = query.executeQuery("SELECT first_name, last_name, borrower_email FROM borrower");

            DefaultTableModel theBorrowers = returnTableModelFromResultSet(borrowers);

            if(borrowers != null)
                borrowers.close();
            if(query != null)
                query.close();
            if(connection != null)
                connection.close();

            return theBorrowers;
        }catch (Exception ex){
            System.out.println("Exception: " + ex.getMessage());
            ex.printStackTrace();
        }
        return new DefaultTableModel();
    }

    public boolean updateBorrowers(DefaultTableModel model){
        Connection connection = null;
        PreparedStatement query = null;

        try{
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/info5051_books?useSSL=false&useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=EST5EDT","root","password");

            query = connection.prepareStatement("UPDATE borrower SET first_name = ?, last_name = ?, borrower_email = ? WHERE borrower_id = ?");


            Vector<Vector> users = model.getDataVector();

            for(int i = 0; i < users.size(); i++){
                query.setString(1, (String)users.get(i).get(0));
                query.setString(2, (String)users.get(i).get(1));
                query.setString(3, (String)users.get(i).get(2));
                query.setInt(4, i+1);
                query.addBatch();
            }

            query.executeBatch();

            if(query != null)
                query.close();
            if(connection != null)
                connection.close();

            return true;
        }catch (Exception ex){
            System.out.println("Exception: " + ex.getMessage());
            ex.printStackTrace();
            return false;
        }
    }

    public boolean addNewBorrower(String first, String last, String email){
        Connection connection = null;
        Statement query = null;

        try{
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/info5051_books?useSSL=false&useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=EST5EDT","root","password");

            query = connection.createStatement();

            query.executeUpdate("INSERT INTO borrower (first_name, last_name, borrower_email) VALUES('"+ first +"', '"+ last +"', '"+ email +"')");

            if(query != null)
                query.close();
            if(connection != null)
                connection.close();

            return true;
        }catch (Exception ex){
            System.out.println("Exception: " + ex.getMessage());
            ex.printStackTrace();
            return false;
        }
    }

    public DefaultTableModel getOverdueBooks(){
        Connection connection = null;
        Statement query = null;
        ResultSet books = null;

        try{
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/info5051_books?useSSL=false&useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=EST5EDT","root","password");
            books = query.executeQuery(
                    "SELECT * FROM info5051_books.book\n" +
                        "INNER JOIN info5051_books.book_loan\n" +
                        "INNER JOIN info5051_books.borrower\n" +
                        "WHERE CURDATE() > info5051_books.book_loan.date_due AND info5051_books.book_loan.date_returned IS NULL;"
            );

            DefaultTableModel theBooks = returnTableModelFromResultSet(books);

            if(books != null)
                books.close();
            if(query != null)
                query.close();
            if(connection != null)
                connection.close();

            return theBooks;
        }catch (Exception ex){
            System.out.println("Exception: " + ex.getMessage());
            ex.printStackTrace();

            return null;
        }
    }

    public boolean addNewUser(String first_name, String last_name, String email){
        Connection connection = null;
        Statement query = null;

        try{
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/info5051_books?useSSL=false&useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=EST5EDT","root","password");
            query = connection.createStatement();
            query.executeUpdate(
                    "INSERT INTO BORROWER (first_name, last_name, email) " +
                        "VALUES ('" + first_name + "', '" + last_name + "', '"+ email +"')"
            );

            if(query != null)
                query.close();
            if(connection != null)
                connection.close();

            return true;
        }catch (Exception ex){
            System.out.println("Exception: " + ex.getMessage());
            ex.printStackTrace();

            return false;
        }
    }

    public boolean addNewBook(String title, String isbn, int edition, String subject, ArrayList<String> Authors){
        Connection connection = null;
        Statement query = null;
        ResultSet authors = null;
        ResultSet book = null;

        try{
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/info5051_books?useSSL=false&useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=EST5EDT","root","password");
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
                if (connection != null)
                    connection.close();

                return true;
            }else{
                return false;
            }
        }catch (Exception ex){
            System.out.println("Exception: " + ex.getMessage());
            ex.printStackTrace();

            return false;
        }
    }

    public boolean returnABook(int BookID){
        Connection connection = null;
        Statement query = null;

        try{
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/info5051_books?useSSL=false&useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=EST5EDT","root","password");
            query = connection.createStatement();
            query.executeUpdate("UPDATE book_loan SET date_returned = CURDATE() WHERE Book_BookID = " + BookID + "");
            query.executeUpdate("UPDATE book SET available = true WHERE BookID = " + BookID + "");

            if(query != null)
                query.close();
            if(connection != null)
                connection.close();

            return true;
        }catch (Exception ex){
            System.out.println("Exception: " + ex.getMessage());
            ex.printStackTrace();

            return false;
        }
    }

    public int getSize()
    {
        // this method informs the JList how many ArrayList items are to be displayed
        return itemsArrayList.size();
    }

    public String getElementAt(int index)
    {
        if(index < itemsArrayList.size())
        {
            Row row = itemsArrayList.get(index);

            //now append each data member of the row object to a StringBuilder object
            StringBuilder itemsArrayListString = new StringBuilder();

            if(row.number >0)//no sense appending a zero
            {
                itemsArrayListString.append(row.number + " x ");
            }

            itemsArrayListString.append(row.item + " ");

            if(row.size.length() > 0)
            {
                itemsArrayListString.append(row.size + " " + row.units + " ");
            }

            //now convert the StringBuilder object to a String object so that it can
            // be stored in the JList, which is set up for just Strings
            return itemsArrayListString.toString();

        }//end outer if

        return null;//leave this here in case an if statement fails above.
    }

    public void addListDataListener(ListDataListener listener)
    {
        // register it
        dataListenerList.add(listener);
    }//end method

    public void removeListDataListener(ListDataListener listener)
    {
        // if there is a listener,remove it
        if(dataListenerList.contains(listener) )
        {
            //TEST
            dataListenerList.remove(listener);
        }
    }//end method

    public synchronized void addElement(double number, String item, String size, String units)
    {
        //CREATE a new Row object
        Row row = new Row();
        row.item = item;
        row.number = number;
        row.size = size;
        row.units = units;

        //add the Row object to the ArrayList
        itemsArrayList.add(row);

        //call the processEvent() method and pass it a new ListDataEvent
        //object indicating that the contents of the list have been changed.
        processEvent (new ListDataEvent(this, ListDataEvent.CONTENTS_CHANGED,0,0) );

    }//end method

    public synchronized void removeElement(int index)
    {
        if(index < itemsArrayList.size() )
        {
            itemsArrayList.remove(index);
        }
        //call the processEvent() method and pass it a ListDataEvent indicating
        // that the contents of the list have been changed.
        processEvent (new ListDataEvent(this, ListDataEvent.CONTENTS_CHANGED,0,0) );

    }//end method

    private void processEvent(ListDataEvent ev)
    {
        synchronized(this)//prevents data corruption if multiple threads try to access the arrayList
        {
            //cycle through the list of listeners so that they all get the message
            // and let them know that a list changed event has occured.
            for(int i = 0; i < dataListenerList.size(); i++)
            {
                dataListenerList.get(i).contentsChanged(ev);
            }//end for
        }//end synchronized block
    }//end method

    private class Row
    {
        public String item;
        public double number;
        public String size;
        public String units;
    }
}//end class
