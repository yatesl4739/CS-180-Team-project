public interface ReservationInterface {

    /**
     * Get the user object for that certain reservation
     * @return User object of user who made the reservation
     */
    public User getUser();

    /**
     * Get the number of people included in the reservation
     * @return int of # of people in the reservation
     */
    public int getNumPeople();

    /**
     * get the time of day of the reservation
     * @return a long of time of day of reservation as minutes since 0:00
     */
    public long getTimeOfReservation();

    /**
     * get the date of the year when the resrevation is occuring
     * @return long where the number is days since epoch
     */
    public long getDate();

    /**
     * returns the total price of a reservation
     *
     * @return double of total price of a reservation
     */
    public double getPrice();

    /**
     * the array of reserved seats
     * @return a 2D array of integers where the first column is x values and the second column is y values
     */
    public int[][] getReservedSeats();

    /**
     * the array of x values of positions of seats reserved
     * @return array of integer x value positions of seats reserved
     */
    public int[] getX();

    /**
     * the array of y values of positions of seats reserved
     * @return array of integer y value positions of seats reserved
     */
    public int[] getY();

    /**
     * set the user object related to the resrvation
     * @param user
     */
    public void setUser(User user);

    /**
     * set the number of people included in the reservation
     * @param numPeople
     */
    public void setNumPeople(int numPeople);

    /**
     * set the time of day of the reservation with the parameter being
     * a long where its minutes since 0:00
     * @param timeOfReservation
     */
    public void setTimeOfReservation(long timeOfReservation);

    /**
     * set the date of the reservation with a long being days since epoch
     * @param date
     */
    public void setDate(long date);

    /**
     * set the price of the reservation
     * @param price
     */
    public void setPrice(double price);

    /**
     * set the array of reserved seats
     * passing reserved seats array where
     * 2D array of integers where the first column is x values and the second column is y values
     * @param reservedSeats
     */
    public void setReservedSeats(int[][] reservedSeats);

    /**
     * set the x and y values of the position of reserved seats
     * @param x
     * @param y
     */
    public void setXY(int[] x, int[] y);


}
