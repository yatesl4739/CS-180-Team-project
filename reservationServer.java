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

    // venue has an events database
    // events database has a reservation database
    private static volatile Venue venue1 = new Venue();          // the venue the user is accessing
    private static volatile UserDatabase usrDB = new UserDatabase(); // a list of all users created

    private ServerSocket serverSocket;           // the serversocket used to connect to the client
    private boolean running = false;            // boolean value for checking if the server is running
    private ExecutorService pool;               // executorservice to manage threads of clilents

    private int port;                           // port used to connect to client
    private final ArrayList<Socket> clients = new ArrayList<>(); // list of clients

    /**
     * Creates the server with a specific port to connect to the client
     *
     * @param port port number
     */
    public reservationServer(int port) {
        this.port = port;
        try {
            serverSocket = new ServerSocket(port);
            pool = Executors.newCachedThreadPool();
            System.out.println("Server created on port " + port);
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
                synchronized (clients) {
                    clients.add(clientSocket);
                }
                System.out.println("Client connected. Active clients: " + getClientCount());
                pool.submit(new clientHandler(clientSocket));
            } catch (IOException e) {
                if (!running) {
                    break;
                }
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
     * encodes list of events into the "#name%%day@@time$$...#" format
     */
    private static String encodeEvents(ArrayList<Event> events) {
        StringBuilder sb = new StringBuilder("#");
        for (int i = 0; i < events.size(); i++) {
            Event ev = events.get(i);
            sb.append(ev.getEventName())
                    .append("%%")
                    .append(ev.getDay())
                    .append("@@")
                    .append(ev.getTimeOfDay());
            if (i < events.size() - 1) {
                sb.append("$$");
            }
        }
        sb.append("#");
        return sb.toString();
    }

    /**
     * Handles interaction between server and client
     */
    private class clientHandler implements Runnable {

        private final Socket socket;   // socket used to connect server and client

        private User clientUser;       // User object of the current client
        private boolean loggedIn;      // boolean value if the client has logged in
        private boolean isAdmin;       // admin flag

        public clientHandler(Socket socket) {
            this.socket = socket;
        }

        /**
         * Reads client's input and makes changes in server
         */
        public void run() {
            try {
                BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                PrintWriter pr = new PrintWriter(socket.getOutputStream(), true);

                // Send initial venue info and current events ONCE per connection
                System.out.println("GIVING OUT VENUE INFO");
                pr.println("VENUENAME:" + venue1.getVenueName());

                String initialEvents = encodeEvents(venue1.getEventDatabase().getEvents());
                System.out.println("THE SERVER IS SENDING INITIAL EVENTS: " + initialEvents);
                pr.println(initialEvents);

                loggedIn = false;
                isAdmin = false;
                clientUser = null;

                boolean keepRunningForClient = true;

                while (keepRunningForClient) {

                    // ---- LOGIN / SIGNUP PHASE ----
                    while (!loggedIn && keepRunningForClient) {
                        System.out.println("READY FOR NEXT OPTION. POSSIBLE NEXT OPTIONS: LOGIN, SIGNUP, EXIT");
                        String nextInput = br.readLine();
                        if (nextInput == null) {
                            // client disconnected
                            keepRunningForClient = false;
                            break;
                        }
                        System.out.println("THE CLIENT SENT (not logged in): " + nextInput);

                        if (nextInput.equals("LOGIN")) {
                            String username = br.readLine();
                            if (username == null) { keepRunningForClient = false; break; }
                            System.out.println("The server received: " + username + " as the username");

                            String password = br.readLine();
                            if (password == null) { keepRunningForClient = false; break; }
                            System.out.println("The server received: " + password + " as the password");

                            // Hard-coded admin login
                            if ("admin".equals(username) && "admin".equals(password)) {
                                pr.println("Success admin login");
                                loggedIn = true;
                                isAdmin = true;
                                clientUser = null;
                                System.out.println("Admin logged in.");
                            } else {
                                clientUser = usrDB.login(username, password);
                                if (clientUser.getUsername().equals("PASSWORD INCORRECT")) {
                                    pr.println("Error: password incorrect");
                                    clientUser = null;
                                } else if (clientUser.getUsername().equals("USER NOT FOUND")) {
                                    pr.println("Error: user not found");
                                    clientUser = null;
                                } else {
                                    pr.println("Success login with Username:" + username);
                                    loggedIn = true;
                                    isAdmin = false;
                                }
                                System.out.println("THE SERVER SENT BACK LOGIN STATUS TO CLIENT");
                            }

                        } else if (nextInput.equals("SIGNUP")) {
                            String username = br.readLine();
                            if (username == null) { keepRunningForClient = false; break; }
                            System.out.println("The server received: " + username + " as the username");

                            String password = br.readLine();
                            if (password == null) { keepRunningForClient = false; break; }
                            System.out.println("The server received: " + password + " as the password");

                            clientUser = usrDB.signUp(username, password);
                            if (clientUser.getUsername().equals("INVALID USERNAME")) {
                                pr.println("Error: Invalid Username");
                                clientUser = null;
                            } else if (clientUser.getUsername().equals("USERNAME TAKEN")) {
                                pr.println("Error: Username has been taken");
                                clientUser = null;
                            } else if (clientUser.getUsername().equals("PASSWORD INVALID")) {
                                pr.println("Error: Password is invalid");
                                clientUser = null;
                            } else {
                                pr.println("Success sign up and login with Username:" + username);
                                loggedIn = true;
                                isAdmin = false;
                            }
                            System.out.println("THE SERVER SENT BACK SIGNUP STATUS TO CLIENT");

                        } else if (nextInput.equals("EXIT")) {
                            keepRunningForClient = false;
                            break;
                        } else {
                            // Ignore random commands when not logged in
                            pr.println("Error: Must LOGIN or SIGNUP first.");
                        }
                    }

                    if (!keepRunningForClient) {
                        break;
                    }

                    System.out.println("User is logged in. Next possible inputs: NEW, VIEW, EVENTS, LOGOUT, EXIT (or CREATE_EVENT for admin)");

                    // ---- COMMAND PHASE (LOGGED IN) ----
                    while (loggedIn && keepRunningForClient) {
                        String nextInput = br.readLine();
                        if (nextInput == null) {
                            keepRunningForClient = false;
                            break;
                        }
                        System.out.println("Server received from client (logged in): " + nextInput);

                        // ADMIN COMMANDS
                        if (isAdmin) {
                            if (nextInput.equals("CREATE_EVENT")) {
                                String eventName = br.readLine();
                                double basePrice = Double.parseDouble(br.readLine());
                                long day = Long.parseLong(br.readLine());
                                long timeOfDay = Long.parseLong(br.readLine());
                                int rows = Integer.parseInt(br.readLine());
                                int cols = Integer.parseInt(br.readLine());

                                char[][] seating = new char[rows][cols];
                                for (int i = 0; i < rows; i++) {
                                    for (int j = 0; j < cols; j++) {
                                        seating[i][j] = 'o'; // all seats open initially
                                    }
                                }

                                Event newEvent = new Event(eventName, basePrice, seating, timeOfDay, day);
                                venue1.getEventDatabase().addEvent(newEvent);

                                pr.println("Event created: " + eventName);
                                System.out.println("Admin created event: " + eventName);

                            } else if (nextInput.equals("EVENTS")) {
                                String returnEvents = encodeEvents(venue1.getEventDatabase().getEvents());
                                pr.println(returnEvents);
                                System.out.println("Admin requested events: " + returnEvents);

                            } else if (nextInput.equals("LOGOUT")) {
                                // Admin logout: keep socket open, go back to login phase
                                loggedIn = false;
                                isAdmin = false;
                                clientUser = null;
                                pr.println("Logout successful");
                                System.out.println("Admin logged out.");
                                break; // break out of logged-in loop, back to login loop

                            } else if (nextInput.equals("EXIT")) {
                                keepRunningForClient = false;
                                break;

                            } else {
                                pr.println("Admin commands: CREATE_EVENT, EVENTS, LOGOUT, or EXIT.");
                            }

                            continue;
                        }

                        // USER COMMANDS (non-admin)

                        if (nextInput.equals("NEW")) {
                            // make a new reservation

                            ArrayList<Event> eventDB = venue1.getEventDatabase().getEvents();
                            String returnEvents = encodeEvents(eventDB);

                            // send list of events
                            pr.println(returnEvents);
                            System.out.println("THE SERVER SENT OUT EVENT LIST " + returnEvents);

                            // user selects event (1-based index) or CANCEL
                            String rawInput = br.readLine();
                            System.out.println("User selection raw input: " + rawInput);

                            if (rawInput == null) {
                                keepRunningForClient = false;
                                break;
                            }
                            if (rawInput.equals("********CANCEL********")) {
                                continue; // go back to waiting for next command
                            }

                            int eventSelect;
                            try {
                                eventSelect = Integer.parseInt(rawInput) - 1;
                            } catch (NumberFormatException e) {
                                pr.println("Error: invalid event selection");
                                continue;
                            }

                            if (eventSelect < 0 || eventSelect >= eventDB.size()) {
                                pr.println("Error: event index out of bounds");
                                continue;
                            }

                            Event selectedEvent = eventDB.get(eventSelect);
                            System.out.println("User gave back " + eventSelect + " as the selection");

                            // Send seating chart
                            char[][] seatingChart = selectedEvent.getSeatingChart();
                            StringBuilder outputSeatingChart = new StringBuilder();

                            for (int i = 0; i < seatingChart.length; i++) {
                                outputSeatingChart.append("[");
                                for (int j = 0; j < seatingChart[i].length; j++) {
                                    outputSeatingChart.append(seatingChart[i][j]);
                                    if (j < seatingChart[i].length - 1) {
                                        outputSeatingChart.append(",");
                                    }
                                }
                                outputSeatingChart.append("\n");
                            }
                            pr.println(outputSeatingChart.toString());
                            pr.println("ENDCHART");

                            System.out.println("SERVER SENT OUT SEATING CHART");

                            // Get number of people (which is just seat count in current client code)
                            rawInput = br.readLine();
                            if (rawInput == null) {
                                keepRunningForClient = false;
                                break;
                            }
                            if (rawInput.equals("********CANCEL********")) {
                                continue;
                            }
                            int numPeople = Integer.parseInt(rawInput);

                            // Client also sends back chosenIndex; we read it to keep protocol aligned
                            String chosenIndexStr = br.readLine();
                            if (chosenIndexStr == null) {
                                keepRunningForClient = false;
                                break;
                            }

                            // time/date pulled from selectedEvent
                            long time = selectedEvent.getTimeOfDay();
                            long date = selectedEvent.getDay();

                            System.out.println("Number of people entered " + numPeople
                                    + " time entered: " + time + " date entered " + date);

                            // Seat selection input: x1,y1,x2,y2,...
                            System.out.println("Server waits for seat input in x1,y1,x2,y2 format");
                            String seatsLine = br.readLine();
                            if (seatsLine == null) {
                                keepRunningForClient = false;
                                break;
                            }
                            if (seatsLine.equals("********CANCEL********")) {
                                continue;
                            }

                            System.out.println("Server received " + seatsLine + " as seat input");

                            String[] xy = seatsLine.split(",");
                            int[] x = new int[xy.length / 2];
                            int[] y = new int[xy.length / 2];

                            for (int i = 0; i < xy.length; i++) {
                                if (i % 2 == 0) {
                                    x[i / 2] = Integer.parseInt(xy[i]);
                                } else {
                                    y[i / 2] = Integer.parseInt(xy[i]);
                                }
                            }

                            boolean success = selectedEvent.createReservation(
                                    x, y, clientUser, numPeople, time, date
                            );
                            if (success) {
                                pr.println("Reservation created successfully.");
                            } else {
                                pr.println("Failed to create reservation (seat conflict or invalid).");
                            }

                        } else if (nextInput.equals("VIEW")) {
                            // user can see all their reservations
                            ArrayList<Reservation> userReservations = new ArrayList<>();

                            for (int i = 0; i < venue1.getEventDatabase().getEvents().size(); i++) {
                                ArrayList<Reservation> resDBTemp = venue1.getEventDatabase()
                                        .getEvents()
                                        .get(i)
                                        .getReservationDB()
                                        .getReservations();
                                for (int j = 0; j < resDBTemp.size(); j++) {
                                    if (resDBTemp.get(j).getUser().getUsername()
                                            .equals(clientUser.getUsername())) {
                                        userReservations.add(resDBTemp.get(j));
                                    }
                                }
                            }

                            // provide string of all reservations
                            StringBuilder returnString = new StringBuilder();
                            for (int i = 0; i < userReservations.size(); i++) {
                                returnString.append(userReservations.get(i).toString());
                                if (i < userReservations.size() - 1) {
                                    returnString.append("$$");
                                }
                            }

                            System.out.println(returnString.toString());
                            pr.println(returnString.toString());
                            pr.println("END_OF_USER_EVENT_DETAILS**");
                            System.out.println("SERVER SENT OUT USER RESERVATIONS");

                        } else if (nextInput.equals("EVENTS")) {
                            // view events on demand
                            String returnEvents = encodeEvents(venue1.getEventDatabase().getEvents());
                            pr.println(returnEvents);
                            System.out.println("THE SERVER SENT OUT EVENT LIST " + returnEvents);

                        } else if (nextInput.equals("LOGOUT")) {
                            // go back to login
                            loggedIn = false;
                            isAdmin = false;
                            clientUser = null;
                            pr.println("Logout successful");
                            System.out.println("USER HAS SELECTED LOGOUT. loggedIn is now " + loggedIn);
                            break; // break out of logged-in loop, back to login loop

                        } else if (nextInput.equals("EXIT")) {
                            keepRunningForClient = false;
                            break;

                        } else {
                            pr.println("Unknown command. Valid: NEW, VIEW, EVENTS, LOGOUT, EXIT");
                        }
                    } // end while (loggedIn && keepRunningForClient)

                } // end outer while (keepRunningForClient)

            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    socket.close();
                } catch (IOException ignored) {}

                synchronized (clients) {
                    clients.remove(socket);
                }
                System.out.println("Client disconnected. Active clients: " + getClientCount());
            }
        }
    }
}