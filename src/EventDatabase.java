import java.util.ArrayList;

/**
 * database that manages all the events.
 * holds all event objects and can be used as an interface to easily acess and mofify events.
 */
public class EventDatabase implements EventDatabaseInterface{
    private volatile ArrayList<Event> eventList = new ArrayList<Event>();

    //pass nothing
    public EventDatabase() {
        eventList = new ArrayList<Event>();

    }
    //pass a single event
    public EventDatabase(Event singleEvent) {
        eventList.add(singleEvent);
    }
    //pass list of events


    public EventDatabase(ArrayList<Event> eventList) {
        this.eventList = eventList;
    }


    //getters setters

    /**
     * get back the entire array list of saved events
     * @return ArrayList<Events> of all the events in the event databse
     */
    public synchronized ArrayList<Event> getEvents() {
        return eventList;
    }

    /**
     * taken an entire array list of events as input
     * sets the current events in the event database as the new list
     *
     * @param listInput
     */
    public synchronized void setEvents(ArrayList<Event> listInput) {
        this.eventList = listInput;
    }


    /**
     * Public method to add a single event to the list of events
     * @param eventInput
     */
    public synchronized void addEvent(Event eventInput) {
        this.eventList.add(eventInput);
    }

    /**
     * given an int, the event at that index in eventList will be removed.
     * @param eventNum
     */
    public synchronized void rmEvent(int eventNum){
        try {
            this.eventList.remove(eventNum);
        }
        catch(IndexOutOfBoundsException e) {
            System.out.println("Error: event number intputted is not a valid index");
            e.printStackTrace();
            return;
        }

    }

}

