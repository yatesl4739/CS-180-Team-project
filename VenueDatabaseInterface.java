public interface VenueDataBaseIntreface {

    public void addVenue(Venue venue);

    public void removeVenue(Venue venue);

    public Venue getVenue(String name);

    java.util.List<Venue> getVenues();
}