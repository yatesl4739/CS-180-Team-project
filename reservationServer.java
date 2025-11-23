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


                    String username = br.readLine();
                    String password = br.readLine();

                    clientUser = usrDB.login(username, password);
                    if (clientUser.getUsername().equals("PASSWORD INCORRECT")) {
                        pr.write("Error: password incorrect");
                    } else if (clientUser.getUsername().equals("USER NOT FOUND")) {
                        pr.write("Error: user not found");
                    } else {
                        pr.write("Success login with Username:" + username);
                    }

                //SIGNUP
                } else if (nextInput.equals("SIGNUP")) {

                    String username = br.readLine();
                    String password = br.readLine();

                    clientUser = usrDB.signUp(username, password);
                    if (clientUser.getUsername().equals("INVALID USERNAME")) {
                        pr.write("Error: Invalid Username");

                    }
                    else if (clientUser.getUsername().equals("USERNAME TAKEN")) {
                        pr.write("Error: Username has been taken");
                    }
                    else if (clientUser.getUsername().equals("PASSWORD INVALID")) {
                        pr.write("Error: Password is invalid");

                    }
                    else {
                        pr.write("Success sign up and login with Username:" + username);
                    }
                } else if (nextInput.equals("EVENTS")) {
                    //view events

                    //single hashtag represents the start of the string
                    String returnEvents = "#";
                    ArrayList<Event> eventDB = venue1.getEventDatabase().getEvents();
                    for (int i = 0; i < eventDB.size(); i++) {
                        if (i == eventDB.size() - 1) {
                            //$$ represents seperator between event names
                            returnEvents += eventDB.get(i).getEventName() + "$$";
                        }
                        else {
                            returnEvents += eventDB.get(i).getEventName();
                        }
                    }
                    //another hashtag added at the end of the string
                    returnEvents += "#";

                    pr.write(returnEvents);
                }


                //move onto next possible selection
                //make reservation, see users current reservations, see events, change password maybe

                //get next selection input
                nextInput = br.readLine();



                pr.println();


                while (nextInput != null) {

                    System.out.println(nextInput);
                    nextInput = br.readLine();


                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

