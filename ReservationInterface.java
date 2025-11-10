public interface ReservationInterface {

    /**
     * gets user
     * @return
     */
    public String getUser();

    /**
     * @return the number of people for reservation
     */
    public int getNumPeople();

    /**
     * @return gets the time of the reservation
     */
    public long getTimeOfReservation();

    /**
     * @return the date of the reservation
     */
    public long getDate();

    /**
     * @return gets the price of the reservation
     */
    public double getPrice();

    /**
     * @return gets seats that are already reserved
     */
    public int[][] getReservedSeats();

    /**
     * replace user
     * @param user
     */
    public void setUser(String user);

    /**
     * replace number of people
     * @param numPeople
     */
    public void setNumPeople(int numPeople);

    /**
     * replace time of reservation
     * @param timeOfReservation
     */
    public void setTimeOfReservation(long timeOfReservation);

    /**
     * replace date of reservation
     * @param date
     */
    public void setDate(long date);

    /**
     * replace price of reservation
     * @param price
     */
    public void setPrice(double price);

    /**
     * replace seats reserved
     * @param reservedSeats
     */
    public void setReservedSeats(int[][] reservedSeats);
}
