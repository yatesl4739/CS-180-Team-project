import java.util.ArrayList;
import java.io.*;

/**
 * database that manages all the events.
 * holds all event objects and can be used as an interface to easily acess and mofify events.
 *
 * the class uses an array list and object input/output io to manage and store all events for a venue
 *
 * @author liam yates
 *
 * @version nov 10th 2025
 */
public class EventDatabase implements Serializable, EventDatabaseInterface {

    private volatile ArrayList<Event> eventList = new ArrayList<Event>();
    private final File SAVE_FILE = new File("saveFiles/eventDatabase.file");

    /**
     * constructor for EventDatabase with no parameters
     * this way the database will either read from the file or init with an empty array list
     */
    public EventDatabase() {
        if (SAVE_FILE.canRead() &&
                SAVE_FILE.exists() &&
                SAVE_FILE.length() != 0) {
            eventList = getObjectFromSaveFile().getEvents();
        }
        else {
            eventList = new ArrayList<Event>();
        }

    }

    /**
     * constructor for EventDatabase
     * where the eventList will be initialized with a single event.
     * @param singleEvent is the only event that the event database will be initialized with.
     */
    public EventDatabase(Event singleEvent) {
        eventList.add(singleEvent);
    }
    //pass list of events

    /**
     * constructor for event database
     * where the events list will be initialized with an entire given event list
     * @param eventList is the arrayList that will be assigned to the DB event list
     */
    public EventDatabase(ArrayList<Event> eventList) {
        this.eventList = eventList;
    }


    //getters setters

    /**
     * get back the entire array list of saved events
     * @return ArrayList<Events> of all the events in the event database
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
        updateSaveFile();
    }


    /**
     * Public method to add a single event to the list of events
     * @param eventInput
     */
    public synchronized void addEvent(Event eventInput) {
        this.eventList.add(eventInput);
        updateSaveFile();
    }

    /**
     * given an int, the event at that index in eventList will be removed.
     * @param eventNum
     */
    public synchronized void rmEvent(int eventNum){
        try {
            this.eventList.remove(eventNum);
            updateSaveFile();
        }
        catch(IndexOutOfBoundsException e) {
            System.out.println("Error: event number inputted is not a valid index");
            e.printStackTrace();
            return;
        }

    }

    /**
     * Update the save file with the current object state
     */
    private synchronized void updateSaveFile() {



        try {
            FileOutputStream fos = new FileOutputStream(SAVE_FILE);
            ObjectOutputStream oos = new ObjectOutputStream(fos);

            oos.writeObject(this);
            oos.close();
            fos.close();
        } catch (IOException e) {
            System.out.println("Error saving to save file");
            e.printStackTrace();
            return;
        }

    }

    /**
     * get the event database from the save file to read info like event list
     *
     * @return this EventDatabase
     */
    private synchronized EventDatabase getObjectFromSaveFile() {

        try {
            FileInputStream fis = new FileInputStream(SAVE_FILE);
            ObjectInputStream ois = new ObjectInputStream(fis);
            EventDatabase fromSaveFile = null;
            try {
                fromSaveFile = (EventDatabase)ois.readObject();
            } catch (ClassNotFoundException e) {
                System.out.println("The save file is empty and or has incorrect save data");
            }
            if (fromSaveFile == null) {
                System.out.println("Error! null has been returned");
                return null;
            }
            return fromSaveFile;


        } catch (IOException e) {
            System.out.println("Error reading from the save file");
            e.printStackTrace();
            return null;
        }
    }

}

