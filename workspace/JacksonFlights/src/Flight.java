import com.fasterxml.jackson.annotation.JsonProperty;


public class Flight {

	private Long id;
	private Airline airline;
	private Airport origin;
	private Airport destination;
	private String[] planes;
	
	public String toString() 
	{
	   return airline.getName() + " fly from " + origin.getCode() + " to " + destination.getCode();
	}
	 
	 @JsonProperty("_id")
	public Long getId() {
		return id;
	}
	 
	 @JsonProperty("_id")
	public void setId(Long id) {
		this.id = id;
	}
	public Airline getAirline() {
		return airline;
	}
	public void setAirline(Airline airline) {
		this.airline = airline;
	}
	public Airport getOrigin() {
		return origin;
	}
	public void setOrigin(Airport origin) {
		this.origin = origin;
	}
	public Airport getDestination() {
		return destination;
	}
	public void setDestination(Airport destination) {
		this.destination = destination;
	}
	public String[] getPlanes() {
		return planes;
	}
	public void setPlanes(String[] planes) {
		this.planes = planes;
	}
	
	
	
}
