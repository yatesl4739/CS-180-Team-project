import java.io.*;
import java.net.Socket;
import java.util.Scanner;

/**
 * ReservationDatabase
 *
 * @author Meraj Syeda & Krish Talati
 * @version 11/23/25
 */

public class ReservationClient {

    private static String host = "localhost";
    private static int port = 4242;

    public static void main(String[] args) {
        try (Socket socket = new Socket(host, port)) {

            BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter pr = new PrintWriter(socket.getOutputStream(), true);
            Scanner sc = new Scanner(System.in);

            // read the venue name from server
            String venueLine = br.readLine();
            System.out.println("Connected to server. " + venueLine);

            //GRAB INCOMING EVENTS
            {
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

            // initial command
            boolean goodToGo = false;

            do {
                System.out.println("Enter command: LOGIN / SIGNUP");
                String cmd = sc.nextLine().toUpperCase().trim();

                if (cmd.equals("LOGIN") || cmd.equals("SIGNUP")) {
                    pr.println(cmd);
                }


                // LOGIN
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

                // SIGNUP
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
            }
            while (!goodToGo);

            boolean loggedIn = true;

            while (loggedIn) {

                // second command
                System.out.println("Next command: NEW / VIEW / EVENTS / LOGOUT / EXIT");
                String next = sc.nextLine().toUpperCase().trim();
                pr.println(next);

                // NEW reservation
                if (next.equals("NEW")) {

                    String rawEvents = br.readLine();
                    System.out.println("\nAvailable Events:");

                    if (rawEvents.startsWith("#") && rawEvents.endsWith("#")) {
                        rawEvents = rawEvents.substring(1, rawEvents.length() - 1);
                    }

                    String[] eventArr = rawEvents.split("\\$\\$");

                    for (int i = 0; i < eventArr.length; i++) {
                        String e = eventArr[i];

                        String[] nameDay = e.split("%%");
                        String name = nameDay[0];

                        String[] dayTime = nameDay[1].split("@@");
                        String day = dayTime[0];
                        String time = dayTime[1];

                        System.out.println((i + 1) + ") " + name + " on " + day + " at " + time);
                    }

                    System.out.print("Select event number: ");
                    int choice = Integer.parseInt(sc.nextLine());
                    pr.println(choice);

                    System.out.println("\nSeating Chart:");
                    String line = br.readLine();

                    while (line != null && !line.equals("ENDCHART")) {
                        System.out.println(line);
                        line = br.readLine();
                    }

                    System.out.print("Number of people: ");
                    String people = sc.nextLine();
                    pr.println(people);

                    System.out.print("Time (long): ");
                    String t = sc.nextLine();
                    pr.println(t);

                    System.out.print("Date (long): ");
                    String d = sc.nextLine();
                    pr.println(d);

                    System.out.print("Enter seats as x1,y1,x2,y2,... : ");
                    String seats = sc.nextLine();
                    pr.println(seats);

                    System.out.println("Reservation sent.\n");


                } else if (next.equals("VIEW")) {
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

                } else if (next.equals("EVENTS")) {
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
                } else if (next.equals("LOGOUT")) {
                    loggedIn = false;
                }

                if (next.equals("EXIT")) {
                    return;
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}