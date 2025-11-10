import java.util.ArrayList;

public interface VenueInterface {

    // Basic property accessors

    public String getVenueName();
    public void setVenueName(String name);

    public String getHoursOfOperation();
    public void setHoursOfOperation(String hours);

    // Database accessors
    public EventDatabase getEventDatabase();
    public void setEventDatabase(EventDatabase db);

    // Event retrieval helpers
    public ArrayList<Long> getEventTimes();
    public ArrayList<Double> getEventPrices();

    // Event modification methods
    public void addEvent(Event e);
    public void removeEvent(int index);
}
