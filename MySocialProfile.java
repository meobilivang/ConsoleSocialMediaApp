/** @author Jacob Nozaki, Phong Nguyen, Bill Tran
 * COM212 Professor Tarimo
 * FINAL PROJECT:
 *      A console-based social networking profile that focuses on a single user’s account
 * 05/12/2020
 * @version 1.0
 * MySocialProfile.java
 * This class creates the social profile from the inputs in main and text file.
 */

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class MySocialProfile {
    private String name;            //Instance varible for user name
    private String email;           //Instance varible for user email
    private String password;        //Instance varible for user password
    private Integer class_year;     //Instance varible for user's class year
    private Event event_List = new Event();     //List of event (Priority Queue)
    private ArrayList<String> timeline;         //List of timeline posts
    private ArrayList<String> friend_list;      //Friend list
    private String filename = "mysocialprofile_asian2.5.txt";           //fixed file name 
    HashMap<String, String> login = new HashMap<String, String>();      //Initilizing a map
    
    /***
     * MySocialProfile
     * 
     * @param user_name
     * @param user_email
     * @param user_password
     * @param user_class_year
     * @param user_activities
     * @param user_posts
     * @param user_friends
     * Constructor to make the profile if there isn't one already
     */
    public MySocialProfile (String user_name, String user_email, String user_password, Integer user_class_year, Event user_activities, ArrayList<String> user_posts, ArrayList<String> user_friends){
        this.name = user_name;
        this.email = user_email;
        this.password = user_password;
        this.class_year = user_class_year;
        this.event_List = user_activities; 
        this.timeline = user_posts;
        this.friend_list = user_friends;
    }

    /**
     * Constructor for an existing Profile
     */ 
    public MySocialProfile() {
        readfile();     //Call method readFile() -> read the existing file
    }

    /**
     * readfile
     * read the file line by line to recreate the profile stored inside
     */
    private void readfile() {
        try 
        {   //try-catch statement is needed because FileInputStream may throw a FileNotFoundException
            
			Scanner lineScanner = new Scanner(new FileInputStream(filename));
			while (lineScanner.hasNext()) { //while more of the input file is still available for reading			
				this.name = lineScanner.nextLine();         //reads an entire line of input for name of user
				this.email = lineScanner.nextLine();        //Email
				this.password = lineScanner.nextLine();     //Password
				this.class_year = Integer.parseInt(lineScanner.nextLine());     //Class Year
                this.login.put(this.email, this.password);          //Put email, password into hashmap

                String events = lineScanner.nextLine();         //read the entire line of event data
                Scanner eventsScanner = new Scanner(events);    //Create a secondary scanner to break this line into events         
				eventsScanner.useDelimiter("\",");      // use a commma "," to delimit each event
                String e;       //storing each individual event
				while (eventsScanner.hasNext()) {
					e = eventsScanner.next();
					e = e.substring(1, e.length()); //cut off the leading quotation mark of each event
                    Scanner eScanner = new Scanner(e); //another scanner for each event
                    String input_date = ""; //storing date of the event
                    String input_desc = ""; //storing description of the event
                    int count = 0;       //Calculate the position within this string
                    
                    while (eScanner.hasNext()) {
                        if (count < 5) {          //index 0-5 are the components of the date (MM dd yyyy HH mm)
                            input_date += " " + eScanner.next();        //Add to the date string
                            count++;            //increment the count var
                        }
                        else
                            input_desc += " " + eScanner.next();        //Add to the description string
                    }
                    
                    input_date = input_date.substring(1, input_date.length());  //cut the " " at the front 
                    input_desc = input_desc.substring(1, input_desc.length());  //cut the " " at the front
                    event_List.add(input_date, input_desc);   //add to event list
                    eScanner.close();       //close scanner for each event
                    
                }
                eventsScanner.close();		//close scanner for events
                
				String allposts = lineScanner.nextLine();           //Line of Timeline posts
				Scanner timelineScanner = new Scanner(allposts);      //Scanner for timeline Posts
                this.timeline= new ArrayList<String>();             
				timelineScanner.useDelimiter("\",");            //Delimeter is a bracket and a comma: ",
                String post;                //Storing each post
                
                while (timelineScanner.hasNext()) {
					post = timelineScanner.next();
					post = post.substring(1, post.length());   //Remove " " from the String.
					timeline.add(post);     //add to the list of post 
                }
                timelineScanner.close();        //close scanner for timeline posts
                
				String allfriends = lineScanner.nextLine();         //Scan the whole list of friend
				Scanner friendScanner = new Scanner(allfriends);     //Scanner for each friend
				this.friend_list = new ArrayList<String>();         
				friendScanner.useDelimiter(",");                //Use a comma (,) as a delimeter
                String friend;          //Storing each friend
                
				while (friendScanner.hasNext()) {
					friend = friendScanner.next();   
					friend_list.add(friend);    //stores friend into array of friends
                }
                friendScanner.close();
            }
            lineScanner.close();    //Close scanner for the whole text file
	
		} catch(FileNotFoundException ex) {         //Catch error
			System.out.println("File not Found");
			System.exit(0);
        }
    }

    /**
     * getEventList
     * Returns the event list
     * @return Event
     */
    public Event getEventList() {
        return event_List;
    }

    /**
     * printName
     * prints the name
     */
    public void printName() {
        System.out.println("Name: " + this.name);
    }

    /**
     * printEmail
     * prints the email
     */
    public void printEmail() {
        System.out.println("Email: " + this.email);
    }

    /**
     * printPassword
     * prints the password (private for limited access)
     */
    private void printPassword() {      //Limited access
        System.out.println("Password: " + this.password);
    }

    /**
     * printClassYear
     * prints the class year
     */
    public void printClassYear() {
        System.out.println("Class year of user: " + this.class_year);
    }

    /**
     * printEvents
     * prints the event list
     */
    public void printEvents() {
        event_List.display();
    }

    /**
     * printTimeLine
     * Gets 3 latest posts from the timeline and prints them
     */
    public void printTimeline() {
        System.out.println("\t3 latest timeline posts: ");
        int count = 1;
        int x = 3;
        
        if (timeline.size() < 3) //if less than 3 events in the list 
            x = timeline.size();        //print all events

        for (int j = timeline.size() - 1; j >= timeline.size() - x; j-- ) {
            System.out.println( "\t\t- Post " + count + ": " + (timeline.get(j)).toString() + "\n");        //Print each post
            count++;        //increment
        }
    }

    /**
     * ViewFriendList
     * prints the friend list
     */
    public void ViewFriendlist() {
        for (int z = 0; z < friend_list.size(); z++) {
            System.out.println("\t\t\t" + (z +1) + ". " +friend_list.get(z));
            System.out.println();
        }
    }
    
    /**
     * checkUsername
     * Checks input email
     * @param input_email
     * @return boolean
     */
    public boolean checkUsername(String input_email) {
        return login.containsKey(input_email);
    }

    /**
     * checkPassword
     * Check input password
     * @param input_password
     * @return boolean
     */
    public boolean checkPassword(String input_password) {
        return login.containsValue(input_password);     //check whether the password is correct
    }

    /**
     * AddEvent
     * @param input_date
     * @param input_event
     * @return boolean
     */
    public int AddEvent(String input_date, String input_event) {
        return event_List.add(input_date, input_event);         //add event to the list
    }

    /**
     * PostTimeLine
     * @param input_post
     * @return boolean
     * returns
     *  false -> if the post exists
     *  true -> the post is not in the list
     */
    public boolean PostTimeline(String input_post) {
        if (this.timeline.contains(input_post))
            return false;
        
        this.timeline.add(input_post);
        return true;
    }

    /**
     * addOrRemoveFriend
     * @param input_friend_email
     * @return boolean
     *      false -> remove a friend
     *      true -> add a new friend
     */
    public boolean addOrRemoveFriend(String input_friend_email) {
        if (this.friend_list.contains(input_friend_email)) {          //friend's email already in the list
            this.friend_list.remove(input_friend_email);        //remove this friend
            return false;
        }

        else {
            this.friend_list.add(input_friend_email);       //otherwise, add this friend
            return true;
        }
    }

    /**
     * homeScreen
     * prints out the personal profile home screen
     * 
     */
    public void homeScreen() {
            System.out.println("\n\n\t\t++++++++++++++++++++++++++++++++++++++++++++++++++++++");
            System.out.println("\t\t\t\tPERSONAL PROFILE: " + this.name);
            System.out.println("\t\t++++++++++++++++++++++++++++++++++++++++++++++++++++++");
            System.out.print("\n");
            if (event_List.isEmpty())           //If there is no event
                System.out.println("\tYour event list is empty!");
            
            else {      //otherwise
                System.out.println("\tUpcoming Event: " + event_List.peak()[1]);        //show the date of the first event
                System.out.println("\tDate of most recent event: " + event_List.peak()[0]);     //show the description of the first event
            }
            System.out.println();
            printTimeline();
            printEvents();
    }
    
    /**
     * saveFile
     * writes the profile to the file
     * 
     */
    public void saveFile() {
        try 
        {
            FileWriter fileout = new FileWriter(filename);         
            BufferedWriter bufWriter = new BufferedWriter(fileout);         //FileWriter to write data to a text file

            bufWriter.write(this.name + "\n");      //Write user name to file
            bufWriter.write(this.email + "\n");     //Write user's email to file
            bufWriter.write(this.password + "\n");      //Write user's password to file
            bufWriter.write(this.class_year + "\n");    //Write user's class year to file

            StringBuffer events_string = new StringBuffer("");      //Initialize an empty string buffer
            String[][] event_2d_List = event_List.getEventList();      //Retrieve the event list
            for (String[] innerString : event_2d_List) {           //Loop through each event
                if (innerString[0] == null && innerString[1] == null )
                    break;      //Break the loop when face an emtpy event
                events_string.append("\"" + innerString[0] + " " + innerString[1] + "\","); //Append the event to string
            }
            bufWriter.write(events_string.toString() + "\n");       //Write event list to the .txt file

            StringBuffer timeline_string = new StringBuffer("");       //Initialize an empty string buffer storing all posts
            this.timeline.forEach(post -> timeline_string.append("\"" + post + "\"," ));     //append each post to the list of posts
            bufWriter.write(timeline_string.toString() + "\n");         //Write the list of timeline posts to .txt file

            StringBuffer friend_list_string = new StringBuffer("");         //Initialize an empty string buffer all friends
            this.friend_list.forEach(friend -> friend_list_string.append(friend + "," ));   //append each friend to the list of friend
            bufWriter.write(friend_list_string.toString() + "\n");            //write the list of friend to the .txt file

            bufWriter.close();          //close

        } catch (IOException e) {       //catch Input Output Exception
            System.out.println("An error occured!");
			e.printStackTrace();
        }
    }
    
}