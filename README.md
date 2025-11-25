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

### Event - Liam

The Event object contains information that is related to a certain event like the event name, price of event, seating chart, time of day, and total revenue.
The seating chart is stored as a 2D array where a 'o' represents an open seat, a 'x' represents a taken seat, and a '' represents a postiion with no available seat.
The event object can take in a reservation request, update its local variables like seating chart and total revenue, and then create a reservation object and store that to the events reservation database. 

### Reservation - Meraj

The Reservation object contains information to help keep track and hold data about a certain reservation created by a User. A reservation object holds data like the User, the number of people included in a reservation, the date of which the reservation was made, the price of which the reservation costs, a couple arrays that help keep track of what seats were reserved, and the event object of which the reservation is for.

### Venue - Krish / Leo

The Venue object is more or less just the overall main class that holds everything together because this system is ment for a signel venue. The venue holds other objects such as event database.

### User - Chelsea

The User object is a object that represnts a signle user that would be creating reservations for our service. A User contains things like a Username, a Password, and a reservation database object. The job of the user object is just to keep data on the user, remember the users reservations and data, and handle requests made by the user. 

## How to test
First run the ReservationServer that sets up a socket for the client to connect to. 
Then run the ReservationClient.java which will attempt to connect to the reservation server. Then you can just test away!
The first thing it will do is tell the client the venue name and events happening at that venue, and then give the client options to either sign in or make an account. 
Once the client is logged into the server under their user they can create a reservation for an event, list the events again, see their current reservations, or exit