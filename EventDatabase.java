import java.util.ArrayList;

public class EventDatabase implements EventDatabaseInterface{
    private ArrayList<Event> eventList = new ArrayList<Event>();

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
    public ArrayList<Event> getEvents() {
        return eventList;
    }

    public void setEvents(ArrayList<Event> listInput) {
        this.eventList = listInput;
    }


    /**
     * Public method to add a single event to the list of events
     * @param eventInput
     */
    public void addEvent(Event eventInput) {
        this.eventList.add(eventInput);
    }

    /**
     * given an int, the event at that index in eventList will be removed.
     * @param eventNum
     */
    public void rmEvent(int eventNum){
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

