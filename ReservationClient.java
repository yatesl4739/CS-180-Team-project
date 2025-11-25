import java.io.*;
import java.net.Socket;
import java.util.Scanner;

/**
 * ReservationClient
 *
 * This program allows a client to connect to the reservationServer
 * to log in, sign up, create new reservations, and view existing
 * reservations.
 *
 * @author Meraj Syeda & Krish Talati, Lab Sec L12
 * @version 11/24/25
 */

public class ReservationClient {

    private static String host = "localhost";   // host address for server
    private static int port = 4242;             // port number for server

    /**
     * Connects the client to the server and handles all
     * user interaction until logout or exit.
     *
     * @param args not used
     */
    public static void main(String[] args) {
        try (Socket socket = new Socket(host, port)) {

            BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter pr = new PrintWriter(socket.getOutputStream(), true);
            Scanner sc = new Scanner(System.in);

            // Reads venue name sent by server
            String venueLine = br.readLine();
            System.out.println("Connected to server. " + venueLine);

            // Reads initial events list from server once connected
            {
                String raw = br.readLine();

                // Remove surrounding # symbols
                if (raw.startsWith("#") && raw.endsWith("#")) {
                    raw = raw.substring(1, raw.length() - 1);
                }

                // Split event entries
                String[] eventArr = raw.split("\\$\\$");

                System.out.println("\nEvents:");
                for (int i = 0; i < eventArr.length; i++) {

                    String e = eventArr[i];
                    String[] nameDay = e.split("%%");
                    String name = nameDay[0];

                    String[] dayTime = nameDay[1].split("@@");
                    String day = dayTime[0];
                    String time = dayTime[1];

                    System.out.println((i + 1) + ") " + name + " on " + day + " at " + time);
                }
            }

            // Ensures user enters LOGIN or SIGNUP successfully
            boolean goodToGo = false;

            do {
                System.out.println("Enter command: LOGIN / SIGNUP");
                String cmd = sc.nextLine().toUpperCase().trim();

                if (cmd.equals("LOGIN") || cmd.equals("SIGNUP")) {
                    pr.println(cmd);  // send command to server
                }

                // LOGIN handling
                if (cmd.equals("LOGIN")) {
                    goodToGo = true;

                    while (true) {
                        System.out.println("Username: ");
                        pr.println(sc.nextLine());

                        System.out.println("Password: ");
                        pr.println(sc.nextLine());

                        String response = br.readLine();
                        System.out.println(response);

                        if (response.startsWith("Success")) {
                            break;
                        }
                    }
                }

                // SIGNUP handling
                else if (cmd.equals("SIGNUP")) {
                    goodToGo = true;

                    while (true) {
                        System.out.println("Create Username: ");
                        pr.println(sc.nextLine());

                        System.out.println("Create Password: ");
                        pr.println(sc.nextLine());

                        String response = br.readLine();
                        System.out.println(response);

                        if (response.startsWith("Success")) {
                            break;
                        }
                    }
                } else {
                    System.out.println("Invalid input.");
                }

            } while (!goodToGo);

            boolean loggedIn = true;

            // Loop continues while user is logged in
            while (loggedIn) {

                // Ask for next action
                System.out.println("Next command: NEW / VIEW / EVENTS / LOGOUT / EXIT");
                String next = sc.nextLine().toUpperCase().trim();
                pr.println(next);

                // NEW reservation flow
                if (next.equals("NEW")) {

                    // Read events list for choosing
                    String rawEvents = br.readLine();
                    System.out.println("\nAvailable Events:");

                    if (rawEvents.startsWith("#") && rawEvents.endsWith("#")) {
                        rawEvents = rawEvents.substring(1, rawEvents.length() - 1);
                    }

                    String[] eventArr = rawEvents.split("\\$\\$");

                    // Print events
                    for (int i = 0; i < eventArr.length; i++) {
                        String e = eventArr[i];

                        String[] nameDay = e.split("%%");
                        String name = nameDay[0];

                        String[] dayTime = nameDay[1].split("@@");
                        String day = dayTime[0];
                        String time = dayTime[1];

                        System.out.println((i + 1) + ") " + name + " on " + day + " at " + time);
                    }

                    // Send chosen event number
                    System.out.print("Select event number: ");
                    int choice = Integer.parseInt(sc.nextLine());
                    pr.println(choice);

                    // Seating chart output
                    System.out.println("\nSeating Chart (o = open, x = taken): ");
                    String line = br.readLine();

                    // Print full 2D seating chart until ENDCHART signal
                    while (line != null && !line.equals("ENDCHART")) {
                        System.out.println(line);
                        line = br.readLine();
                    }

                    // Send number of people
                    System.out.print("Number of people: ");
                    String people = sc.nextLine();
                    pr.println(people);

                    // Send timestamp info
                    System.out.print("Time (long): ");
                    String t = sc.nextLine();
                    pr.println(t);

                    System.out.print("Date (long): ");
                    String d = sc.nextLine();
                    pr.println(d);

                    // Seat coordinates
                    System.out.print("Enter seats as x1,y1,x2,y2,... : ");
                    String seats = sc.nextLine();
                    pr.println(seats);

                    System.out.println("Reservation sent.\n");


                }
                // VIEW reservations
                else if (next.equals("VIEW")) {
                    String raw = br.readLine();

                    if (raw == null || raw.trim().isEmpty()) {
                        System.out.println("No reservations found.");
                    } else {
                        String[] resArr = raw.split("\\$\\$");
                        System.out.println("\nYour Reservations:");
                        for (String r : resArr) {
                            System.out.println("- " + r);
                        }
                    }

                }
                // EVENTS listing again
                else if (next.equals("EVENTS")) {
                    String raw = br.readLine();

                    if (raw.startsWith("#") && raw.endsWith("#")) {
                        raw = raw.substring(1, raw.length() - 1);
                    }

                    String[] eventArr = raw.split("\\$\\$");

                    System.out.println("\nEvents:");
                    for (int i = 0; i < eventArr.length; i++) {

                        String e = eventArr[i];
                        String[] nameDay = e.split("%%");
                        String name = nameDay[0];

                        String[] dayTime = nameDay[1].split("@@");
                        String day = dayTime[0];
                        String time = dayTime[1];

                        System.out.println((i + 1) + ") " + name + " on " + day + " at " + time);
                    }

                }
                // LOGOUT
                else if (next.equals("LOGOUT")) {
                    loggedIn = false;
                }

                // EXIT closes client
                if (next.equals("EXIT")) {
                    return;
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
