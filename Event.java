public class Event {
    private String eventName;
    private double priceOfEvent;
    private char[][] seatingChart; //stores available seats and shape of seats
    private long timeOfDay; // time since epoch
    private long day;
    private ReservationDatabase reservatinDB = new ReservationDatabase();
    private long totalRevenue;

    /**
     * constructor of object without taking totalRevenue as a modified input
     * totalRevenue is by default set to 0
     * @param priceOfEvent
     * @param seatingChart
     * @param timeOfDay
     * @param day
     */
    public Event(double priceOfEvent, char[][] seatingChart, long timeOfDay, long day){
        this.priceOfEvent = priceOfEvent;
        this.seatingChart = seatingChart;
        this.timeOfDay = timeOfDay;
        this.day = day;
        this.totalRevenue = 0;
    }

    /**
     * constructor of object where totalRevenue is not set to default
     *
     * @param priceOfEvent
     * @param seatingChart
     * @param timeOfDay
     * @param day
     * @param totalRevenue
     */
    public Event(double priceOfEvent, char[][] seatingChart, long timeOfDay, long day, long totalRevenue){
        this.priceOfEvent = priceOfEvent;
        this.seatingChart = seatingChart;
        this.timeOfDay = timeOfDay;
        this.day = day;
        this.totalRevenue = totalRevenue;
    }


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

    /**
     * uses input from UI to create a reservation for this event.
     * @param x
     * @param y
     * @param user
     * @param numPeople
     * @param timeOfReservation
     * @param date
     * @return
     */
    public boolean createReservation(int[] x, int[] y, String user, int numPeople, long timeOfReservation, long date) {
        //array of x values represents x values of seats reserved
        //array of y values represents y values of sears reserved

        double totalCostOfReservation = numPeople * priceOfEvent;

        if (x.length != y.length) {
            return false;
            //Error beacuse there is a mismatch in data provided for seat position
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
                System.out.println("Seat # " + (i + 1) + " has been resved");
                //seat is reserved and now marked as taken
            }

        }
        reservationDB.addReservation(new Reservation(this, user, numPeople, timeOfReservation, date,
                totalCostOfReservation, x, y));
    }




    }





}
