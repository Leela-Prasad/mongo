import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Filters.or;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.bson.Document;
import org.bson.conversions.Bson;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Main {

	public static void main(String[] args) {

		try (MongoClient mongoClient = new MongoClient("localhost",27017)) 
		{
			MongoDatabase travelDatabase = mongoClient.getDatabase("travel");
			MongoCollection<Document> flightsCollection = travelDatabase.getCollection("flights");
  
			Bson destination = eq("destination.code","JFK");
			Bson origin = or(eq("origin.code","LHR"));
			Bson query = and(destination,origin);

			MongoCursor<Document> results = flightsCollection.find(query).iterator();
			
			//Create an arraylist of Flights to store the data retrieved from the database
			List<Flight> flights = new ArrayList<Flight>();

			while (results.hasNext()) {

				ObjectMapper mapper = new ObjectMapper();

				try {
					Flight flight = mapper.readValue(results.next().toJson(), Flight.class);
					flights.add(flight);
				} catch (JsonParseException e) {
					e.printStackTrace();
				} catch (JsonMappingException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

			//loop through the arraylist and print out the results to the console 
			for (Flight flight : flights) {
				System.out.println(flight.getAirline().getName() + " fly from " + flight.getOrigin().getCode() + " to " + flight.getDestination().getCode());
			}

									
			//Now we'll create a new flight by taking an existing flight from the arraylist and change some of its values:
			Flight newFlight = flights.get(0);

			newFlight.setId(99899l);
			Airline virtualAirways = new Airline();
			virtualAirways.setAirlineID(123456l);
			virtualAirways.setCountry("United Kingdom");
			virtualAirways.setName("Virtual Airways");
			virtualAirways.setActive(true);
			newFlight.setAirline(virtualAirways);

			try {
				
				ObjectMapper mapper = new ObjectMapper();
				String newFlightJSON = newFlightJSON = mapper.writeValueAsString(newFlight);
				System.out.println(newFlightJSON);
				flightsCollection.insertOne(Document.parse(newFlightJSON));

			} catch (JsonGenerationException e) {
				e.printStackTrace();
			} catch (JsonMappingException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}

			
			System.out.println( flightsCollection.find(new Document("_id",99899)).first().toJson());
		}
	}
}

