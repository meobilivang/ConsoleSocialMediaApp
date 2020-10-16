/**
 *  @author Jacob Nozaki, Phong Nguyen, Bill Tran
 * COM212 Professor Tarimo
 * FINAL PROJECT:
 *      A console-based social networking profile that focuses on a single user’s account
 * 05/12/2020
 * @version 1.0
 * Event.java
 * this class creates the 2d array that holds the events for the social profile
 */
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

class Event {
    private int capacity = 5;           //Default capacity with 5 rows (5 events) for the 2D array 
    private int size = 0;               //Actual number of events inside this
    private int size_inner_arr = 2;     //Default capacity of an inner array: 1 for the date, 
                                        //1 for description of that event

    String[][] events_list = new String[capacity][size_inner_arr];      //Initilize the 2D used to implement the Priority Queue (Min Heap)
    
    /***
     * getLeftChildIndex
     * @param parentIndex
     * @return int
     * This returns the left child index from the given parentIndex
     */
    private int getLeftChildIndex(int parentIndex) { return 2 * parentIndex + 1; }     //Get index of left child

    /***
     * getRightChildIndex
     * @param parentIndex
     * @return int
     * this returns the index of the child from the given parentIndex
     */
    private int getRightChildIndex(int parentIndex) { return 2 * parentIndex + 2; }     //Get index of right child
    /**
     * getParentIndex
     * @param childIndex
     * @return int
     * This returns the index of the parent when given the childIndex
     */
    private int getParentIndex(int childIndex) { return (childIndex - 1) / 2; }         //Get index of parent

    /***
     * hasLeftChild
     * @param index
     * @return boolean
     * This returns a boolean t/f if there is a left child of the index given
     */
    private boolean hasLeftChild(int index)    { return getLeftChildIndex(index) < size; }   

    /****
     * hasRightChild
     * @param index
     * @return boolean
     * This returns a boolean t/f if there is a right child of the index given
     */
    private boolean hasRightChild(int index)    { return getRightChildIndex(index) < size; }

    /***
     * hasParent
     * @param index
     * @return boolean
     * this returns if there is a parent of the index given
     */
    private boolean hasParent(int index)    { return getParentIndex(index) >= 0; }

    /***
     * LeftChild
     * @param index
     * @return String[]
     * This returns the left child from the given index
     */
    private String[] LeftChild(int index) { return events_list[getLeftChildIndex(index)]; }
    
    /***
     * RightChild
     * @param index
     * @return String[]
     * This returns the right child in the list of the given index
     */
    private String[] RightChild(int index) { return events_list[getRightChildIndex(index)]; }
    
    /****
     * parent
     * @param index
     * @return String[]
     * this returns the parent at location from index given
     */
    private String[] parent(int index) { return events_list[getParentIndex(index)]; }
    
    /***
     * getEventList()
     * @return String[][]
     * This returns the list of events
     */
    public String[][] getEventList() {
        return events_list;
    }
    /****
     * getSize()
     * @return int
     * This returns the size of the event list
     */
    public int getSize() {
        return size;        //the actual size of the event list
    }

    /***
     * swap
     * @param indexOne
     * @param indexTwo
     * This swaps the positions in the list of two given indexes
     */
    private void swap(int indexOne, int indexTwo) {
        String temp_date = events_list[indexOne][0];           //Auxiliary string to store the date
        String temp_des = events_list[indexOne][1];            //Auxiliary string to store description

        events_list[indexOne][0] = events_list[indexTwo][0];        //Place the data + desription to the arr at 1st index
        events_list[indexOne][1] = events_list[indexTwo][1];

        events_list[indexTwo][0] = temp_date;                   //Place the data + desription to the arr at 2nd index
        events_list[indexTwo][1] = temp_des;
    }
    
    /***
     * ensureExtraCapacity
     * this makes sure there is always double the capacity in the array of stored elements if necessary it creates a new 2d string array to copy everything over and have enough additional space
     */
    private void ensureExtraCapacity() {
        if (size == capacity) {
            String[][] copy = new String[capacity*2][2];            //auxiliary array
            System.arraycopy(events_list, 0, copy, 0, capacity);    //Copy existing event to auxiliary array
            capacity *= 2;              //Increase the capacity
            events_list = copy;         //reassign the event list
        }
    }
    /**
     * peak
     * @return String[]
     * This returns the front value (not remove it) */
    public String[] peak () {
        if (size == 0) throw new IllegalStateException();      //If the event list is empty
        return events_list[0]; 
    }
    /**
     * isEmpty
     * @return boolean
     * returns true if size is 0 and list is empty, false otherwise
     */
    public boolean isEmpty() {
        return size == 0;
    }

    /***
     * ExtractMin
     * @return String[]
     * this returns a string array of the front event
     */
    public String[] ExtractMin() {
        if (size == 0) throw new IllegalStateException();       //If the event list is empty, throws exception
        
        String[] front_event = events_list[0];      //Store the inner array at index 0
        events_list[0] = events_list[size-1];       //Place the array at the back to the front
        size--;         //Size decrement
        heapifyDown();          //Move down the inner arrays, maintain heap property

        return front_event;
    }

    /**
     * add
     * @param date
     * @param event_description
     * @return int
     * returns one of these values:
     *      -1 : the date already in the list
     *      0: the date is in the pass
     *      1: the data is valid
     */
    public int add(String date, String event_description) {
        if (containsInList(date))       //the date in the list
            return -1;

        else if (checkRealTime(date) != 1)  //the date is in the past
            return 0;

        else {
            ensureExtraCapacity();          
            events_list[size][0] = date;   //Add the date to inner array
            events_list[size][1] = event_description;   //Add the description to inner array

            size++;         //Size increment
            heapifyUp();     //Move up the element, maintain heap property
        }
        return 1;
    }

    /***
     * heapifyUp
     * this loops through to make sure the heap property is preserved and swaps the Parent index with index below if it is violated
     */
    public void heapifyUp() {
        int index = size - 1;       //index at the back
        while ( hasParent(index) && isSoonerDate(events_list[index], parent(index)) == 1 ) {     //Loop runs where heap property is violated
            swap(getParentIndex(index), index);     //swapp
            index = getParentIndex(index);          //reassign the index
        }
    }

    /***
     * heapifyDown
     * this loops through to make sure the heap property is preserved and swaps smallerChildIndex if it is violated to fix it
     */
    public void heapifyDown() {
        int index = 0;      //index of the front
        while (hasLeftChild(index)) {   //if no left child so no right child
            int smallerChildIndex = getLeftChildIndex(index);       //get the index of left child (default: left child)
            if (hasRightChild(index) && isSoonerDate(RightChild(index), LeftChild(index)) == 1 ) {      //Comparing 2 events, get the sooner event
                smallerChildIndex = getRightChildIndex(index);          //Index of right child
            }

            if ( isSoonerDate(events_list[index], events_list[smallerChildIndex]) == 1) {       //If the events at index occurs before the event to the left / right 
                break;              //heap property is maintained -> break the loop
            }

            else {
                swap(index, smallerChildIndex);     //swap
            }
            index = smallerChildIndex;      //move to the next index
        }
    }

    /**
     * isSoonerDate
     * @param event_1
     * @param event_2
     * @return int
     * returns one of these to check if the date is sooner
     *      1: if event_1 occurs before event_2
     *      0: if event_1 does not occur before event_2
     *      -1: Error occurs
     */
    public int isSoonerDate(String[] event_1, String[] event_2) {
        SimpleDateFormat dateformat = new SimpleDateFormat("MM dd yyyy HH mm");     //Standard format of the date
        Date d1;
        Date d2;
        
        try {
            d1 = dateformat.parse(event_1[0]);      //Convert string into date format
            d2 = dateformat.parse(event_2[0]);      

            return ( d1.compareTo(d2) < 0 ? 1 : 0 );    //Return 1 if date 1 is sooner is sooner date 2, otherwise returns 0

        } catch (ParseException e) {            //Catch exception
            e.printStackTrace();
        }

        return -1;
    }

    /**
     * checkRealTime
     * @param input_time
     * @return int
     * returns one of these to make sur eit is a valid time input (not in the past)
     *      1: valid time input
     *      0 : invalid input time
     *      -1: error
     */
    public int checkRealTime(String input_time) {
        SimpleDateFormat dateformat = new SimpleDateFormat("MM dd yyyy HH mm"); //Standard format of the date
        Date realTime = new Date();         //Real time date
        
        try {       //try-catch statement to catch the Exception
            Date inputDate = dateformat.parse(input_time);              //Convert the date from string to date format

            return ( realTime.compareTo(inputDate) < 0 ? 1 : 0);            //Return 1 if the input date occurs in the past, otherwise returns 0

        } catch (ParseException e) {        //Catch ParseException
            e.printStackTrace();
        }
        return -1;
        
    }
    /**
     * containsInList
     * @param input_time
     * @return boolean
     * checks if the time is already in the list
     */
    public boolean containsInList(String input_time) {
        for (int i = 0; i < size; i++) {       //Loop through the event list
            if (events_list[i][0].equals(input_time))       //Check whether the date in the list
                return true;
        }
        return false;
    }

    /***
     * display
     * prints out the event, date, and description, or tells if it is empty
     */
    public void display() {
        if (isEmpty())      //Event list empty
            System.out.println("\tYour event list is empty!");

        else {
            for (int i = 0; i < size; i++) {        //Print the date and description of each event
                System.out.println("\n\t\t\t\t\tEVENT " + (i + 1));
                System.out.println("\tDate of the event: " + events_list[i][0]);
                System.out.println("\tDescription: " + events_list[i][1]);
            }
        }
        
    }

}