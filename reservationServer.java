import javax.naming.spi.ResolveResult;
import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.concurrent.*;


public class reservationServer implements Runnable, ReservationServerInterface {
    //venue has an events database
    //events database has a reservation database
    private static volatile Venue venue1 = new Venue();
    private static volatile UserDatabase usrDB = new UserDatabase();

    private ServerSocket serverSocket;
    private boolean running = false;
    private ExecutorService pool;

    private int port;
    private final ArrayList<Socket> clients = new ArrayList<>();

    public reservationServer(int port)  {

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

    public void start() {

        if (running) {
            throw new IllegalStateException("Server is already running");
        }
        running = true;
        new Thread(this).start();
    }

    public void stop() {
        running = false;
        try {
            serverSocket.close();
        } catch (IOException ignored) {}
        pool.shutdownNow();
    }

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

    public int getPort() {
        return port;
    }

    public boolean isRunning() {
        return running;
    }

    public int getClientCount() {
        synchronized (clients) {
            return clients.size();
        }
    }

    public Venue getVenue1() {
        return venue1;
    }

    public UserDatabase getUserDB() {
        return usrDB;
    }



    private static class clientHandler implements Runnable {
        private Socket socket;
        private User clientUser;

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
                pr.println("VENUENAME:" + venue1.getVenueName());


                //view events

                //single hashtag represents the start of the string
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
                            returnEvents += eventDB.get(i).getTimeOfDay() + "$$";
                        } else {
                            returnEvents += eventDB.get(i).getEventName() + "%%";
                            returnEvents += eventDB.get(i).getDay() + "@@";
                            returnEvents += eventDB.get(i).getTimeOfDay();
                        }
                    }
                    //another hashtag added at the end of the string
                    returnEvents += "#";

                    pr.println(returnEvents);
                }

                //client would see something like options to do:
                //login, sign up



                String nextInput = br.readLine();

                //login:
                if (nextInput.equals("LOGIN")) {

                    boolean good = false;

                    do {
                        String username = br.readLine();
                        String password = br.readLine();

                        clientUser = usrDB.login(username, password);
                        if (clientUser.getUsername().equals("PASSWORD INCORRECT")) {
                            pr.println("Error: password incorrect");
                        } else if (clientUser.getUsername().equals("USER NOT FOUND")) {
                            pr.println("Error: user not found");
                        } else {
                            pr.println("Success login with Username:" + username);
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
                            pr.println("Error: Invalid Username");

                        } else if (clientUser.getUsername().equals("USERNAME TAKEN")) {
                            pr.println("Error: Username has been taken");
                        } else if (clientUser.getUsername().equals("PASSWORD INVALID")) {
                            pr.println("Error: Password is invalid");

                        } else {
                            pr.println("Success sign up and login with Username:" + username);
                            good = true;
                        }
                    }
                    while (!good);

                }


                //move onto next possible selection
                //make reservation, see users current reservations, see events, change password maybe

                //get next selection input
                nextInput = br.readLine();

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

                    pr.println(returnEvents);

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

                    pr.println(returnString);
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

                    pr.println(returnEvents);
                }
                //TODO: add password reset ability




                //TODO: make everything above a loop so a user can stay in the thingy


            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

