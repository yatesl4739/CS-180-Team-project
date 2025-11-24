import javax.naming.spi.ResolveResult;
import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.concurrent.*;


public class reservationServer {
    //venue has an events database
    //events database has a reservation database
    private static volatile Venue venue1 = new Venue();
    private static volatile UserDatabase usrDB = new UserDatabase();


    public static void main(String[] args) {

        try {
            System.out.println("Creating server");
            ExecutorService pool = Executors.newCachedThreadPool();
            ServerSocket serverSocket = new ServerSocket(4242);
            System.out.println("Server has been created!");

            while (true) {
                //open the server on port 5000


                System.out.println("Waiting for client to connect...");

                //forever look for connections:
                while (true) {
                    Socket connectedClient = serverSocket.accept();


                    //create a instance of the client handler and add it to the thread pool
                    pool.submit(new clientHandler(connectedClient));


                }


            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    private static class clientHandler implements Runnable {
        private Socket socket;
        private User clientUser;
        private boolean loggedIn;

        public clientHandler(Socket socket) {
            this.socket = socket;
        }

        public void run() {
            //DO THE CLIENT STUFF

            try {
                BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));

                //Print writer is really for testing use. We would be most likely passing some data somehow
                PrintWriter pr = new PrintWriter(socket.getOutputStream(), true);


                //EVERYTHING BELOW THIS LINE IS COMMUNICATION TO A CERTAIN CLIENT INSTANCE.

                //GIVE THE CLIENT THE VENUE INFO
                pr.write("VENUENAME:" + venue1.getVenueName());

                //client would see something like options to do:
                //login, sign up, or view events

                String nextInput = br.readLine();

                //login:
                if (nextInput.equals("LOGIN")) {

                    boolean good = false;

                    do {
                        String username = br.readLine();
                        String password = br.readLine();

                        clientUser = usrDB.login(username, password);
                        if (clientUser.getUsername().equals("PASSWORD INCORRECT")) {
                            pr.write("Error: password incorrect");
                        } else if (clientUser.getUsername().equals("USER NOT FOUND")) {
                            pr.write("Error: user not found");
                        } else {
                            pr.write("Success login with Username:" + username);
                            loggedIn = true;
                            good = true;
                        }
                    }
                    while (!good);

                    //SIGNUP
                } else if (nextInput.equals("SIGNUP")) {

                    boolean good = false;

                    do {

                        String username = br.readLine();
                        String password = br.readLine();


                        clientUser = usrDB.signUp(username, password);
                        if (clientUser.getUsername().equals("INVALID USERNAME")) {
                            pr.write("Error: Invalid Username");

                        } else if (clientUser.getUsername().equals("USERNAME TAKEN")) {
                            pr.write("Error: Username has been taken");
                        } else if (clientUser.getUsername().equals("PASSWORD INVALID")) {
                            pr.write("Error: Password is invalid");

                        } else {
                            pr.write("Success sign up and login with Username:" + username);
                            loggedIn = true;
                            good = true;
                        }
                    }
                    while (!good);

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
                            returnEvents += eventDB.get(i).getTimeOfDay() + "$$";
                        } else {
                            returnEvents += eventDB.get(i).getEventName() + "%%";
                            returnEvents += eventDB.get(i).getDay() + "@@";
                            returnEvents += eventDB.get(i).getTimeOfDay();
                        }
                    }
                    //another hashtag added at the end of the string
                    returnEvents += "#";

                    pr.write(returnEvents);
                }

                while (loggedIn) {

                    //move onto next possible selection
                    //make reservation, see users current reservations, see events, change password maybe

                    //get next selection input
                    if (br.readLine().equals("SECOND_COMMAND")) {
                        nextInput = br.readLine();
                    } else {
                        break;
                    }

                    if (nextInput.equals("NEW")) {
                        //make a new reservation

                        //show events for the user to pick

                        String returnEvents = "#";
                        ArrayList<Event> eventDB = venue1.getEventDatabase().getEvents();
                        for (int i = 0; i < eventDB.size(); i++) {
                            if (i == eventDB.size() - 1) {
                                //$$ represents seperator between event names
                                //%% is seperator between day and event name,
                                // and @@ is seperator between day and time
                                returnEvents += eventDB.get(i).getEventName() + "%%";
                                returnEvents += eventDB.get(i).getDay() + "@@";
                                returnEvents += eventDB.get(i).getTimeOfDay() + "$$";
                            } else {
                                returnEvents += eventDB.get(i).getEventName() + "%%";
                                returnEvents += eventDB.get(i).getDay() + "@@";
                                returnEvents += eventDB.get(i).getTimeOfDay();
                            }
                        }
                        //another hashtag added at the end of the string
                        returnEvents += "#";

                        pr.write(returnEvents);

                        //-1 to make it an index becauase the client side will select starting at 1.
                        int eventSelect = Integer.parseInt(br.readLine()) - 1;

                        //give them the seating chart

                        char[][] seatingChart = venue1.getEventDatabase().getEvents()
                                .get(eventSelect).getSeatingChart();

                        String outputSeatingChart = "";

                        for (int i = 0; i < seatingChart.length; i++) {
                            for (int j = 0; j < seatingChart[i].length; j++) {
                                outputSeatingChart += seatingChart[i][j];

                            }
                            outputSeatingChart += "\n";
                        }

                        int numPeople = Integer.parseInt(br.readLine());

                        long time = Long.parseLong(br.readLine());

                        long date = Long.parseLong(br.readLine());


                        //Seat selection input can come in as
                        //x1,y1,x2,y2,x3,y3
                        nextInput = br.readLine();

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
                                if (resDBTemp.get(i).getUser().getUsername().equals(clientUser.getUsername())) {
                                    userReservations.add(resDBTemp.get(i));
                                }
                            }
                        }


                        //provideString of all reservations
                        String returnString = "";
                        for (int i = 0; i < userReservations.size(); i++) {
                            //$$ marks the space between reservation strings
                            if (i < userReservations.size() - 1) {
                                returnString += userReservations.get(i).toString() + "$$";
                            } else {
                                //no $$ on this one beacuse its the last entry
                                returnString += userReservations.get(i).toString();
                            }

                        }

                        pr.write(returnString);
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
                                returnEvents += eventDB.get(i).getTimeOfDay() + "$$";
                            } else {
                                returnEvents += eventDB.get(i).getEventName() + "%%";
                                returnEvents += eventDB.get(i).getDay() + "@@";
                                returnEvents += eventDB.get(i).getTimeOfDay();
                            }
                        }
                        //another hashtag added at the end of the string
                        returnEvents += "#";

                        pr.write(returnEvents);
                    }

                    else if (nextInput.equals("LOGOUT")) {
                        loggedIn = false;
                    }

                    //TODO: add password reset ability


                    //TODO: make everything above a loop so a user can stay in the thingy

                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

