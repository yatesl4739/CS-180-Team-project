import java.util.*;


/**
 * The Venue class represents a physical location that hosts multiple events.
 * Each Venue contains its own EventDatabase, which stores all events occurring
 * at that venue. The Venue also stores information about its name and hours
 * of operation.
 */

public class Venue implements VenueInterface{

    private final Object gatekeeper = new Object();

    //the name of the venue (e.g "AMC theaters" or "Reagal cinemas")
    private String venueName;
    //the hours of operation
    private String hoursOfOperation;
    // Stores all events for this venue using the EventDatabase class
    private EventDatabase eventDatabase;

    /**
     * Constructor for Venue that initializes only the name.
     * Hours of operation default to "Not specified" and a new EventDatabase is created.
     *
     * @param name The name of the venue.
     */
    public Venue(String name) {
        this.venueName = name;
        this.hoursOfOperation = "Not specified";
        this.eventDatabase = new EventDatabase();
    }

    /**
     * constructor for Venue that has no name, to be changed later
     */
    public Venue() {
        this.venueName = "New Venue";
        this.hoursOfOperation = "Not specified";
        this.eventDatabase = new EventDatabase();
    }

    /**
     * Constructor for Venue that initializes name, database, and hours of operation.
     *
     * @param name             The name of the venue.
     * @param eventDatabase    The EventDatabase associated with the venue.
     * @param hoursOfOperation The operating hours of the venue.
     */
    public Venue(String name, EventDatabase eventDatabase, String hoursOfOperation) {
        this.venueName = name;
        this.eventDatabase = eventDatabase;
        this.hoursOfOperation = hoursOfOperation;
    }


    //getters and setters

    /**
     * get back the hours of operation of the venue
     * @return String which contains hours of operation
     */
    public String getHoursOfOperation(){
        return this.hoursOfOperation;
    }

    /**
     * set the hours of operation of the venue
     * @param hoursOfOperation String of hours of operation of venue
     */
    public void setHoursOfOperation(String hoursOfOperation){
        this.hoursOfOperation = hoursOfOperation;
    }

    /**
     * get back the name of the venue
     * @return String which represents the venue name
     */
    public String getVenueName() {
        return venueName;
    }

    /**
     * set the venue name
     * @param venueName
     */
    public void setVenueName(String venueName) {
        this.venueName = venueName;
    }

    /**
     * get back the event database of events for a venue
     * @return EventDatabse object that holds the events for a venue
     */
    public EventDatabase getEventDatabase() {
        return eventDatabase;
    }

    /**
     * set the event database, which holds events for a venue.
     * @param eventDatabase
     */
    public void setEventDatabase(EventDatabase eventDatabase) {
        this.eventDatabase = eventDatabase;
    }


    /**
     * Returns a list of all event times for events in the database.
     *
     * @return ArrayList of event times (in epoch format).
     */
    public ArrayList<Long> getEventTimes(){

        synchronized (gatekeeper){
            ArrayList<Long> times = new ArrayList<>();
            for(Event e: eventDatabase.getEvents()){
                times.add(e.getTimeOfDay());
            }
            return times;
        }

    }

    /**
     * Returns a list of all event prices for events in the database.
     *
     * @return ArrayList of event prices.
     */
    public ArrayList<Double> getEventPrices(){
        synchronized (gatekeeper){
            ArrayList<Double> prices = new ArrayList<>();
            for(Event e: eventDatabase.getEvents()){
                prices.add(e.getPriceOfEvent());
            }
            return prices;
        }

    }


    /**
     * Returns a list of all event names for events in the database.
     *
     * @return ArrayList of event names.
     */
    public ArrayList<String> getEventNames() {
        ArrayList<String> names = new ArrayList<>();
        synchronized (gatekeeper) {
            for (Event e : eventDatabase.getEvents()) {
                names.add(e.getEventName());
            }
        }
        return names;
    }

    /**
     * Adds a new event to the venue’s EventDatabase.
     *
     * @param e The Event to add.
     */
    public void addEvent(Event e){
        eventDatabase.addEvent(e);
    }

    /**
     * Removes an event from the venue’s EventDatabase based on its index.
     *
     * @param index The index of the event to remove.
     */
    public void removeEvent(int index){
        eventDatabase.rmEvent(index);
    }



}
