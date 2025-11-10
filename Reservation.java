public class Reservation implements ReservationInterface{
    private String user;
    private int numPeople;
    private long timeOfReservation;
    private long date;
    private double price;
    private int[][] reservedSeats;

    /**
     * constructor of object
     * @param user
     * @param numPeople
     * @param timeOfReservation
     * @param date
     * @param price
     * @param reservedSeats
     */
    public Reservation(String user, int numPeople, long timeOfReservation, long date,
                       double price, int[][] reservedSeats) {
        this.user = user;
        this.numPeople = numPeople;
        this.timeOfReservation = timeOfReservation;
        this.date = date;
        this.price = price;
        this.reservedSeats = reservedSeats;
    }

    //getters
    public String getUser() {
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

    //setters
    public void setUser(String user) {
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

    public String toString() {
        return "Reservation: \n User: " + user + "\n Time Of Reservation: " + timeOfReservation +
                "\n Date: " + date + "\n Price: " + price;
    }

}
