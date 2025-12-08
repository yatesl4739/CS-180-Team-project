import javax.naming.spi.ResolveResult;
import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.concurrent.*;

/**
 * reservationServer
 *
 * This program receives input from ReservationClient
 * and makes changes to the venue. A user is able
 * to login, create an account, logout, and make
 * or view reservations.
 *
 * @author Liam Yates, Leo Zhang, Chelsea Zhao, lab sec L12
 *
 * @version November 24, 2025
 *
 */

public class reservationServer implements Runnable, ReservationServerInterface {
    //venue has an events database
    //events database has a reservation database
    private static volatile Venue venue1 = new Venue();  //  the venue the user is accessing
    private static volatile UserDatabase usrDB = new UserDatabase();  //  a list of all users created

    private ServerSocket serverSocket;  //  the serversocket used to connect to the client
    private boolean running = false;  //  boolean value for checking if the server is running
    private ExecutorService pool;  //  executorservice to manage threads of clilents

    private int port;  // port used to connect to client
    private final ArrayList<Socket> clients = new ArrayList<>();  // list of clients

    /**
     * Creates the server with a specific port to connect to the client
     *
     * @param port
     */
    public reservationServer(int port) {

        this.port = port;
        try {
            serverSocket = new ServerSocket(port);
            pool = Executors.newCachedThreadPool();
            System.out.println("Server created");
        } catch (IOException e) {
            throw new RuntimeException("Can't start server", e);
        }
    }

    public static void main(String[] args) {
        reservationServer serv = new reservationServer(4242);
        serv.start();
    }

    /**
     * Creates a thread for the server to run
     */
    public void start() {

        if (running) {
            throw new IllegalStateException("Server is already running");
        }
        running = true;
        new Thread(this).start();
    }

    /**
     * Stops and closes the server
     */
    public void stop() {
        running = false;
        try {
            serverSocket.close();
        } catch (IOException ignored) {
        }
        pool.shutdownNow();
    }

    /**
     * Connects the server to the client
     */
    public void run() {
        if (running) {
            System.out.println("Server running...");
        }

        while (running) {
            try {
                Socket clientSocket = serverSocket.accept();
                pool.submit(new clientHandler(clientSocket));
            } catch (IOException e) {
                if (running == false) break;
                e.printStackTrace();
            }
        }
    }

    /**
     * Returns the port number
     *
     * @return port
     */
    public int getPort() {
        return port;
    }

    /**
     * Checks if the server is running
     *
     * @return running
     */
    public boolean isRunning() {
        return running;
    }

    /**
     * Returns number of clients online
     *
     * @return clients.size()
     */
    public int getClientCount() {
        synchronized (clients) {
            return clients.size();
        }
    }

    /**
     * Returns the venue
     *
     * @return venue1
     */
    public Venue getVenue1() {
        return venue1;
    }

    /**
     * Returns the list of users created
     *
     * @return usrDB
     */
    public UserDatabase getUserDB() {
        return usrDB;
    }

    /**
     * Handles interaction between server and client
     */
    private static class clientHandler implements Runnable {
        private Socket socket;  // socket used to connect server and client
        private User clientUser;  // User object of the current client
        private boolean loggedIn;  // boolean value if the client has logged in

        /**
         * Sets client's socket to a given socket
         *
         * @param socket used to connect client to server
         */
        public clientHandler(Socket socket) {
            this.socket = socket;
        }

        /**
         * Reads client's input and makes changes in server
         */
        public void run() {
            //DO THE CLIENT STUFF

            try {
                BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));

                //Print writer is really for testing use. We would be most likely passing some data somehow
                PrintWriter pr = new PrintWriter(socket.getOutputStream(), true);


                //EVERYTHING BELOW THIS LINE IS COMMUNICATION TO A CERTAIN CLIENT INSTANCE.

                //GIVE THE CLIENT THE VENUE INFO

                //DEBUGGING MESSAGE
                System.out.println("GIVING OUT VENUE INFO");

                pr.println("VENUENAME:" + venue1.getVenueName());


                //view events

                //single hashtag represents the start of the string
                while (true) {
                {



                    String returnEvents = "#";
                    ArrayList<Event> eventDB = venue1.getEventDatabase().getEvents();
                    for (int i = 0; i < eventDB.size(); i++) {
                        if (i == eventDB.size() - 1) {
                            //$$ represents seperator between event names
                            //%% is seperator between day and event name,
                            // and @@ is seperator between day and time
                            returnEvents += eventDB.get(i).getEventName() + "%%";
                            returnEvents += eventDB.get(i).getDay() + "@@";
                            returnEvents += eventDB.get(i).getTimeOfDay();
                        } else {
                            returnEvents += eventDB.get(i).getEventName() + "%%";
                            returnEvents += eventDB.get(i).getDay() + "@@";
                            returnEvents += eventDB.get(i).getTimeOfDay() + "$$";
                        }
                    }
                    //another hashtag added at the end of the string
                    returnEvents += "#";

                    //DEBUGGING MESSAGE
                    System.out.println("THE SERVER IS SENDING THIS OUT: " + returnEvents);

                    pr.println(returnEvents);

                    //DEBUGGING MESSAGE
                    System.out.println("EVENT INFO GIVEN OUT BY SERVER");

                }

                //client would see something like options to do:
                //login, sign up

                String nextInput;

                do {
                    //DEBUGGING MESSAGE
                    System.out.println("READY FOR NEXT OPTION. POSSIBLE NEXT OPTIONS: LOGIN, SIGNUP");


                        nextInput = br.readLine();

                    //DEBUGGING MESSAGE
                    System.out.println("THE CLIENT SENT BACK: " + nextInput);

                    //login:
                    if (nextInput.equals("LOGIN")) {

                            String username = br.readLine();

                            //DEBUGGING MESSAGE
                            System.out.println("The server recieved:" + username + " as the username");

                            String password = br.readLine();

                            //DEBUGGING MESSAGE
                            System.out.println("The server recieved:" + password + " as the password");

                            clientUser = usrDB.login(username, password);
                            if (clientUser.getUsername().equals("PASSWORD INCORRECT")) {
                                pr.println("Error: password incorrect");
                            } else if (clientUser.getUsername().equals("USER NOT FOUND")) {
                                pr.println("Error: user not found");
                            } else {
                                pr.println("Success login with Username:" + username);

                                loggedIn = true;
                            }
                            System.out.println("THE SERVER SENT BACK LOGIN STATUS TO CLIENT");


                        //SIGNUP
                    } else if (nextInput.equals("SIGNUP")) {

                            String username = br.readLine();
                            //DEBUGGING MESSAGE
                            System.out.println("The server recieved:" + username + " as the username");

                            String password = br.readLine();

                            //DEBUGGING MESSAGE
                            System.out.println("The server recieved:" + password + " as the password");


                            clientUser = usrDB.signUp(username, password);
                            if (clientUser.getUsername().equals("INVALID USERNAME")) {
                                pr.println("Error: Invalid Username");

                            } else if (clientUser.getUsername().equals("USERNAME TAKEN")) {
                                pr.println("Error: Username has been taken");
                            } else if (clientUser.getUsername().equals("PASSWORD INVALID")) {
                                pr.println("Error: Password is invalid");

                            } else {
                                pr.println("Success sign up and login with Username:" + username);

                                loggedIn = true;
                            }
                        System.out.println("THE SERVER SENT BACK SIGNUP STATUS TO CLIENT");

                    }
                } while (!loggedIn);


                //DEBUGGING MESSAGE
                System.out.println("Next possible server inputs: NEW to make a new reservation, VIEW to see all reservations for that user, EVENTS to see all events");

                //move onto next possible selection
                //make reservation, see users current reservations, see events, change password maybe

                System.out.println("Checking to see if user is logged in");

                    while (loggedIn) {
                        System.out.println("Starting option loop. User logged in");

                        //get next selection input
                        nextInput = br.readLine();

                        //DEBUGGING MESSAGE
                        System.out.println("Server recieved from client: " + nextInput);


                        if (nextInput.equals("NEW")) {
                            //make a new reservation

                            //show events for the user to pick

                            String returnEvents = "#";
                            ArrayList<Event> eventDB = venue1.getEventDatabase().getEvents();
                            for (int i = 0; i < eventDB.size(); i++) {

                                if (i == eventDB.size() - 1) {
                                    //DEBUGGING COMMENT
                                    System.out.println("THE LAST EVENT IN THE LIST");
                                    //$$ represents seperator between event names
                                    //%% is seperator between day and event name,
                                    // and @@ is seperator between day and time
                                    returnEvents += eventDB.get(i).getEventName() + "%%";
                                    returnEvents += eventDB.get(i).getDay() + "@@";
                                    returnEvents += eventDB.get(i).getTimeOfDay();

                                } else {
                                    //DEBUGGING COMMENT
                                    System.out.println("event encoded");
                                    returnEvents += eventDB.get(i).getEventName() + "%%";
                                    returnEvents += eventDB.get(i).getDay() + "@@";
                                    returnEvents += eventDB.get(i).getTimeOfDay() + "$$";
                                }
                            }
                            //another hashtag added at the end of the string
                            returnEvents += "#";

                            pr.println(returnEvents);
                            System.out.println("THE SERVER SENT OUT EVENT LIST"  + returnEvents);

                            //-1 to make it an index because the client side will select starting at 1.
                            System.out.println("User is asked to pick an event");

                            //Add support for cancel button
                            String rawInput = br.readLine();

                            //debugging print:
                            System.out.println(rawInput);

                            if (rawInput.equals("********CANCEL********")) {
                                //cancel button pressed
                                rawInput = "";
                                continue;
                            }


                            int eventSelect = Integer.parseInt(rawInput) - 1;
                            System.out.println("User gave back " + eventSelect + " as the selection");

                            //give them the seating chart

                            char[][] seatingChart = venue1.getEventDatabase().getEvents()
                                    .get(eventSelect).getSeatingChart();

                            String outputSeatingChart = "";

                            for (int i = 0; i < seatingChart.length; i++) {
                                outputSeatingChart += "[";
                                for (int j = 0; j < seatingChart[i].length; j++) {
                                    outputSeatingChart += seatingChart[i][j];
                                    if (j < seatingChart[i].length - 1) {
                                        outputSeatingChart += ",";
                                    }
                                }
                                outputSeatingChart += "\n";
                            }
                            pr.println(outputSeatingChart);
                            pr.println("ENDCHART");

                            System.out.println("SERVER SENT OUT SEATING CHART");

                            //cancel button check

                            rawInput = br.readLine();
                            if (rawInput.equals("********CANCEL********")) {
                                //cancel button pressed
                                rawInput = "";
                                continue;
                            }
                            int numPeople = Integer.parseInt(rawInput);

                            long time = Long.parseLong(br.readLine());

                            long date = Long.parseLong(br.readLine());

                            System.out.println("Number of people entered " + numPeople + " time entered: " + time + " date entered" + date);


                            //Seat selection input can come in as
                            //x1,y1,x2,y2,x3,y3

                            System.out.println("Server waits for seat input in x1,y1,x2,y2 format");



                            nextInput = br.readLine();

                            if (nextInput.equals("********CANCEL********")) {
                                //cancel button pressed
                                nextInput = "";
                                continue;
                            }

                            System.out.println("Server recieved " + nextInput + "as seat input");


                            String[] xy = nextInput.split(",");
                            int[] x = new int[xy.length / 2];
                            int[] y = new int[xy.length / 2];

                            for (int i = 0; i < xy.length; i++) {
                                if (i % 2 == 0) {
                                    x[i / 2] = Integer.parseInt(xy[i]);

                                } else {
                                    y[i / 2] = Integer.parseInt(xy[i]);
                                }
                            }

                            //create a new reservation
                            venue1.getEventDatabase().getEvents().get(eventSelect)
                                    .createReservation(x, y, clientUser, numPeople, time, date);


                        }

                        //user can see all their reservations
                        else if (nextInput.equals("VIEW")) {
                            ArrayList<Reservation> userReservations = new ArrayList<Reservation>();

                            for (int i = 0; i < venue1.getEventDatabase().getEvents().size(); i++) {
                                ArrayList<Reservation> resDBTemp = venue1.getEventDatabase().
                                        getEvents().get(i).getReservationDB().getReservations();
                                for (int j = 0; j < resDBTemp.size(); j++) {
                                    if (resDBTemp.get(j).getUser().getUsername().equals(clientUser.getUsername())) {
                                        userReservations.add(resDBTemp.get(j));
                                    }
                                }
                            }


                            //provideString of all reservations
                            String returnString = "";
                            for (int i = 0; i < userReservations.size(); i++) {
                                //$$ marks the space between reservation strings
                                if (i < userReservations.size() - 1) {
                                    returnString += userReservations.get(i).toString();
                                } else {
                                    //no $$ on this one beacuse its the last entry
                                    returnString += userReservations.get(i).toString() + "$$";
                                }

                            }

                            pr.println(returnString);

                            System.out.println("SERVER SENT OUT SER RESERVATIONS");

                        } else if (nextInput.equals("EVENTS")) {
                            //view events

                            //single hashtag represents the start of the string
                            String returnEvents = "#";
                            ArrayList<Event> eventDB = venue1.getEventDatabase().getEvents();
                            for (int i = 0; i < eventDB.size(); i++) {
                                if (i == eventDB.size() - 1) {
                                    //$$ represents seperator between event names
                                    //%% is seperator between day and event name,
                                    // and @@ is seperator between day and time
                                    returnEvents += eventDB.get(i).getEventName() + "%%";
                                    returnEvents += eventDB.get(i).getDay() + "@@";
                                    returnEvents += eventDB.get(i).getTimeOfDay();
                                } else {
                                    returnEvents += eventDB.get(i).getEventName() + "%%";
                                    returnEvents += eventDB.get(i).getDay() + "@@";
                                    returnEvents += eventDB.get(i).getTimeOfDay() + "$$";
                                }
                            }
                            //another hashtag added at the end of the string
                            returnEvents += "#";

                            pr.println(returnEvents);
                            System.out.println("THE SERVER SENT OUT EVENT LIST"  + returnEvents);
                        } else if (nextInput.equals("LOGOUT")) {

                            loggedIn = false;
                            System.out.println("USER HAS SELECTED LOGOUT. THE LOGGEDIN BOOLEAN IS NOW" + loggedIn);
                        }

                        //TODO: add password reset ability


                    }
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

