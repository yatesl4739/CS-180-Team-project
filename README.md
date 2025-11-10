# EventFlow cs 180 group project 1

## About
EventFlow is a event reservation manager built to be versatile to any seating chart for any event. 
This was built by group #4 of the Purdue cs180 group project #1 
Group 4 consisted of 
Leo Zhang, Meraj Syeda, Liam Yates, Krish Talati, and Chelsea Zhao

## Structure
There are databases of a couple different objects.
That includes databases of Reservations, Events, and Users.
Each database has IO support and saves data to the ```saveFiles``` directory.
Each public function of every class is declared in a public interface.

### Event

The Event object contains information that is related to a certain event like the event name, price of event, seating chart, time of day, and total revenue.
The seating chart is stored as a 2D array where a 'o' represents an open seat, a 'x' represents a taken seat, and a '' represents a postiion with no available seat.
The event object can take in a reservation request, update its local variables like seating chart and total revenue, and then create a reservation object and store that to the events reservation database. 

### Reservation 

### Venue

### User
