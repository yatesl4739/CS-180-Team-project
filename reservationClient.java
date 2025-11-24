import java.io.*;
import java.net.Socket;
import java.util.Scanner;

/**
 * ReservationDatabase
 *
 * @author Meraj Syeda & Krish Talati
 *
 * @version 11/23/25
 *
 */

public class reservationClient implements Runnable {

    private String host;
    private int port;

    public reservationClient(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public static void main(String[] args) {
        reservationClient client = new reservationClient("localhost", 4242);
        client.run();
    }

    @Override
    public void run() {
        try (Socket socket = new Socket(host, port)) {

            BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter pr = new PrintWriter(socket.getOutputStream(), true);
            Scanner sc = new Scanner(System.in);

            // read the venue name from server
            String venueLine = br.readLine();
            System.out.println("Connected to server. " + venueLine);

            // initial command
            System.out.println("Enter command: LOGIN / SIGNUP");
            String cmd = sc.nextLine().toUpperCase().trim();

            while (!(cmd.equals("LOGIN") || cmd.equals("SIGNUP"))) {
                System.out.println("Invalid input");
                System.out.println("Enter command: LOGIN / SIGNUP");
                cmd = sc.nextLine().toUpperCase().trim();
            }

            pr.println(cmd);

            // LOGIN
            if (cmd.equals("LOGIN")) {
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
            }

            /* shouldn't be an option bc otherwise user will not sign in

            // EVENTS
            else if (cmd.equals("EVENTS")) {
                String events = br.readLine();
                System.out.println("Events: " + events);
            }

             */

            boolean loggedIn = true;

            while (loggedIn) {

                // I don't think this is needed actually?
                pr.println("SECOND_COMMAND");

                // second command
                System.out.println("Next command: NEW / VIEW / EVENTS / LOGOUT");
                String next = sc.nextLine().toUpperCase().trim();
                pr.println(next);

                // NEW reservation - Krish take over here!

                // Logout
                if (next.equals("LOGOUT")) {
                    loggedIn = false;
                }

            }

            // stop here!
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
