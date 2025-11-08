public class Event {
    private double priceOfEvent;
    private char[][] seatingChart; //stores available seats and shape of seats
    private long timeOfDay; // time since epoch
    private long day;

    public Event(double priceOfEvent, char[][] seatingChart, long timeOfDay, long day){
        this.priceOfEvent = priceOfEvent;
        this.seatingChart = seatingChart;
        this.timeOfDay = timeOfDay;
        this.day = day;
    }
    private ReservationDatabase reservationDb = new ReservationDatabase();

    //getters setters

    public void setPriceOfEvent(double priceOfEvent) {
        this.priceOfEvent = priceOfEvent;
    }

    public void setSeatingChart(char[][] seatingChart) {
        this.seatingChart = seatingChart;
    }

    public void setTimeOfDay(long timeOfDay) {
        this.timeOfDay = timeOfDay;
    }

    public void setDay(long day) {
        this.day = day;
    }

    public double getPriceOfEvent() {
        return priceOfEvent;
    }

    public char[][] getSeatingChart() {
        return seatingChart;
    }

    public long getTimeOfDay() {
        return timeOfDay;
    }

    public long getDay() {
        return day;
    }





}