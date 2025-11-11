import java.io.Serializable;

/**
 * Class that is used to manage and hold data for a certain event
 *
 * @author liam yates
 *
 * @version nov 10 2025
 *
 */
public class Event implements EventInterface, Serializable {
    private String eventName;
    private double priceOfEvent;
    private volatile char[][] seatingChart; //stores available seats and shape of seats
    private long timeOfDay; // time since epoch
    private long day;
    private volatile ReservationDatabase reservationDB = new ReservationDatabase();
    private long totalRevenue;

    /**
     * constructor of object without taking totalRevenue as a modified input
     * totalRevenue is by default set to 0
     *
     * @param eventName
     * @param priceOfEvent
     * @param seatingChart
     * @param timeOfDay
     * @param day
     */
    public Event(String eventName, double priceOfEvent, char[][] seatingChart, long timeOfDay, long day) {
        this.eventName = eventName;
        this.priceOfEvent = priceOfEvent;
        this.seatingChart = seatingChart;
        this.timeOfDay = timeOfDay;
        this.day = day;
        this.totalRevenue = 0;
    }

    /**
     * constructor of object where totalRevenue is not set to default
     *
     * @param eventName
     * @param priceOfEvent
     * @param seatingChart
     * @param timeOfDay
     * @param day
     * @param totalRevenue
     */
    public Event(String eventName, double priceOfEvent, char[][] seatingChart, long timeOfDay, long day, long totalRevenue) {
        this.eventName = eventName;
        this.priceOfEvent = priceOfEvent;
        this.seatingChart = seatingChart;
        this.timeOfDay = timeOfDay;
        this.day = day;
        this.totalRevenue = totalRevenue;
    }


    //getters setters

    /**
     * used to set the name of the event
     * @param eventName
     */
    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    /**
     * used to set the price per ticket of the event
     * @param priceOfEvent
     */
    public void setPriceOfEvent(double priceOfEvent) {
        this.priceOfEvent = priceOfEvent;
    }

    /**
     * used to set the seating chart layout of the event
     * takes a 2D array of characters as input
     * within the array,
     * a 'x' represents a taken seat,
     * a 'o' represents an open seat,
     * and a ' ' represents a non available seat position.
     * @param seatingChart
     */
    public synchronized void setSeatingChart(char[][] seatingChart) {
        this.seatingChart = seatingChart;
    }

    /**
     * used to set the time of day the event will occur.
     * this is given in # of minutes since 00:00 of the current day.
     * @param timeOfDay
     */
    public void setTimeOfDay(long timeOfDay) {
        this.timeOfDay = timeOfDay;
    }

    /**
     * set the day that the event is occuring
     *
     * @param day long where it represents the # of days since the epoch
     */
    public void setDay(long day) {
        this.day = day;
    }

    /**
     * set the total revenue the event has made
     * @param newTotalRev
     */
    public void setTotalRevenue(int newTotalRev) {
        this.totalRevenue = newTotalRev;
    }

    /**
     * get the event name
     * @return String of event name
     */
    public String getEventName() {
        return eventName;
    }

    /**
     * get the price per ticket of the event
     * @return double of price of event
     */
    public double getPriceOfEvent() {
        return priceOfEvent;
    }

    /**
     * get back the seating chart of the event.
     * array contains 'x' for a taken seat,
     * 'o' for an open seat,
     * ' ' for a non available seat.
     *
     * @return 2D seating chart array
     */
    public synchronized char[][] getSeatingChart() {
        return seatingChart;
    }

    /**
     * get back the time of day in terms of min since 00:00
     * @return a long which represents mininutes since 00:00
     */
    public long getTimeOfDay() {
        return timeOfDay;
    }

    /**
     * get the day the event occurs
     * @return a long which represents the # day since epoch
     */
    public long getDay() {
        return day;
    }

    /**
     * get the total revenue the event has earned so far
     *
     * @return a long that represents the total revenue of the event
     */
    public long getTotalRevenue() {
        return totalRevenue;
    }

    /**
     * get the reservation database back
     * @return the reservation database object that is used for an event.
     */
    public ReservationDatabase getReservationDB() {
        return reservationDB;
    }

    /**
     * uses input from UI to create a reservation for this event.
     *
     * @param x
     * @param y
     * @param user
     * @param numPeople
     * @param timeOfReservation
     * @param date
     * @return boolean represents if the reservation was created properly
     */
    public synchronized boolean createReservation(int[] x, int[] y, User user, int numPeople, long timeOfReservation, long date) {
        //array of x values represents x values of seats reserved
        //array of y values represents y values of sears reserved

        double totalCostOfReservation = numPeople * priceOfEvent;

        if (x.length != y.length) {
            return false;
            //Error because there is a mismatch in data provided for seat position
        }
        for (int i = 0; i < x.length; i++) {
            if (seatingChart[x[i]][y[i]] == ' ') {
                System.out.println("the seat assignemnt at index " + i
                        + "does not exist in the seating chart");
                return false;
                //seat does not exist
            } else if (seatingChart[x[i]][y[i]] == 'x') {
                System.out.println("the seat assignemnt at index " + i
                        + "is already reserved");
                return false;
                //seat is taken
            } else if (seatingChart[x[i]][y[i]] == 'o') {
                System.out.println("Seat # " + (i + 1) + " has been reserved");
                seatingChart[x[i]][y[i]] = 'x';
                //seat is reserved and now marked as taken
            }

        }
        reservationDB.addReservation(new Reservation(this, user, numPeople, timeOfReservation, date,
                totalCostOfReservation, x, y));
        return true;
    }


}