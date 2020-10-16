/**
 * @author Jacob Nozaki, Phong Nguyen, Bill Tran
 * COM212 Professor Tarimo
 * FINAL PROJECT: 
 *      A console-based social networking profile that focuses on a single user’s account
 * 05/12/2020
 * @version 1.0
 * SocialMediaApp.java
 * This class combines the Event and MySocialProfile classes to generate a social profile that it displays on screen and saves to a .txt file
 */

import java.io.*;
import java.util.*;

public class SocialMediaApp {
    /**
     * SecondOptionList holds the second option that the user chooses
     * @param user
     * @param user_activities
     * return boolean
     */
    public static boolean secondOptionList(MySocialProfile user) throws IOException {
        int sub_choice;    //storing the choice entered by user
        boolean run = false;        //the program stops
        do { //Execute a set of actions 
            System.out.print("\nEnter a number: \n (1) Post to timeline \n (2) Add new event \n ");
            System.out.print("(3) View friendlist \n (4) Add friend/Remove friend \n ");
            System.out.print("(5) Log out \n\t --> ");
            sub_choice = getChar();

            switch (sub_choice) {       //Switching cases depending on user's input

                case '1':              //New Post to timeline
                    System.out.print("\n\tWhat's on your mind? ");
                    String input_post = getString();        //post input by user
                    user.PostTimeline(input_post);          //add the input post to time line
                    System.out.print("\t\t\t<Posted successfully to timeline>");
                    user.homeScreen();                      //display homescreen
                    break;

                case '2':           //new Event
                    System.out.print("\n\t\tPlease enter the date <Month day year hour minute> ");
                    System.out.print("<Example: 12 08 2020 04 09> :");
                    String input_date = getString();            //Take the input data from user
                    System.out.print("\n\t\tWhat is your event? ");
                    String input_desc = getString();

                    while (user.AddEvent(input_date, input_desc) != 1) {
                        if (user.AddEvent(input_date, input_desc) == -1)
                            System.out.print("\n\t\t\t\t<THIS DATE ALREADY OCCUPIED!>");
                        
                        else
                            System.out.print("\n\t\t\t\t<THIS DATE IS IN THE PAST!>");    
                        
                        System.out.print("\n\t\tPlease re-enter the date <Month day year hour minute> ");
                        System.out.print("<Example: 12 08 2020 04 09> :");
                        input_date = getString();
                        System.out.print("\n\t\tWhat is your event? ");
                        input_desc = getString();
                    }
            
                    user.homeScreen();      //Display homescreen
                    break;

                case '3':           //View friendlist
                    System.out.print("\n\t\t\t\tFRIEND LIST \n");
                    System.out.println();
                    user.ViewFriendlist();
                    user.homeScreen();
                    break;

                case '4':           //Add or remove friend
                    System.out.print("\n\t\t(If the input email already in the list, it would be removed)");
                    System.out.println();
                    System.out.print("\n\tEnter your friend's email: ");
                    String a_friend = getString();


                    if (user.addOrRemoveFriend(a_friend))       //add friend
                        System.out.println("\t\t\t<Added successfully!>\n");   
                    else                                    //remove friend
                        System.out.println("\t\t\t<Removed successfully!>\n");

                    user.homeScreen();
                    break;

                default:            //If the input is not listed above
                    if (sub_choice != '5') 
                        System.out.println("\t\t\t<Not a valid option. Please retry!>\n");
            }

        } while (sub_choice != '5');        //repeat the above code as long as user's input is not '5'
        user.saveFile();                    //Save file
        return run;     
    }

    /**
     * main holds the calls to other classes and methods that enable the class to work
     * 
     * @throws IOException
     */
    public static void main (String[] args) throws IOException {
        int main_choice; 
        do 
        {   
            System.out.println("\n\t\t+++++++++++++++++++++++++++++++++++++++++++++++++++");
            System.out.println("\t\t\t\tWELCOME TO CAMEL WEB");
            System.out.println("\t\t+++++++++++++++++++++++++++++++++++++++++++++++++++");
            System.out.print("\nEnter a letter: \n (A) Create a new profile \n (B) Load an existing profile \n ");
			System.out.print("(C) Exit the app \n\t --> ");
            main_choice = getChar();            //Receive the input from user

            switch (main_choice) {              //Switching cases based on user's input

                case 'A':           //Creating new Profile (Overwriting existing file)
                    System.out.println("\t\t+++++++++++++++++++++++++++++++++++++++++++++++++++");
                    System.out.println("\t\t\t\tCREATING A NEW PROFILE");
                    System.out.println("\t\t+++++++++++++++++++++++++++++++++++++++++++++++++++");
                    
                    System.out.print("\n\tPlease enter your full name: ");
                    String user_name = getString();             //User name

                    System.out.print("\n\tPlease enter your email: ");
                    String user_email = getString();            //User's email

                    System.out.print("\n\tPlease enter a password: ");
                    String user_password = getString();           //USer's password
                    System.out.println("\t\t\t<Please do not share your password with others>");

                    System.out.print("\n\tPlease enter your class year: ");
                    Integer user_classyear = getInt();              //User's class year
                    
                    Event user_activities = new Event();            //New Event object (Priority Queue)
                    System.out.print("\n\tEnter number of events that you want to attend: ");
                    Integer num_events = getInt();      //Number of events
                    int count = 1;                  //Counting number of events variable
                    String date;
                    String desc;

                    while (num_events != 0) {           
                        System.out.println("\n\t\t\t\t\tEVENT " + count);          
                        System.out.print("\n\tPlease enter the date <Month day year hour minute> ");
                        System.out.print("<Example: 12 08 2020 04 30> :");
                        date = getString();             //Get event's date
                        System.out.print("\n\tWhat is your event? ");
                        desc = getString();             //Get event's description

                        while (user_activities.add(date, desc) != 1) {          //While the date is invalid
                            if (user_activities.add(date, desc) == -1)              //The date exists in the list already
                                System.out.print("\n\t\t\t\t<THIS DATE ALREADY OCCUPIED!>");
                            
                            else
                                System.out.print("\n\t\t\t\t<THIS DATE IS IN THE PAST!>");      ////The date is in the past
                            
                            System.out.print("\n\tPlease re-enter the date <Month day year hour minute> ");     //Prompts user to re-enter 
                            System.out.print("<Example: 12 08 2020 04 09> :");
                            date = getString();         //Date
                            System.out.print("\n\tWhat is your event? ");
                            desc = getString();         //Description
                        }
                        num_events--;           //Decrement the number of events
                        count++;                //Increment the counting varible
                    }
                    
                    ArrayList<String> user_posts = new ArrayList<String>();         //An arraylist containing timeline posts
                    System.out.print("\n\tEnter number of post that you want to upload to profile: ");
                    Integer num_post = getInt();            //number of posts
                    String each_post;
                    int count_post = 1;             //a counting variable for timeline posts

                    while (num_post != 0) {
                        System.out.println("\n\t\t\t\t\tPOST " + count_post);
                        System.out.print("\n\tWhat's on your mind? ");          //Promts user's input
                        each_post = getString();            //Get user's input
                        user_posts.add(each_post);          //Add the input post to list
                        num_post--;                 //Decrement number of posts
                        count_post++;               //Increment the counting varible
                    }

                    ArrayList<String> user_friends = new ArrayList<String>();       //An arraylist containing friendlist 
                    System.out.print("\n\tEnter number of friends that you want add: ");
                    Integer num_friend = getInt();              //number of friends
                    String each_friend;             
                    int count_friend = 1;               ////a counting variable for number of friends

                    while (num_friend != 0) {
                        System.out.println("\n\t\t\t\t\tFRIEND " + count_friend);
                        System.out.print("\n\tEnter your friend's email: ");
                        each_friend = getString();      
                        user_friends.add(each_friend);      //add the friend to the list
                        num_friend--;               //Decrement the number of friends
                        count_friend++;             //Increment number of friends
                    }
                    
                    MySocialProfile new_user = new MySocialProfile(user_name, user_email, user_password, user_classyear, user_activities, user_posts, user_friends);    //New Profile
                    new_user.homeScreen();          //Display home screen
                    while (secondOptionList(new_user));           //Prompts user to choose second set of operations until user wants to stop
                    
                    break;

                case 'B':               //Log into existing Profile
                    System.out.println("\n\t\t+++++++++++++++++++++++++++++++++++++++++++++++++++");
                    System.out.println("\t\t\t\t\tLOG IN");
                    System.out.println("\t\t+++++++++++++++++++++++++++++++++++++++++++++++++++");

                    MySocialProfile user = new MySocialProfile();           //Call the existing profile
                    System.out.print("\n\tPlease enter your email: ");
                    String input_email = getString();           //input email
    
                    while (!user.checkUsername(input_email)) {      //Check email
                        System.out.print("\n\tIncorrect email! Please re-enter your email: ");  
                        input_email = getString();          //Re-enter the email
                    }

                    System.out.print("\n\tPlease enter your password: ");
                    String input_password = getString();        //input password
                    while (!user.checkPassword(input_password)) {      //Check password
                        System.out.print("\n\tIncorrect password! Please re-enter your password: ");
                        input_password = getString();           //Re-enter the password
                    }

                    user.homeScreen();      //Display home screen
                    System.out.println();
                    while (secondOptionList(user));     //Prompts user to choose second set of operations until user wants to stop
                    
                    break;

                default:        //When the input character is not listed
                    if (main_choice != 'C')         
                        System.out.println("\t\t\t<Not a valid option. Please retry!>\n");
            }

        } while (main_choice != 'C');       //Perform operations until user chooses perform 'C'
        System.out.print("\n\t\t\t\tTHANK YOU FOR USING OUR APP :D");
        System.out.println();
    }

    /***
     * getString() converts the input to a string
     * @return String
     * @throws IOException
     */
    public static String getString() throws IOException
	{
		InputStreamReader isr = new InputStreamReader(System.in);
		BufferedReader br = new BufferedReader(isr);            //Read user's input
		String s = br.readLine();               //Get user's input
		return s;
	}
    
    /***
     *  getChar() converts the string input to a Char
     * @return char
     * @throws IOException
     */
	public static char getChar() throws IOException
	{
		String s = getString();             //Get user's input
		return s.charAt(0);
	}
    
    /***
     * getInt() turns the string into an input
     * @return int
     * @throws IOException
     */
	public static int getInt() throws IOException
	{
		String s = getString();             //Get user's input
		return Integer.parseInt(s);         //Convert to Integer
    }
    
}