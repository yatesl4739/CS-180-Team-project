public interface ReservationInterface {
    public String getUser();
    public int getNumPeople();
    public long getTimeOfReservation();
    public long getDate();
    public double getPrice();
    public int[][] getReservedSeats();

    public void setUser(String user);
    public void setNumPeople(int numPeople);
    public void setTimeOfReservation(long timeOfReservation);
    public void setDate(long date);
    public void setPrice(double price);
    public void setReservedSeats(int[][] reservedSeats);
}
