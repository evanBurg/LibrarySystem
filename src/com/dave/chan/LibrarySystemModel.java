package com.dave.chan;

import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

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
    private class Row
    {
        public String item;
        public double number;
        public String size;
        public String units;
    }

    private ArrayList<Row> itemsArrayList = new ArrayList<Row>();
    private ArrayList<ListDataListener> dataListenerList = new ArrayList<ListDataListener>();

    public ResultSet getAllBooks(){
        Connection connection = null;
        Statement query = null;
        ResultSet books = null;

        try{
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/animals?useSSL=false&useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=EST5EDT","root","password");
            books = query.executeQuery("SELECT * FROM book");

            if(books != null)
                books.close();
            if(query != null)
                query.close();
            if(connection != null)
                connection.close();

            return books;
        }catch (Exception ex){
            System.out.println("Exception: " + ex.getMessage());
            ex.printStackTrace();
        }finally {

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
}//end class
