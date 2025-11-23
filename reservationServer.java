import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.concurrent.*;


public class reservationServer {
    //venue has an events database
    //events database has a reservation database
    private Venue venue1 = new Venue();
    private UserDatabase usrDB = new UserDatabase();

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

                pr.println("CONNECTED TO SERVER");
                pr.println();

                String nextInput = br.readLine();

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

