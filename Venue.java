import java.util.ArrayList;

public class Venue implements VenueInterface{

    //the name of the venue (e.g "AMC theaters" or "Reagal cinemas")
    private String venueName;
    //the hours of operation
    private String hoursOfOperation;
    // Stores all events for this venue using the EventDatabase class
    private EventDatabase eventDatabase;


    public Venue(String name) {
        this.venueName = name;
        this.hoursOfOperation = "Not specified";
        this.eventDatabase = new EventDatabase();
    }

    public Venue(String name, EventDatabase eventDatabase, String hoursOfOperation) {
        this.venueName = name;
        this.eventDatabase = eventDatabase;
        this.hoursOfOperation = hoursOfOperation;
    }

    public String getHoursOfOperation(){
        return this.hoursOfOperation;
    }

    public void setHoursOfOperation(String hoursOfOperation){
        this.hoursOfOperation = hoursOfOperation;
    }

    public String getVenueName() {
        return venueName;
    }

    public void setVenueName(String venueName) {
        this.venueName = venueName;
    }

    public EventDatabase getEventDatabase() {
        return eventDatabase;
    }

    public void setEventDatabase(EventDatabase eventDatabase) {
        this.eventDatabase = eventDatabase;
    }

    public ArrayList<Long> getEventTimes(){
        ArrayList<Long> times = new ArrayList<>();
        for(Event e: eventDatabase.getEvents()){
            times.add(e.getTimeOfDay());
        }
        return times;
    }

    public ArrayList<Double> getEventPrices(){
        ArrayList<Double> prices = new ArrayList<>();
        for(Event e: eventDatabase.getEvents()){
            prices.add(e.getPriceOfEvent());
        }
        return prices;
    }

    public void addEvent(Event e){
        eventDatabase.addEvent(e);
    }

    public void removeEvent(int index){
        eventDatabase.rmEvent(index);
    }


}
