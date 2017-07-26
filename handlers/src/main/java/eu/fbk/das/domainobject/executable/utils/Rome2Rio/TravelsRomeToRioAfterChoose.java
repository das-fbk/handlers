package eu.fbk.das.domainobject.executable.utils.Rome2Rio;

public class TravelsRomeToRioAfterChoose {
	private String start;
	private String arrive;
	private Integer duration;
	private Integer distance;
	private Integer price;
	private String vehicle;
	private String agency;
	private String position;
	
	public TravelsRomeToRioAfterChoose(String start, String arrive, Integer duration, Integer distance, Integer price,
			String vehicle, String agency, String position) {
		super();
		this.start = start;
		this.arrive = arrive;
		this.duration = duration;
		this.distance = distance;
		this.price = price;
		this.vehicle = vehicle;
		this.agency = agency;
		this.position = position;
	}
	
	public String getStart() {
		return start;
	}
	public void setStart(String start) {
		this.start = start;
	}
	public String getArrive() {
		return arrive;
	}
	public void setArrive(String arrive) {
		this.arrive = arrive;
	}
	public Integer getDuration() {
		return duration;
	}
	public void setDuration(Integer duration) {
		this.duration = duration;
	}
	public Integer getDistance() {
		return distance;
	}
	public void setDistance(Integer distance) {
		this.distance = distance;
	}
	public Integer getPrice() {
		return price;
	}
	public void setPrice(Integer price) {
		this.price = price;
	}
	public String getVehicle() {
		return vehicle;
	}
	public void setVehicle(String vehicle) {
		this.vehicle = vehicle;
	}
	public String getAgency() {
		return agency;
	}
	public void setAgency(String agency) {
		this.agency = agency;
	}
	public String getPosition() {
		return position;
	}
	public void setPosition(String position) {
		this.position = position;
	}
	
}
