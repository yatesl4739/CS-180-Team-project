import java.util.ArrayList;
import java.util.List;

public class VenueDatabase implements VenueDatabaseInterface {

    private List<Venue> venues;

    public VenueDatabase() {
        this.venues = new ArrayList<>();
    }

    public void addVenue(Venue venue) {
        venues.add(venue);
    }

    public void removeVenue(Venuve venue) {
        venues.remove(venue);
    }

    public Venue getVenue(String name) {
        for (Venuve v : venues) {
            if (v.getVenueName().equals(name)) {
                return v;
            }
        }
        return null;
    }

    public List<Venue> getVenues() {
        return venues;
    }
}