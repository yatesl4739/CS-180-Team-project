public interface UserInterface {



    public void setUsername(String username);
    public void setPassword(String password);
    public void setReservationDb(ReservationDatabase reservationDb);
    public String getUsername();
    public String getPassword();
    public ReservationDatabase getReservationDb();

    public void addReservation(Reservation r);
    public void removeReservation(int index);

}