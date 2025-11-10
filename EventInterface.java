public interface EventInterface {
    void setEventName(String eventName);
    void setPriceOfEvent(double priceOfEvent);
    void setSeatingChart(char[][] seatingChart);
    void setTimeOfDay(long timeOfDay);
    void setDay(long day);

    String getEventName();
    double getPriceOfEvent();
    char[][] getSeatingChart();
    long getTimeOfDay();
    long getDay();

    boolean createReservation(int[] x, int[] y, String user, int numPeople, long timeOfReservation, long date);
}