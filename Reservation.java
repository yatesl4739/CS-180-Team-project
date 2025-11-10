/**
 * Reservation
 *
 * @author Meraj Syeda
 *
 * @version 11/10/25
 */

public class Reservation implements ReservationInterface{
    private User user;
    private int numPeople;
    private long timeOfReservation;
    private long date;
    private double price;
    private int[][] reservedSeats;
    private int[] reservedX;
    private int[] reservedY;
    private Event event;

    /**
     * constructor of object
     * @param user
     * @param numPeople
     * @param timeOfReservation
     * @param date
     * @param price
     * @param x 
     * @param y
     */
    public Reservation(Event event, User user, int numPeople, long timeOfReservation, long date,
                       double price, int[] x, int[] y) {

    
    public Reservation(String user, int numPeople, long timeOfReservation, long date,
                       double price, int[][] reservedSeats) {
        this.user = user;
        this.numPeople = numPeople;
        this.timeOfReservation = timeOfReservation;
        this.date = date;
        this.price = price;
        this.event = event;
        this.reservedX = x;
        this.reservedY = y;
        this.setReservedSeats(x, y);

        //setup the reservedseats 2D array
        if (x.length == y.length) {
            this.reservedSeats = new int[2][x.length];
            for (int i = 0; i < x.length; i ++) {
                //this sets up the reserved seats 2d array so each array in the 1d array
                // represents x and y cords where the first array is x cords
                // the second array (index 1) is the y values of each seat.
                // this way multiple seat reservation positions can be stored to 1 array.
                this.reservedSeats[0][i] = x[i];
                this.reservedSeats[1][i] = y[i];
            }
        }

    }

    //should i add an empty constructor here...?



    //getters
    public User getUser() {
        return user;
    }

    public int getNumPeople() {
        return numPeople;
    }

    public long getTimeOfReservation() {
        return timeOfReservation;
    }

    public long getDate() {
        return date;
    }

    public double getPrice() {
        return price;
    }

    public int[][] getReservedSeats() {
        return reservedSeats;
    }

    public int[] getX() {
        return reservedX;
    }

    public int[] getY() {
        return reservedY;
    }

    public Event getEvent() {
        return event;
    }



    //setters
    public void setUser(User user) {
        this.user = user;
    }

    public void setNumPeople(int numPeople) {
        this.numPeople = numPeople;
    }

    public void setTimeOfReservation(long timeOfReservation) {
        this.timeOfReservation = timeOfReservation;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setReservedSeats(int[][] reservedSeats) {
        this.reservedSeats = reservedSeats;
    }

    public void setReservedSeats(int[] x, int[] y ) {

        this.reservedSeats = new int[2][x.length];

        for (int i = 0; i < x.length; i ++ ) {
            reservedSeats[0][i] = x[i];
            reservedSeats[1][i] = y[i];
        }
    }

    public void setXY(int[] x, int[] y) {
        this.reservedX = x;
        this.reservedY = y;

        this.reservedSeats = new int[2][x.length];

        for (int i = 0; i < x.length; i ++ ) {
            reservedSeats[0][i] = x[i];
            reservedSeats[1][i] = y[i];
        }
    }

    public void setY(int[] y) {
        this.reservedY = y;
    }

    public void setEvent(Event e1) {
        this.event = e1;
    }

    @Override
    public String toString() {
        return "Reservation: \n User: " + user + "\n Time Of Reservation: " + timeOfReservation +
                "\n Date: " + date + "\n Price: " + price;
    }

}
