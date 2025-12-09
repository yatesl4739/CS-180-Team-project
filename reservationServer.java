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
    private static volatile UserDatabase usrDB = new UserDatabase();  // list of all users created

    private ServerSocket serverSocket;  // the serversocket used to connect to the client
    private boolean running = false;    // boolean value for checking if the server is running
    private ExecutorService pool;       // executorservice to manage threads of clients

    private int port;                   // port used to connect to client

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
                if (!running) break;
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
        private boolean loggedIn; // boolean value if the client has logged in
        private boolean isAdmin;

        public clientHandler(Socket socket) {
            this.socket = socket;
        }

        /**
         * Reads client's input and makes changes in server
         */
        public void run() {

            try {
                BufferedReader br = new BufferedReader(
                        new InputStreamReader(socket.getInputStream()));
                PrintWriter pr = new PrintWriter(socket.getOutputStream(), true);

                // Send venue info and initial events once
                System.out.println("GIVING OUT VENUE INFO");
                pr.println("VENUENAME:" + venue1.getVenueName());

                String returnEvents = encodeEvents();
                pr.println(returnEvents);
                System.out.println("INITIAL EVENT INFO SENT: " + returnEvents);

                String nextInput;

                // LOGIN / SIGNUP phase
                do {
                    System.out.println("READY FOR NEXT OPTION. POSSIBLE NEXT OPTIONS: LOGIN, SIGNUP");
                    nextInput = br.readLine();
                    if (nextInput == null) {
                        socket.close();
                        return;
                    }

                    System.out.println("THE CLIENT SENT BACK: " + nextInput);

                    if (nextInput.equals("LOGIN")) {
                        String username = br.readLine();
                        String password = br.readLine();

                        System.out.println("The server received: " + username + " / " + password);

                        if ("admin".equals(username) && "admin".equals(password)) {
                            pr.println("Success admin login");
                            loggedIn = true;
                            isAdmin = true;
                            System.out.println("Admin logged in.");
                        } else {
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
                        }

                    } else if (nextInput.equals("SIGNUP")) {
                        String username = br.readLine();
                        String password = br.readLine();

                        System.out.println("The server received signup: " + username + " / " + password);

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

                System.out.println("User logged in. Waiting for commands: NEW, VIEW, EVENTS, CREATE_EVENT (admin), LOGOUT.");

                // MAIN COMMAND LOOP WHILE LOGGED IN
                while (loggedIn) {
                    nextInput = br.readLine();
                    if (nextInput == null) {
                        break;
                    }

                    System.out.println("Server received from client: " + nextInput);

                    // Handle admin-only commands first
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
                            continue;
                        } else if (nextInput.equals("LOGOUT")) {
                            loggedIn = false;
                            isAdmin = false;
                            System.out.println("Admin logged out.");
                            break;
                        } else {
                            // For safety, ignore other commands in admin mode (client UI hides them)
                            System.out.println("Admin issued unsupported command: " + nextInput);
                            continue;
                        }
                    }

                    // Normal user commands
                    if (nextInput.equals("NEW")) {
                        handleNewReservation(br, pr);
                    } else if (nextInput.equals("VIEW")) {
                        handleViewReservations(pr);
                    } else if (nextInput.equals("EVENTS")) {
                        String evStr = encodeEvents();
                        pr.println(evStr);
                        System.out.println("THE SERVER SENT OUT EVENT LIST " + evStr);
                    } else if (nextInput.equals("CREATE_EVENT")) {
                        // Normal user shouldn't be doing this, but if they do, respond once
                        pr.println("Error: only admin can create events.");
                        System.out.println("Non-admin tried CREATE_EVENT");
                    } else if (nextInput.equals("LOGOUT")) {
                        loggedIn = false;
                        System.out.println("USER HAS SELECTED LOGOUT. loggedIn is now " + loggedIn);
                        break;
                    } else if (nextInput.equals("EXIT")) {
                        loggedIn = false;
                        System.out.println("Client requested EXIT.");
                        break;
                    } else {
                        System.out.println("Unknown command: " + nextInput);
                    }
                }

                socket.close();
                System.out.println("Client disconnected.");

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        private static String encodeEvents() {
            StringBuilder sb = new StringBuilder("#");
            ArrayList<Event> eventDB = venue1.getEventDatabase().getEvents();
            for (int i = 0; i < eventDB.size(); i++) {
                Event ev = eventDB.get(i);
                sb.append(ev.getEventName()).append("%%");
                sb.append(ev.getDay()).append("@@");
                sb.append(ev.getTimeOfDay());
                if (i < eventDB.size() - 1) {
                    sb.append("$$");
                }
            }
            sb.append("#");
            return sb.toString();
        }

        private void handleNewReservation(BufferedReader br, PrintWriter pr) throws IOException {
            // Show events to pick from
            String returnEvents = encodeEvents();
            pr.println(returnEvents);
            System.out.println("THE SERVER SENT OUT EVENT LIST " + returnEvents);

            System.out.println("User is asked to pick an event");

            String rawInput = br.readLine();
            System.out.println("Event selection raw input: " + rawInput);

            if (rawInput == null || rawInput.equals("********CANCEL********")) {
                return;
            }

            int eventSelect = Integer.parseInt(rawInput) - 1;
            System.out.println("User gave back " + eventSelect + " as the selection");

            ArrayList<Event> eventDB = venue1.getEventDatabase().getEvents();
            if (eventSelect < 0 || eventSelect >= eventDB.size()) {
                pr.println("Reservation failed: invalid event selected.");
                return;
            }

            // send seating chart
            char[][] seatingChart = eventDB.get(eventSelect).getSeatingChart();
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

            rawInput = br.readLine();
            if (rawInput == null || rawInput.equals("********CANCEL********")) {
                return;
            }

            int numPeople = Integer.parseInt(rawInput);
            int chosenIndex = Integer.parseInt(br.readLine());

            long time = eventDB.get(chosenIndex).getTimeOfDay();
            long date = eventDB.get(chosenIndex).getDay();

            System.out.println("Number of people: " + numPeople +
                    " time: " + time + " date: " + date);

            String seatsInput = br.readLine();
            if (seatsInput == null || seatsInput.equals("********CANCEL********")) {
                return;
            }
            System.out.println("Server received seats: " + seatsInput);

            String[] xy = seatsInput.split(",");
            int[] x = new int[xy.length / 2];
            int[] y = new int[xy.length / 2];

            for (int i = 0; i < xy.length; i++) {
                if (i % 2 == 0) {
                    x[i / 2] = Integer.parseInt(xy[i]);
                } else {
                    y[i / 2] = Integer.parseInt(xy[i]);
                }
            }

            boolean success = eventDB.get(eventSelect)
                    .createReservation(x, y, clientUser, numPeople, time, date);

            if (success) {
                pr.println("Reservation created successfully.");
                System.out.println("Reservation created successfully.");
            } else {
                pr.println("Reservation failed: invalid seat(s) or data.");
                System.out.println("Reservation failed.");
            }
        }

        private void handleViewReservations(PrintWriter pr) {
            ArrayList<Reservation> userReservations = new ArrayList<>();

            for (int i = 0; i < venue1.getEventDatabase().getEvents().size(); i++) {
                ArrayList<Reservation> resDBTemp = venue1.getEventDatabase()
                        .getEvents().get(i).getReservationDB().getReservations();
                for (int j = 0; j < resDBTemp.size(); j++) {
                    if (resDBTemp.get(j).getUser().getUsername()
                            .equals(clientUser.getUsername())) {
                        userReservations.add(resDBTemp.get(j));
                    }
                }
            }

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
        }
    }
}