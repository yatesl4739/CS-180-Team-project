public interface UserInterface {

    private String username;
    private String password;

    private ReservationDatabase reservationDb;

    public void setUsername(String username);
    public void setPassword(String password);
    public void setReservationDb(ReservationDatabase reservationDb);
    public String getUsername();
    public String getPassword();
    public ReservationDatabase getReservationDb();

    public void addReservation(Reservation r);
    public void removeReservation(int index);

}