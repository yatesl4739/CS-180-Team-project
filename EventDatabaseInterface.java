import java.util.ArrayList;

/**
 * interface to help public implementation of EventDatabase class
 */
public interface EventDatabaseInterface {


    /**
     * public method to get the list of events
     * @return ArrayList<Event>
     */
    public ArrayList<Event> getEvents();

    /**
     *
     * public method to set the list of events
     * @param listInput
     * @author liam yates
     */

    public void setEvents(ArrayList<Event> listInput);

    /**
     * Public method to add a single event to the list of events
     * @param eventInput
     * @author liam yates
     */
    public void addEvent(Event eventInput);


    /**
     * given an int, the event at that index in eventList will be removed.
     * @param eventNum
     * @author liam yates
     *
     */
    public void rmEvent(int eventNum);

}