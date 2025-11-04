import java.util.ArrayList;
public interface EventDatabaseInterface {

    //Event list to store every event going on
    private ArrayList<Event> eventList;

    //public method to get the list of events
    public ArrayList<Event> getEvents();

    //public method to set the list of events
    public void setEvents(ArrayList<Event>);

    //public method to add an event to the list of events
    public void addEvent(Event) {
        this.eventList.add(Event);
    }


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