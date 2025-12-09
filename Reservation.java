import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Reservation
 *
 * @author Meraj Syeda
 * @version 11/10/25
 */

public class Reservation implements ReservationInterface {
    private User user;
    private int numPeople;
    private long timeOfReservation;
    private long date;
    private double price;
    private int[][] reservedSeats;
    private int[] reservedX;
    private int[] reservedY;
    private Event event;
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("MMM dd, yyyy");

    /**
     * constructor of object
     *
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
            for (int i = 0; i < x.length; i++) {
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

    public void setReservedSeats(int[] x, int[] y) {

        this.reservedSeats = new int[2][x.length];

        for (int i = 0; i < x.length; i++) {
            reservedSeats[0][i] = x[i];
            reservedSeats[1][i] = y[i];
        }
    }

    public void setXY(int[] x, int[] y) {
        this.reservedX = x;
        this.reservedY = y;

        this.reservedSeats = new int[2][x.length];

        for (int i = 0; i < x.length; i++) {
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


    private String dateFormat(long daysSinceEpoch) {
        try {
            return LocalDate.ofEpochDay(daysSinceEpoch).format(DATE_FORMATTER);
        } catch (Exception e) {
            //if theres any errors just give back the straight up "days since epoch"
            String returnString = "" + daysSinceEpoch;
            return returnString;
        }
    }

    private String timeFormat(long rawTime) {
        try {
            int time = (int) rawTime;
            int hourOfTime = time / 100;
            int minuteOfTimr = time % 100;

            String morning = "AM";
            if (hourOfTime > 12) {
                //time is in afternoon:
                morning = "PM";
            }


            hourOfTime = hourOfTime % 12;

            if (hourOfTime == 0) {
                //if its midnight im the morning it should display 12
                hourOfTime = 12;
            }

            return String.format("%d:%02d %s", hourOfTime, minuteOfTimr, morning);
        } catch (Exception e) {
            //any issues?
            String outputString = "" + rawTime;
            return outputString;
        }

    }

    @Override
    public String toString() {
        return "Reservation: \n User: " + user.getUsername() + "\n Time Of Reservation: " + timeFormat(timeOfReservation) +
                "\n Date: " + dateFormat(date) + "\n Price: " + price;
    }

}
