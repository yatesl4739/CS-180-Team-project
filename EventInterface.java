public interface EventInterface {

    //setters

    /**
     * set event name
     * @param eventName
     */
    void setEventName(String eventName);

    /**
     * set event price
     * @param priceOfEvent
     */
    void setPriceOfEvent(double priceOfEvent);

    /**
     * set the current seating chart
     * @param seatingChart
     */
    void setSeatingChart(char[][] seatingChart);

    /**
     * set the time of day that the event takes place
     * this input should be in seconds since 0:00 (midnight)
     * @param timeOfDay
     */
    void setTimeOfDay(long timeOfDay);

    /**
     * set the day that the event takes place
     * this input should be in days since epoch
     * @param day
     */
    void setDay(long day);

    //getters

    /**
     * get the event name
     * @return event name as a string
     */
    String getEventName();

    /**
     * get the price of the event
     * @return price of the event as a double
     */
    double getPriceOfEvent();

    /**
     * return the seating chart 2D array
     * @return char[][] seating chart
     * the seating chart is organized so
     * 'o' is an open seat,
     * 'x' is a taken seat,
     * and a ' ' is no seat.
     */
    char[][] getSeatingChart();
    long getTimeOfDay();
    long getDay();

    /**
     * uses input from UI to create a reservation for this event.
     * @param x
     * @param y
     * @param user
     * @param numPeople
     * @param timeOfReservation
     * @param date
     * @return boolean represents if the reservation was created properly
     */
    public boolean createReservation(int[] x, int[] y, User user, int numPeople, long timeOfReservation, long date);
}